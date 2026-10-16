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
fun EnVivoScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Pill
        Surface(
            color = PrimaryFixed.copy(alpha = 0.6f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(PrimaryContainer))
                    Text("Servicio en curso • GPS Activo", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = OnPrimaryFixed)
                }
                Text("ID #CM-8492", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
            }
        }

        // Live GPS Map Visual Node
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(220.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background simulated map
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top ETA Badge
                    Surface(
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = PrimaryGreen)
                            Column {
                                Text("Próximo hito", fontSize = 10.sp, color = OnSurfaceVariant)
                                Text("Llegada en 8 minutos", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                            }
                        }
                    }

                    // Car Node
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = PrimaryGreen,
                            shape = RoundedCornerShape(20.dp),
                            shadowElevation = 6.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Text("En trayecto (42 km/h)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Bottom Destination
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(color = EmergencyRed, shape = RoundedCornerShape(8.dp)) {
                            Text("Clínica Alemana", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }

        // Caregiver Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(26.dp))
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("VR", color = PrimaryFixed, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Valentina Rojas", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("★ 4.9", color = SecondaryTerracotta, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Text("Certificada (480h) • Especialista geriatría", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { /* Call */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Llamar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { /* Chat */ },
                        modifier = Modifier.weight(1f).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryFixed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = OnPrimaryFixed, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Chat en Vivo", color = OnPrimaryFixed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Well-being note
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = SecondaryTerracotta)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Bitácora de bienestar", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Hace 4 min", fontSize = 10.sp, color = OnSurfaceVariant)
                    }
                    Text(
                        "“María Elena se encuentra tranquila y de buen ánimo. Disfrutando de la música durante el trayecto.”",
                        fontSize = 12.sp,
                        color = OnSurfaceText,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text("✓ Reportado por Valentina", fontSize = 11.sp, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Emergency / Cancel
        Button(
            onClick = { /* Support */ },
            colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Icon(Icons.Default.SupportAgent, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Contactar a Operador de Seguridad CM", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
