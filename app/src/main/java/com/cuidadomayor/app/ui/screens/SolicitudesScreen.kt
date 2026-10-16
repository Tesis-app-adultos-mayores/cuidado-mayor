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
fun SolicitudesScreen(onOpenService: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Radar status
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(PrimaryGreen))
                    Column {
                        Text("Conectado / Recibiendo solicitudes", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryGreen)
                        Text("Radar activo en tiempo real • Hasta 7 km", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }
                Surface(color = PrimaryFixed.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp)) {
                    Text("Alta demanda", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
        }

        // Recommended Mission Card (María Elena 98% affinity)
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌟 Recomendada para Ti", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Surface(color = TertiaryFixed, shape = RoundedCornerShape(8.dp)) {
                        Text("98% Afinidad", color = OnPrimaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ME", color = PrimaryFixed, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("María Elena (78 años)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Módulo 01: Acompañamiento a Clínica Alemana", fontSize = 11.sp, color = PrimaryContainer, fontWeight = FontWeight.Medium)
                        Text("Viernes 24 Oct • 10:00 AM - 13:00 PM (3 horas)", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pago neto estimado", fontSize = 10.sp, color = OnSurfaceVariant)
                        Text("$38.200 CLP", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    }
                    Button(
                        onClick = onOpenService,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Aceptar Misión", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
