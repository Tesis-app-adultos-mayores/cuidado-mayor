package com.cuidadomayor.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cuidadomayor.app.data.network.NetworkClient
import com.cuidadomayor.app.data.network.model.*
import com.cuidadomayor.app.data.repository.CuidadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class CotizacionUiState {
    object Idle : CotizacionUiState()
    object Loading : CotizacionUiState()
    data class Success(val response: CotizacionResponse) : CotizacionUiState()
    data class Error(val message: String) : CotizacionUiState()
}

sealed class CrearServicioUiState {
    object Idle : CrearServicioUiState()
    object Loading : CrearServicioUiState()
    data class Success(val response: ServicioDetalleResponse) : CrearServicioUiState()
    data class Error(val message: String) : CrearServicioUiState()
}

class AgendarViewModel(application: Application) : AndroidViewModel(application) {

    private val networkClient = NetworkClient(application)
    private val cuidadoRepository = CuidadoRepository(
        appApiService = networkClient.getAppApiService(),
        jsonParser = networkClient.jsonParser
    )

    private val _cotizacionState = MutableStateFlow<CotizacionUiState>(CotizacionUiState.Idle)
    val cotizacionState: StateFlow<CotizacionUiState> = _cotizacionState.asStateFlow()

    private val _crearServicioState = MutableStateFlow<CrearServicioUiState>(CrearServicioUiState.Idle)
    val crearServicioState: StateFlow<CrearServicioUiState> = _crearServicioState.asStateFlow()
    
    var lastCotizacionRequest: CotizacionRequest? = null

    fun cotizarServicio(request: CotizacionRequest) {
        viewModelScope.launch {
            _cotizacionState.value = CotizacionUiState.Loading
            val result = cuidadoRepository.cotizarServicio(request)
            
            result.onSuccess { response ->
                lastCotizacionRequest = request
                _cotizacionState.value = CotizacionUiState.Success(response)
            }.onFailure { throwable ->
                _cotizacionState.value = CotizacionUiState.Error(cuidadoRepository.getUiErrorMessage(throwable))
            }
        }
    }

    fun crearServicio() {
        val cotizacion = lastCotizacionRequest ?: return
        
        viewModelScope.launch {
            _crearServicioState.value = CrearServicioUiState.Loading
            
            val request = CrearServicioRequest(
                adultoMayorId = cotizacion.adultoMayorId,
                modalidadId = cotizacion.modalidadId,
                tipo = cotizacion.tipo,
                franja = cotizacion.franja,
                inicioProgramado = cotizacion.inicioProgramado,
                paradas = cotizacion.paradas,
                complementosIds = cotizacion.complementosIds,
                notas = "Servicio agendado desde Android App"
            )
            
            val result = cuidadoRepository.crearServicio(request)
            
            result.onSuccess { response ->
                _crearServicioState.value = CrearServicioUiState.Success(response)
            }.onFailure { throwable ->
                _crearServicioState.value = CrearServicioUiState.Error(cuidadoRepository.getUiErrorMessage(throwable))
            }
        }
    }

    fun resetStates() {
        _cotizacionState.value = CotizacionUiState.Idle
        _crearServicioState.value = CrearServicioUiState.Idle
    }
}
