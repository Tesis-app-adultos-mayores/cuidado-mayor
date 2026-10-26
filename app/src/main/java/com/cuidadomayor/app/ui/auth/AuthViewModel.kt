package com.cuidadomayor.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cuidadomayor.app.data.network.NetworkClient
import com.cuidadomayor.app.data.network.model.*
import com.cuidadomayor.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: UsuarioData) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val networkClient = NetworkClient(application)
    private val authRepository = AuthRepository(
        context = application,
        authApiService = networkClient.getAuthApiService(),
        tokenManager = networkClient.tokenManager,
        jsonParser = networkClient.jsonParser
    )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<UsuarioData?>(null)
    val currentUser: StateFlow<UsuarioData?> = _currentUser.asStateFlow()

    init {
        // Listen for session expiration to redirect to Login
        viewModelScope.launch {
            networkClient.tokenManager.sessionExpiredEvent.collectLatest {
                _currentUser.value = null
                _uiState.value = AuthUiState.Error("Tu sesión ha expirado. Por favor ingresa de nuevo.")
            }
        }
    }

    fun login(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor ingresa correo y contraseña")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(correo.trim(), clave)
            result.onSuccess { tokenResponse ->
                _currentUser.value = tokenResponse.usuario
                _uiState.value = AuthUiState.Success(tokenResponse.usuario)
            }.onFailure { throwable ->
                val errorMsg = authRepository.getUiErrorMessage(throwable)
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    fun registroFamilia(
        correo: String,
        clave: String,
        nombres: String,
        apellidos: String,
        tipoDocumento: String,
        numeroDocumento: String,
        telefono: String
    ) {
        if (correo.isBlank() || clave.isBlank() || nombres.isBlank() || numeroDocumento.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa los campos requeridos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = RegistroFamiliaRequest(
                correo = correo.trim(),
                clave = clave,
                nombres = nombres.trim(),
                apellidos = apellidos.trim(),
                tipoDocumento = tipoDocumento,
                numeroDocumento = numeroDocumento.trim(),
                telefono = telefono.trim()
            )
            val result = authRepository.registroFamilia(request)
            result.onSuccess { tokenResponse ->
                _currentUser.value = tokenResponse.usuario
                _uiState.value = AuthUiState.Success(tokenResponse.usuario)
            }.onFailure { throwable ->
                val errorMsg = authRepository.getUiErrorMessage(throwable)
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    fun registroAcompanante(
        correo: String,
        clave: String,
        nombres: String,
        apellidos: String,
        tipoDocumento: String,
        numeroDocumento: String,
        fechaNacimiento: String,
        genero: String,
        telefono: String,
        vinculacion: String,
        interesesIds: List<Int>,
        perfilCorto: String
    ) {
        if (correo.isBlank() || clave.isBlank() || nombres.isBlank() || numeroDocumento.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor completa los campos requeridos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = RegistroAcompananteRequest(
                correo = correo.trim(),
                clave = clave,
                nombres = nombres.trim(),
                apellidos = apellidos.trim(),
                tipoDocumento = tipoDocumento,
                numeroDocumento = numeroDocumento.trim(),
                fechaNacimiento = fechaNacimiento.trim(),
                genero = genero,
                telefono = telefono.trim(),
                vinculacion = vinculacion,
                interesesIds = interesesIds,
                perfilCorto = perfilCorto.trim()
            )
            val result = authRepository.registroAcompanante(request)
            result.onSuccess { tokenResponse ->
                _currentUser.value = tokenResponse.usuario
                _uiState.value = AuthUiState.Success(tokenResponse.usuario)
            }.onFailure { throwable ->
                val errorMsg = authRepository.getUiErrorMessage(throwable)
                _uiState.value = AuthUiState.Error(errorMsg)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            authRepository.logout()
            _currentUser.value = null
            _uiState.value = AuthUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
