package com.cuidadomayor.app.data.repository

import com.cuidadomayor.app.data.network.api.AppApiService
import com.cuidadomayor.app.data.network.exception.ApiException
import com.cuidadomayor.app.data.network.model.*
import kotlinx.serialization.json.Json
import retrofit2.Response

class CuidadoRepository(
    private val appApiService: AppApiService,
    private val jsonParser: Json
) {

    // --- Catálogos y Tamizaje Barthel ---
    suspend fun evaluarBarthel(request: BarthelRequest): Result<BarthelResponse> =
        handleApiCall { appApiService.evaluarBarthel(request) }

    suspend fun getPoliticaDatosVigente(): Result<PoliticaResponse> =
        handleApiCall { appApiService.getPoliticaDatosVigente() }

    suspend fun getIntereses(): Result<List<CatalogItem>> =
        handleApiCall { appApiService.getIntereses() }

    suspend fun getModalidades(): Result<List<CatalogItem>> =
        handleApiCall { appApiService.getModalidades() }

    suspend fun getComplementos(): Result<List<CatalogItem>> =
        handleApiCall { appApiService.getComplementos() }

    // --- Adultos Mayores ---
    suspend fun registrarAdultoMayor(request: AdultoMayorRequest): Result<AdultoMayorDetalleResponse> =
        handleApiCall { appApiService.registrarAdultoMayor(request) }

    suspend fun listarAdultosMayores(): Result<List<AdultoMayorResumen>> =
        handleApiCall { appApiService.listarAdultosMayores() }

    suspend fun getAdultoMayorDetalle(adultoId: String): Result<AdultoMayorDetalleResponse> =
        handleApiCall { appApiService.getAdultoMayorDetalle(adultoId) }

    // --- Servicios ---
    suspend fun cotizarServicio(request: CotizacionRequest): Result<CotizacionResponse> =
        handleApiCall { appApiService.cotizarServicio(request) }

    suspend fun crearServicio(request: CrearServicioRequest): Result<ServicioDetalleResponse> =
        handleApiCall { appApiService.crearServicio(request) }

    suspend fun listarServicios(
        estado: String? = null,
        desde: String? = null,
        hasta: String? = null
    ): Result<List<ServicioDetalleResponse>> =
        handleApiCall { appApiService.listarServicios(estado, desde, hasta) }

    suspend fun cancelarServicio(servicioId: String, motivo: String): Result<CancelarServicioResponse> =
        handleApiCall { appApiService.cancelarServicio(servicioId, CancelarServicioRequest(motivo)) }

    // --- Utilidades internas ---
    private suspend fun <T> handleApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(parseResponseError(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
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

        if (httpCode == 422 && errorCode == "barthel_no_apto") {
            detalleMsg = "Por ahora el servicio es para adultos con Barthel de 61 o más."
        }

        return ApiException(httpCode = httpCode, error = errorCode, detalle = detalleMsg)
    }

    fun getUiErrorMessage(throwable: Throwable): String {
        if (throwable is ApiException) {
            return throwable.detalle
        }
        return throwable.message ?: "Ocurrió un error inesperado de red"
    }
}
