package com.cuidadomayor.app.data.network.util

import com.cuidadomayor.app.data.network.exception.ApiException
import com.cuidadomayor.app.data.network.model.ApiErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.Response

object ApiErrorParser {

    fun <T> parseError(response: Response<T>, jsonParser: Json): ApiException {
        val httpCode = response.code()
        val errorBodyString = response.errorBody()?.string()

        if (!errorBodyString.isNullOrBlank()) {
            try {
                val apiError = jsonParser.decodeFromString<ApiErrorResponse>(errorBodyString)
                return ApiException(
                    httpCode = httpCode,
                    error = apiError.error,
                    detalle = apiError.detalle
                )
            } catch (_: Exception) {
                // Fallback if custom non-standard error body
            }
        }

        val defaultDetail = when (httpCode) {
            401 -> "Sesión no válida o expirada"
            403 -> "Acceso no autorizado a este recurso"
            404 -> "Recurso no encontrado"
            409 -> "Conflicto en la solicitud"
            422 -> "Datos de entrada no válidos"
            429 -> "Demasiadas peticiones. Por favor intente más tarde"
            500 -> "Error interno en los servidores de Cuidado Mayor"
            else -> "Error de comunicación con el servidor ($httpCode)"
        }

        return ApiException(
            httpCode = httpCode,
            error = "http_error_$httpCode",
            detalle = defaultDetail
        )
    }
}
