package com.cuidadomayor.app.data.network

import android.content.Context
import com.cuidadomayor.app.data.network.api.AppApiService
import com.cuidadomayor.app.data.network.api.AuthApiService
import com.cuidadomayor.app.data.network.authenticator.TokenAuthenticator
import com.cuidadomayor.app.data.network.interceptor.AuthInterceptor
import com.cuidadomayor.app.data.network.storage.TokenManager
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkClient(
    context: Context,
    val baseUrl: String = DEFAULT_BASE_URL
) {

    val tokenManager: TokenManager by lazy {
        TokenManager(context.applicationContext)
    }

    // AHORA: Ignora llaves desconocidas si el servidor envía datos nuevos que la app aún no lee.
    val jsonParser: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = true
        encodeDefaults = true
    }

    private var authApiServiceInstance: AuthApiService? = null
    private var appApiServiceInstance: AppApiService? = null

    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(tokenManager)
    }

    private val tokenAuthenticator: TokenAuthenticator by lazy {
        TokenAuthenticator(
            tokenManager = tokenManager,
            authApiServiceProvider = { getAuthApiService() },
            jsonParser = jsonParser
        )
    }

    val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val retrofit: Retrofit by lazy {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(jsonParser.asConverterFactory(contentType))
            .build()
    }

    fun getAuthApiService(): AuthApiService {
        if (authApiServiceInstance == null) {
            synchronized(this) {
                if (authApiServiceInstance == null) {
                    authApiServiceInstance = retrofit.create(AuthApiService::class.java)
                }
            }
        }
        return authApiServiceInstance!!
    }

    fun getAppApiService(): AppApiService {
        if (appApiServiceInstance == null) {
            synchronized(this) {
                if (appApiServiceInstance == null) {
                    appApiServiceInstance = retrofit.create(AppApiService::class.java)
                }
            }
        }
        return appApiServiceInstance!!
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    companion object {
        const val DEFAULT_BASE_URL = "https://tesis-api-1.tail09c500.ts.net/api/v1/"
    }
}
