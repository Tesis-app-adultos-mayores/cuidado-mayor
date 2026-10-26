package com.cuidadomayor.app.data.network.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cuidadomayor.app.data.network.model.TokenResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.atomic.AtomicReference

class TokenManager(context: Context) {

    private val prefs: SharedPreferences = createEncryptedSharedPreferences(context)

    // In-memory cache for ultra-fast access in OkHttp AuthInterceptor
    private val memoryAccessToken = AtomicReference<String?>(
        prefs.getString(KEY_ACCESS_TOKEN, null)
    )

    private val _sessionExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val sessionExpiredEvent: SharedFlow<Unit> = _sessionExpiredEvent.asSharedFlow()

    @Synchronized
    fun getAccessToken(): String? {
        return memoryAccessToken.get() ?: prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    @Synchronized
    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    @Synchronized
    fun saveSession(tokenResponse: TokenResponse) {
        memoryAccessToken.set(tokenResponse.accessToken)
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, tokenResponse.accessToken)
            .putString(KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
            .putString(KEY_TOKEN_TYPE, tokenResponse.tokenType)
            .putLong(KEY_EXPIRES_IN_S, tokenResponse.expiraEnS)
            .putString(KEY_FIREBASE_TOKEN, tokenResponse.firebaseToken)
            .apply()
    }

    @Synchronized
    fun updateAccessTokenInMemory(newAccessToken: String) {
        memoryAccessToken.set(newAccessToken)
        prefs.edit().putString(KEY_ACCESS_TOKEN, newAccessToken).apply()
    }

    @Synchronized
    fun clearSession() {
        memoryAccessToken.set(null)
        prefs.edit().clear().apply()
        _sessionExpiredEvent.tryEmit(Unit)
    }

    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_FILENAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREFS_FILENAME_FALLBACK, Context.MODE_PRIVATE)
        }
    }

    companion object {
        private const val PREFS_FILENAME = "cuidado_mayor_secure_prefs"
        private const val PREFS_FILENAME_FALLBACK = "cuidado_mayor_prefs_fallback"
        private const val KEY_ACCESS_TOKEN = "key_access_token"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
        private const val KEY_TOKEN_TYPE = "key_token_type"
        private const val KEY_EXPIRES_IN_S = "key_expires_in_s"
        private const val KEY_FIREBASE_TOKEN = "key_firebase_token"
    }
}
