package com.cuidadomayor.app.data.network.api

import com.cuidadomayor.app.data.network.model.*
import retrofit2.Response
import retrofit2.http.*

interface AppApiService {

    // --- Catálogos y Tamizaje Barthel ---
    @POST("barthel/evaluar")
    @Headers("No-Auth: true")
    suspend fun evaluarBarthel(@Body request: BarthelRequest): Response<BarthelResponse>

    @GET("politicas-datos/vigente")
    @Headers("No-Auth: true")
    suspend fun getPoliticaDatosVigente(): Response<PoliticaResponse>

    @GET("intereses")
    @Headers("No-Auth: true")
    suspend fun getIntereses(): Response<List<CatalogItem>>

    @GET("modalidades")
    @Headers("No-Auth: true")
    suspend fun getModalidades(): Response<List<CatalogItem>>

    @GET("complementos")
    @Headers("No-Auth: true")
    suspend fun getComplementos(): Response<List<CatalogItem>>

    // --- Adultos Mayores ---
    @POST("adultos-mayores")
    suspend fun registrarAdultoMayor(@Body request: AdultoMayorRequest): Response<AdultoMayorDetalleResponse>

    @GET("adultos-mayores")
    suspend fun listarAdultosMayores(): Response<List<AdultoMayorResumen>>

    @GET("adultos-mayores/{adulto_id}")
    suspend fun getAdultoMayorDetalle(@Path("adulto_id") adultoId: String): Response<AdultoMayorDetalleResponse>

    // --- Servicios ---
    @POST("servicios/cotizacion")
    suspend fun cotizarServicio(@Body request: CotizacionRequest): Response<CotizacionResponse>

    @POST("servicios")
    suspend fun crearServicio(@Body request: CrearServicioRequest): Response<ServicioDetalleResponse>

    @GET("servicios")
    suspend fun listarServicios(
        @Query("estado") estado: String? = null,
        @Query("desde") desde: String? = null,
        @Query("hasta") hasta: String? = null
    ): Response<List<ServicioDetalleResponse>>

    @POST("servicios/{servicio_id}/cancelar")
    suspend fun cancelarServicio(
        @Path("servicio_id") servicioId: String,
        @Body request: CancelarServicioRequest
    ): Response<CancelarServicioResponse>
}
