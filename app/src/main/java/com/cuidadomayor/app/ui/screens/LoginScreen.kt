package com.cuidadomayor.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuidadomayor.app.ui.theme.*

@Composable
fun LoginScreen(onClose: () -> Unit) {
    var emailOrRut by remember { mutableStateOf("carolina.morales@correo.com") }
    var password by remember { mutableStateOf("••••••••••••") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Cerrar")
            }
            Text("Cuidado Mayor", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 14.sp)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Bienvenido de vuelta", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
            Text(
                "Ingresa a tu cuenta para coordinar o acompañar el cuidado con tranquilidad.",
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        OutlinedTextField(
            value = emailOrRut,
            onValueChange = { emailOrRut = it },
            label = { Text("Correo o RUT") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Iniciar Sesión Segura", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = PrimaryGreen)
                Text("Ingresar con Huella / Biometría", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}
