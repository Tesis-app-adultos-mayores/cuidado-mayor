package com.cuidadomayor.app.data.repository

import android.content.Context
import com.cuidadomayor.app.data.network.api.AuthApiService
import com.cuidadomayor.app.data.network.exception.ApiException
import com.cuidadomayor.app.data.network.model.*
import com.cuidadomayor.app.data.network.storage.TokenManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import retrofit2.Response

class AuthRepository(
    private val context: Context,
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager,
    private val jsonParser: Json
) {

    private fun getFirebaseAuthSafely(): FirebaseAuth? {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            FirebaseAuth.getInstance()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun login(correo: String, clave: String): Result<TokenResponse> {
        return handleAuthCall {
            authApiService.login(LoginRequest(correo = correo, clave = clave))
        }
    }

    suspend fun registroFamilia(request: RegistroFamiliaRequest): Result<TokenResponse> {
        return handleAuthCall {
            authApiService.registroFamilia(request)
        }
    }

    suspend fun registroAcompanante(request: RegistroAcompananteRequest): Result<TokenResponse> {
        return handleAuthCall {
            authApiService.registroAcompanante(request)
        }
    }

    suspend fun logout(): Result<Unit> {
        val refreshToken = tokenManager.getRefreshToken()

        // 1. Firebase Auth SignOut
        try {
            getFirebaseAuthSafely()?.signOut()
        } catch (_: Exception) {}

        // 2. Call backend Logout endpoint
        return if (!refreshToken.isNullOrBlank()) {
            try {
                authApiService.logout(LogoutRequest(refreshToken))
                tokenManager.clearSession()
                Result.success(Unit)
            } catch (e: Exception) {
                tokenManager.clearSession()
                Result.success(Unit)
            }
        } else {
            tokenManager.clearSession()
            Result.success(Unit)
        }
    }

    suspend fun registrarDispositivo(fcmToken: String): Result<Unit> {
        return try {
            val response = authApiService.registrarDispositivo(
                DispositivoRequest(fcmToken = fcmToken, plataforma = "android")
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(parseResponseError(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun handleAuthCall(
        call: suspend () -> Response<TokenResponse>
    ): Result<TokenResponse> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                val tokenResponse = response.body()!!

                // Save session in EncryptedSharedPreferences and memory
                tokenManager.saveSession(tokenResponse)

                // Sync with Firebase Auth
                authenticateWithFirebase(tokenResponse.firebaseToken)

                Result.success(tokenResponse)
            } else {
                Result.failure(parseResponseError(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun authenticateWithFirebase(firebaseCustomToken: String) {
        if (firebaseCustomToken.isBlank()) return
        val auth = getFirebaseAuthSafely() ?: return

        try {
            auth.signInWithCustomToken(firebaseCustomToken).await()
        } catch (e: Exception) {
            val isUnauthenticated = e is FirebaseAuthException &&
                (e.errorCode.contains("UNAUTHENTICATED", ignoreCase = true) ||
                 e.errorCode.contains("INVALID_CUSTOM_TOKEN", ignoreCase = true) ||
                 e.errorCode.contains("CUSTOM_TOKEN_MISMATCH", ignoreCase = true))

            if (isUnauthenticated) {
                val refreshToken = tokenManager.getRefreshToken()
                if (!refreshToken.isNullOrBlank()) {
                    val refreshCall = authApiService.refreshTokenSync(RefreshRequest(refreshToken))
                    val refreshResponse = refreshCall.execute()
                    if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                        val newTokenResponse = refreshResponse.body()!!
                        tokenManager.saveSession(newTokenResponse)
                        auth.signInWithCustomToken(newTokenResponse.firebaseToken).await()
                    }
                }
            }
        }
    }

    private fun <T> parseResponseError(response: Response<T>): ApiException {
        val httpCode = response.code()
        val errorBodyString = response.errorBody()?.string()

        var errorCode = "http_error_$httpCode"
        var detalleMsg = "Error en la solicitud ($httpCode)"

        if (!errorBodyString.isNullOrBlank()) {
            try {
                val apiError = jsonParser.decodeFromString<ApiErrorResponse>(errorBodyString)
                errorCode = apiError.error
                detalleMsg = apiError.detalle
            } catch (_: Exception) {}
        }

        // Manejo especial de HTTP 429 con Retry-After header
        if (httpCode == 429) {
            val retryAfterSeconds = response.headers()["Retry-After"]?.toIntOrNull() ?: 60
            val minutes = Math.max(1, retryAfterSeconds / 60)
            detalleMsg = "Demasiados intentos. Intenta de nuevo en $minutes min"
            errorCode = "cuenta_bloqueada"
        }

        return ApiException(httpCode = httpCode, error = errorCode, detalle = detalleMsg)
    }

    fun getUiErrorMessage(throwable: Throwable): String {
        if (throwable is ApiException) {
            return when (throwable.error) {
                "credenciales_invalidas" -> "Correo o clave incorrectos"
                "cuenta_inactiva" -> "Tu cuenta no está activa. Contacta a soporte."
                "cuenta_bloqueada" -> throwable.detalle
                "duplicado" -> throwable.detalle
                else -> throwable.detalle
            }
        }
        return throwable.message ?: "Ocurrió un error inesperado de red"
    }
}
