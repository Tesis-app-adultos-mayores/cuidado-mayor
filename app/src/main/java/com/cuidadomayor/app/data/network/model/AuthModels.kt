package com.cuidadomayor.app.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("correo")
    val correo: String,
    @SerialName("clave")
    val clave: String
)

@Serializable
data class RegistroFamiliaRequest(
    @SerialName("correo")
    val correo: String,
    @SerialName("clave")
    val clave: String,
    @SerialName("nombres")
    val nombres: String,
    @SerialName("apellidos")
    val apellidos: String,
    @SerialName("tipo_documento")
    val tipoDocumento: String,
    @SerialName("numero_documento")
    val numeroDocumento: String,
    @SerialName("telefono")
    val telefono: String
)

@Serializable
data class RegistroAcompananteRequest(
    @SerialName("correo")
    val correo: String,
    @SerialName("clave")
    val clave: String,
    @SerialName("nombres")
    val nombres: String,
    @SerialName("apellidos")
    val apellidos: String,
    @SerialName("tipo_documento")
    val tipoDocumento: String,
    @SerialName("numero_documento")
    val numeroDocumento: String,
    @SerialName("fecha_nacimiento")
    val fechaNacimiento: String,
    @SerialName("genero")
    val genero: String,
    @SerialName("telefono")
    val telefono: String,
    @SerialName("vinculacion")
    val vinculacion: String,
    @SerialName("intereses_ids")
    val interesesIds: List<Int>,
    @SerialName("perfil_corto")
    val perfilCorto: String,
    @SerialName("empresa_aliada_id")
    val empresaAliadaId: String? = null
)

@Serializable
data class LogoutRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class DispositivoRequest(
    @SerialName("fcm_token")
    val fcmToken: String,
    @SerialName("plataforma")
    val plataforma: String = "android"
)
