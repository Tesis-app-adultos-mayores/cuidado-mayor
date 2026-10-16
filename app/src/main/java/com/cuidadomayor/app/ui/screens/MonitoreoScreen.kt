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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuidadomayor.app.ui.theme.*

@Composable
fun MonitoreoScreen() {
    var backupDispatched by remember { mutableStateOf(false) }
    var transportDispatched by remember { mutableStateOf(false) }
    var candidateApproved by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Ticker
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = EmergencyContainer),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("CRÍTICAS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmergencyRed)
                    Text(if (backupDispatched) "0" else "1", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = EmergencyRed)
                    Text("SOS Activo", fontSize = 9.sp, color = OnSurfaceVariant)
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("EN CURSO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                    Text("14", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    Text("Servicios", fontSize = 9.sp, color = OnSurfaceVariant)
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("PENDIENTES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                    Text(if (transportDispatched) "2" else "3", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SecondaryTerracotta)
                    Text("Despacho", fontSize = 9.sp, color = OnSurfaceVariant)
                }
            }
        }

        // Urgent SOS Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = EmergencyRed),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#SOS-104 Botón Pánico", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Hace 45s", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                }
                Text("María Elena Morales (78a)", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Ubicación: Av. Vitacura con Las Tranqueras • Descompensación leve / Solicitud apoyo médico",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp
                )

                if (!backupDispatched) {
                    Button(
                        onClick = { backupDispatched = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryContainer),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Despachar Móvil Respaldo (CM-03 / SAMU)", color = OnSecondaryFixed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "✓ Móvil CM-03 en trayecto con médico regulador",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // Specialized Transport Dispatch
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Móvil Adaptado (Rampa Baja)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("#EX-302", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 12.sp)
                }

                Text("Solicitud Express: Sra. Carmen (74 años) • Silla Eléctrica", fontSize = 12.sp, color = OnSurfaceVariant)
                Text("Conductor: Mario Sandoval (Cert. RCP) • Mercedes Sprinter (LG-4421)", fontSize = 11.sp, color = OnSurfaceText)

                Button(
                    onClick = { transportDispatched = true },
                    enabled = !transportDispatched,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (transportDispatched) "Transporte Despachado" else "Confirmar y Despachar Transporte",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Staff Validation
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Validación de Personal • Lorena Fuentes (TENS)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("480 hrs clínicas • Título validado • Cruz Roja validado", fontSize = 11.sp, color = OnSurfaceVariant)

                Button(
                    onClick = { candidateApproved = true },
                    enabled = !candidateApproved,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (candidateApproved) "Acompañante Aprobada" else "Aprobar y Habilitar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
