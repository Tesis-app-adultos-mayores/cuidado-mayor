package com.cuidadomayor.app.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("expira_en_s")
    val expiraEnS: Long,
    @SerialName("firebase_token")
    val firebaseToken: String,
    @SerialName("usuario")
    val usuario: UsuarioData
)

@Serializable
data class UsuarioData(
    @SerialName("id")
    val id: String,
    @SerialName("rol")
    val rol: String,
    @SerialName("correo")
    val correo: String,
    @SerialName("nombres")
    val nombres: String,
    @SerialName("perfil_id")
    val perfilId: String,
    @SerialName("estado_acompanante")
    val estadoAcompanante: String? = null
)

@Serializable
data class ApiErrorResponse(
    @SerialName("error")
    val error: String,
    @SerialName("detalle")
    val detalle: String
)
