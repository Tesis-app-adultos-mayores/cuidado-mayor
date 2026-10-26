package com.cuidadomayor.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cuidadomayor.app.data.network.NetworkClient
import com.cuidadomayor.app.data.network.model.AdultoMayorDetalleResponse
import com.cuidadomayor.app.data.network.model.AdultoMayorRequest
import com.cuidadomayor.app.data.network.model.BarthelRequest
import com.cuidadomayor.app.data.network.model.BarthelResponse
import com.cuidadomayor.app.data.repository.CuidadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BarthelUiState {
    object Idle : BarthelUiState()
    object Loading : BarthelUiState()
    data class Success(val response: BarthelResponse) : BarthelUiState()
    data class Error(val message: String) : BarthelUiState()
}

sealed class RegistroAdultoUiState {
    object Idle : RegistroAdultoUiState()
    object Loading : RegistroAdultoUiState()
    data class Success(val response: AdultoMayorDetalleResponse) : RegistroAdultoUiState()
    data class Error(val message: String) : RegistroAdultoUiState()
}

class AdultoMayorViewModel(application: Application) : AndroidViewModel(application) {

    private val networkClient = NetworkClient(application)
    private val cuidadoRepository = CuidadoRepository(
        appApiService = networkClient.getAppApiService(),
        jsonParser = networkClient.jsonParser
    )

    private val _barthelState = MutableStateFlow<BarthelUiState>(BarthelUiState.Idle)
    val barthelState: StateFlow<BarthelUiState> = _barthelState.asStateFlow()

    private val _registroState = MutableStateFlow<RegistroAdultoUiState>(RegistroAdultoUiState.Idle)
    val registroState: StateFlow<RegistroAdultoUiState> = _registroState.asStateFlow()

    var lastBarthelRequest: BarthelRequest? = null

    fun evaluarBarthel(request: BarthelRequest) {
        viewModelScope.launch {
            _barthelState.value = BarthelUiState.Loading
            val result = cuidadoRepository.evaluarBarthel(request)
            
            result.onSuccess { response ->
                lastBarthelRequest = request
                _barthelState.value = BarthelUiState.Success(response)
            }.onFailure { throwable ->
                _barthelState.value = BarthelUiState.Error(cuidadoRepository.getUiErrorMessage(throwable))
            }
        }
    }

    fun registrarAdultoMayor(request: AdultoMayorRequest) {
        viewModelScope.launch {
            _registroState.value = RegistroAdultoUiState.Loading
            val result = cuidadoRepository.registrarAdultoMayor(request)
            
            result.onSuccess { response ->
                _registroState.value = RegistroAdultoUiState.Success(response)
            }.onFailure { throwable ->
                _registroState.value = RegistroAdultoUiState.Error(cuidadoRepository.getUiErrorMessage(throwable))
            }
        }
    }

    fun resetStates() {
        // Al resetear, no borramos 'lastBarthelRequest' para que pueda pasar a la siguiente pantalla
        _barthelState.value = BarthelUiState.Idle
        _registroState.value = RegistroAdultoUiState.Idle
    }
}
