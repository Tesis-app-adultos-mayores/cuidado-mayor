package com.cuidadomayor.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun EnServicioScreen() {
    var step by remember { mutableIntStateOf(4) }
    var waterCount by remember { mutableIntStateOf(2) }
    var moodCount by remember { mutableIntStateOf(1) }
    var medCount by remember { mutableIntStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cockpit Telemetry Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("SERVICIO ACTIVO • #CM-8492", color = PrimaryFixed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Surface(color = PrimaryContainer, shape = RoundedCornerShape(12.dp)) {
                        Text("Seguro", color = OnPrimaryContainer, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryContainer.copy(alpha = 0.7f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("Tiempo transcurrido", color = PrimaryFixedDim, fontSize = 11.sp)
                        Text("01:14:27", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Duración estimada", color = PrimaryFixedDim, fontSize = 11.sp)
                        Text("03:00:00", color = PrimaryFixed, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Senior Quick Dossier
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        Text("María Elena Morales", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("78 años • Pasajera Principal", fontSize = 11.sp, color = OnSurfaceVariant)
                        Text("Bastón ligero", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PrimaryGreen)
                    }
                }

                // High contrast allergy alert
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmergencyContainer.copy(alpha = 0.6f))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed, modifier = Modifier.size(16.dp))
                    Text("ALERGIA: PENICILINA", color = EmergencyRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                // Family Contact Button
                Button(
                    onClick = { /* Call daughter */ },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryGreen)
                            Text("Carolina Morales (Hija)", color = OnSurfaceText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Llamar", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Active Step 4 Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, PrimaryGreen, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "ETAPA 4 EN CURSO (Desde 10:45 AM)",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
                Text(
                    text = "4. En Consulta Médica / Actividad",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Hospital Central • Sala 304 (Dr. Valenzuela - Cardiología)",
                    fontSize = 12.sp,
                    color = OnSurfaceVariant
                )

                Button(
                    onClick = { step = 5 },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Completar Etapa 4 y Avanzar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // Micro Logger
        Text("Bitácora de Bienestar Rápida", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { waterCount++ },
                modifier = Modifier.weight(1f).height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = PrimaryGreen)
                    Text("Agua ($waterCount)", color = OnSurfaceText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = { moodCount++ },
                modifier = Modifier.weight(1f).height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SentimentSatisfied, contentDescription = null, tint = SecondaryContainer)
                    Text("Ánimo ($moodCount)", color = OnSurfaceText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = { medCount++ },
                modifier = Modifier.weight(1f).height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Medication, contentDescription = null, tint = PrimaryGreen)
                    Text("Med ($medCount)", color = OnSurfaceText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // SOS Button
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.White)
                    Column {
                        Text("ASISTENCIA SOS INMEDIATA", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        Text("Conecta con Operador CM en 15 seg", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
                Icon(Icons.Default.Call, contentDescription = null, tint = Color.White)
            }
        }
    }
}
