package com.cuidadomayor.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cuidadomayor.app.data.network.NetworkClient
import com.cuidadomayor.app.data.network.model.AdultoMayorResumen
import com.cuidadomayor.app.data.repository.CuidadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Empty(val message: String = "No tienes adultos mayores registrados. Agrega uno para comenzar.") : HomeUiState()
    data class HasData(val adultosMayores: List<AdultoMayorResumen>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val networkClient = NetworkClient(application)
    private val cuidadoRepository = CuidadoRepository(
        appApiService = networkClient.getAppApiService(),
        jsonParser = networkClient.jsonParser
    )

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun cargarAdultosMayores() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = cuidadoRepository.listarAdultosMayores()

            result.onSuccess { lista ->
                if (lista.isEmpty()) {
                    _uiState.value = HomeUiState.Empty()
                } else {
                    _uiState.value = HomeUiState.HasData(lista)
                }
            }.onFailure { throwable ->
                _uiState.value = HomeUiState.Error(cuidadoRepository.getUiErrorMessage(throwable))
            }
        }
    }
}
