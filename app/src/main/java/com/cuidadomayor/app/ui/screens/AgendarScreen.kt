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
fun AgendarScreen(onBookingSuccess: () -> Unit) {
    var wheelchair by remember { mutableStateOf(false) }
    var rampVehicle by remember { mutableStateOf(true) }

    val basePrice = 44970
    val platformFee = 3500
    val wheelchairFee = if (wheelchair) 4500 else 0
    val rampFee = if (rampVehicle) 6000 else 0
    val totalPrice = basePrice + platformFee + wheelchairFee + rampFee

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Steps Bar
        Text("Pasos 4 y 5: Validación final y pago", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)

        // Resumen
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Fecha y Franja Horaria", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("Viernes, 24 de Octubre • 10:00 AM – 13:00 PM (3 horas)", fontSize = 12.sp, color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                Text("Ruta: Av. Las Tranqueras 1420 ➔ Clínica Alemana (14.8 km)", fontSize = 11.sp, color = OnSurfaceVariant)
                Text("Cuidadora asignada: Camila Valenzuela P. (4.9 ★)", fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }

        // Extras
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Complementos de Movilidad (Paso 4)", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Silla de ruedas plegable", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("+$4.500 CLP", fontSize = 11.sp, color = PrimaryGreen)
                    }
                    Checkbox(checked = wheelchair, onCheckedChange = { wheelchair = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Vehículo con rampa baja", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("+$6.000 CLP", fontSize = 11.sp, color = PrimaryGreen)
                    }
                    Checkbox(checked = rampVehicle, onCheckedChange = { rampVehicle = it })
                }
            }
        }

        // Pricing Breakdown
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Desglose de Costo Transparente (Paso 5)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Base servicio profesional (3h)", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("$$basePrice", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                if (rampVehicle) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Vehículo con rampa", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text("$$rampFee", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                if (wheelchair) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Silla de ruedas", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text("$$wheelchairFee", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tarifa plataforma y seguro", fontSize = 11.sp, color = OnSurfaceVariant)
                    Text("$$platformFee", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(color = SurfaceContainer)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Estimado", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("$$totalPrice CLP", fontWeight = FontWeight.Bold, color = SecondaryTerracotta, fontSize = 16.sp)
                }
            }
        }

        Button(
            onClick = onBookingSuccess,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Confirmar y Programar Servicio ($$totalPrice)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
