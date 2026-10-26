package com.cuidadomayor.app.data.network.api

import com.cuidadomayor.app.data.network.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/refresh")
    @Headers("No-Auth: true")
    fun refreshTokenSync(
        @Body request: RefreshRequest
    ): Call<TokenResponse>

    @POST("auth/login")
    @Headers("No-Auth: true")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokenResponse>

    @POST("auth/registro/familia")
    @Headers("No-Auth: true")
    suspend fun registroFamilia(
        @Body request: RegistroFamiliaRequest
    ): Response<TokenResponse>

    @POST("auth/registro/acompanante")
    @Headers("No-Auth: true")
    suspend fun registroAcompanante(
        @Body request: RegistroAcompananteRequest
    ): Response<TokenResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<Unit>

    @POST("dispositivos")
    suspend fun registrarDispositivo(
        @Body request: DispositivoRequest
    ): Response<Unit>
}
