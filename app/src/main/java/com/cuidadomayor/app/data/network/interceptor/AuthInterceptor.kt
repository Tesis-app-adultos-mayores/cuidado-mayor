package com.cuidadomayor.app.data.network.interceptor

import com.cuidadomayor.app.data.network.storage.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Check if request is explicitly marked as public (No-Auth) or is the refresh endpoint
        val noAuthHeader = originalRequest.header("No-Auth")
        val isRefreshEndpoint = originalRequest.url.encodedPath.endsWith("auth/refresh")

        if (noAuthHeader != null || isRefreshEndpoint) {
            val requestWithoutNoAuth = originalRequest.newBuilder()
                .removeHeader("No-Auth")
                .build()
            return chain.proceed(requestWithoutNoAuth)
        }

        val accessToken = tokenManager.getAccessToken()
        return if (!accessToken.isNullOrBlank()) {
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
