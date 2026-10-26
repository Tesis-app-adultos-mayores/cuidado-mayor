package com.cuidadomayor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cuidadomayor.app.ui.navigation.AppNavigation
import com.cuidadomayor.app.ui.theme.CuidadoMayorTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cuidadomayor.app.ui.auth.AuthViewModel
import android.app.Application

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel {
                AuthViewModel(applicationContext as Application)
            }

            CuidadoMayorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(authViewModel = authViewModel)
                }
            }
        }
    }
}
