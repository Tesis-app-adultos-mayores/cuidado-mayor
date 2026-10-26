package com.cuidadomayor.app.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// --- Barthel & Políticas ---
@Serializable
data class BarthelRequest(
    val comer: Int,
    val trasladarse: Int,
    @SerialName("aseo_personal") val aseoPersonal: Int,
    @SerialName("uso_retrete") val usoRetrete: Int,
    val banarse: Int,
    val desplazarse: Int,
    @SerialName("subir_escaleras") val subirEscaleras: Int,
    val vestirse: Int,
    @SerialName("control_heces") val controlHeces: Int,
    @SerialName("control_orina") val controlOrina: Int
)

@Serializable
data class BarthelResponse(
    val puntaje: Int,
    val apto: Boolean,
    @SerialName("puntaje_minimo") val puntajeMinimo: Int? = null
)

@Serializable
data class BarthelSimpleResponse(
    val id: String? = null,
    val puntaje: Int? = null,
    val apto: Boolean? = null,
    val tipo: String? = null,
    @SerialName("realizada_en") val realizadaEn: String? = null
)

@Serializable
data class PoliticaResponse(
    val version: String,
    @SerialName("url_documento") val urlDocumento: String,
    @SerialName("vigente_desde") val vigenteDesde: String
)

@Serializable
data class CatalogItem(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)

// --- Adulto Mayor ---
@Serializable
data class UbicacionData(
    val lat: Double,
    val lng: Double
)

@Serializable
data class ContactoEmergenciaData(
    val nombre: String,
    val telefono: String,
    val parentesco: String
)

@Serializable
data class FichaCuidadoData(
    val movilidad: String,
    @SerialName("ayudas_tecnicas") val ayudasTecnicas: List<String>,
    val alergias: String,
    val medicamentos: String,
    val indicaciones: String
)

@Serializable
data class ConsentimientoData(
    @SerialName("politica_version") val politicaVersion: String,
    @SerialName("nombre_firmante") val nombreFirmante: String,
    @SerialName("firmado_por_representante") val firmadoPorRepresentante: Boolean,
    @SerialName("incluye_datos_salud") val incluyeDatosSalud: Boolean,
    @SerialName("incluye_ubicacion") val incluyeUbicacion: Boolean
)

@Serializable
data class AdultoMayorRequest(
    val parentesco: String,
    val nombres: String,
    val apellidos: String,
    @SerialName("tipo_documento") val tipoDocumento: String,
    @SerialName("numero_documento") val numeroDocumento: String,
    @SerialName("fecha_nacimiento") val fechaNacimiento: String,
    val genero: String,
    val telefono: String,
    val direccion: String,
    @SerialName("direccion_referencias") val direccionReferencias: String? = null,
    val ubicacion: UbicacionData,
    @SerialName("eps_o_prepagada") val epsOPrepagada: String,
    @SerialName("tiene_deterioro_cognitivo") val tieneDeterioroCognitivo: Boolean,
    @SerialName("preferencia_genero_acomp") val preferenciaGeneroAcomp: String,
    @SerialName("temas_a_evitar") val temasAEvitar: String? = null,
    @SerialName("intereses_ids") val interesesIds: List<Int>,
    @SerialName("pin_entrega") val pinEntrega: String,
    @SerialName("contactos_emergencia") val contactosEmergencia: List<ContactoEmergenciaData>,
    @SerialName("ficha_cuidado") val fichaCuidado: FichaCuidadoData,
    val consentimiento: ConsentimientoData,
    val barthel: BarthelRequest
)

@Serializable
data class AdultoMayorDetalleResponse(
    val id: String,
    val estado: String,
    val parentesco: String,
    val nombres: String,
    val apellidos: String,
    @SerialName("tipo_documento") val tipoDocumento: String,
    @SerialName("numero_documento") val numeroDocumento: String,
    @SerialName("fecha_nacimiento") val fechaNacimiento: String,
    val edad: Int? = null,
    val genero: String,
    val telefono: String,
    val direccion: String,
    @SerialName("direccion_referencias") val direccionReferencias: String? = null,
    val ubicacion: UbicacionData,
    @SerialName("eps_o_prepagada") val epsOPrepagada: String,
    @SerialName("tiene_deterioro_cognitivo") val tieneDeterioroCognitivo: Boolean,
    @SerialName("preferencia_genero_acomp") val preferenciaGeneroAcomp: String,
    @SerialName("temas_a_evitar") val temasAEvitar: String? = null,
    // Opcionalmente podemos recibir la lista de intereses parseada o ignorarla
    @SerialName("contactos_emergencia") val contactosEmergencia: List<ContactoEmergenciaData>,
    @SerialName("ficha_cuidado") val fichaCuidado: FichaCuidadoData,
    val consentimiento: ConsentimientoData,
    // AQUI ESTA LA MAGIA: El servidor nos devuelve un resumen del test, NO las 10 preguntas
    val barthel: BarthelSimpleResponse? = null
)

@Serializable
data class AdultoMayorResumen(
    val id: String,
    val nombres: String,
    val apellidos: String,
    val edad: Int,
    val estado: String,
    @SerialName("consentimiento_vigente") val consentimientoVigente: Boolean,
    @SerialName("puntaje_barthel") val puntajeBarthel: Int? = null
)

// --- Servicios ---
@Serializable
data class ParadaData(
    val direccion: String,
    val ubicacion: UbicacionData,
    val proposito: String
)

@Serializable
data class CotizacionRequest(
    @SerialName("adulto_mayor_id") val adultoMayorId: String,
    @SerialName("modalidad_id") val modalidadId: Int,
    val tipo: String,
    val franja: String,
    @SerialName("inicio_programado") val inicioProgramado: String,
    val paradas: List<ParadaData>,
    @SerialName("complementos_ids") val complementosIds: List<Int>
)

@Serializable
data class ConceptoData(
    val concepto: String,
    val detalle: String,
    @SerialName("valor_cop") val valorCop: Int
)

@Serializable
data class CotizacionResponse(
    @SerialName("modelo_tarifa") val modeloTarifa: String,
    @SerialName("fin_estimado") val finEstimado: String,
    @SerialName("valor_acompanamiento_cop") val valorAcompanamientoCop: Int,
    @SerialName("valor_transporte_cop") val valorTransporteCop: Int,
    @SerialName("valor_complementos_cop") val valorComplementosCop: Int,
    @SerialName("total_aproximado_cop") val totalAproximadoCop: Int,
    @SerialName("distancia_estimada_m") val distanciaEstimadaM: Int,
    @SerialName("minutos_franja") val minutosFranja: Int,
    @SerialName("minutos_tolerancia") val minutosTolerancia: Int? = null,
    val conceptos: List<ConceptoData>,
    val aviso: String
)

@Serializable
data class CrearServicioRequest(
    @SerialName("adulto_mayor_id") val adultoMayorId: String,
    @SerialName("modalidad_id") val modalidadId: Int,
    val tipo: String,
    val franja: String,
    @SerialName("inicio_programado") val inicioProgramado: String,
    val paradas: List<ParadaData>,
    @SerialName("complementos_ids") val complementosIds: List<Int>,
    val notas: String? = null,
    @SerialName("acompanante_solicitado_id") val acompananteSolicitadoId: String? = null
)

@Serializable
data class ServicioDetalleResponse(
    val id: String,
    val estado: String,
    val notas: String? = null
)

@Serializable
data class CancelarServicioRequest(
    val motivo: String
)

@Serializable
data class CancelarServicioResponse(
    @SerialName("servicio_id") val servicioId: String,
    val estado: String,
    @SerialName("con_cargo") val conCargo: Boolean,
    @SerialName("cargo_estimado_cop") val cargoEstimadoCop: Int
)
