package com.cuidadomayor.app.data.network.authenticator

import com.cuidadomayor.app.data.network.api.AuthApiService
import com.cuidadomayor.app.data.network.model.ApiErrorResponse
import com.cuidadomayor.app.data.network.model.RefreshRequest
import com.cuidadomayor.app.data.network.storage.TokenManager
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    private val authApiServiceProvider: () -> AuthApiService,
    private val jsonParser: Json
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val path = response.request.url.encodedPath
        if (path.endsWith("auth/refresh") || path.endsWith("auth/login") || path.contains("auth/registro")) {
            tokenManager.clearSession()
            return null
        }

        // Prevent infinite loops: If we have already retried once and it failed again with 401, stop.
        if (response.priorResponse != null) {
            tokenManager.clearSession()
            return null
        }

        val errorType = parseApiErrorType(response)

        if (errorType == ERROR_TOKEN_REUTILIZADO) {
            tokenManager.clearSession()
            return null
        }

        val requestAccessToken = extractBearerToken(response.request)

        synchronized(this) {
            val currentAccessToken = tokenManager.getAccessToken()

            // 1. Check if another concurrent thread already refreshed the token
            if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestAccessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            // 2. Perform synchronized token refresh
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                tokenManager.clearSession()
                return null
            }

            return try {
                val authApiService = authApiServiceProvider()
                val refreshCall = authApiService.refreshTokenSync(RefreshRequest(refreshToken))
                val refreshResponse = refreshCall.execute()

                if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                    val newTokenResponse = refreshResponse.body()!!

                    // Rotate token & update in encrypted storage + memory
                    tokenManager.saveSession(newTokenResponse)

                    // Retry original request with new access token
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newTokenResponse.accessToken}")
                        .build()
                } else {
                    tokenManager.clearSession()
                    null
                }
            } catch (e: Exception) {
                tokenManager.clearSession()
                null
            }
        }
    }

    private fun parseApiErrorType(response: Response): String? {
        return try {
            val responseBodyString = response.peekBody(1024 * 1024).string()
            if (responseBodyString.isNotBlank()) {
                val apiError = jsonParser.decodeFromString<ApiErrorResponse>(responseBodyString)
                apiError.error
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun extractBearerToken(request: Request): String? {
        val authHeader = request.header("Authorization") ?: return null
        return if (authHeader.startsWith("Bearer ", ignoreCase = true)) {
            authHeader.substring(7).trim()
        } else null
    }

    companion object {
        const val ERROR_TOKEN_VENCIDO = "token_vencido"
        const val ERROR_TOKEN_REUTILIZADO = "token_reutilizado"
    }
}
