package com.cuidadomayor.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuidadomayor.app.ui.theme.*

@Composable
fun CatalogoCuidadosScreen(
    onOpenExpress: () -> Unit,
    onOpenBarthel: () -> Unit,
    onBookModule: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("PLAN FAMILIAR ACTIVO", color = PrimaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("María Elena (78)", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Estado de ánimo: Alegre • 2 sesiones esta semana", color = OnPrimaryContainer, fontSize = 11.sp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Acompañante: Camila Rojas (Hoy 16:30 hrs)", color = Color.White, fontSize = 11.sp)
                    Button(
                        onClick = onOpenBarthel,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Barthel: 75", color = PrimaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Express Urgent Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = SecondaryFixed),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SecondaryTerracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                    }
                    Column {
                        Text("Servicio Express Urgente", color = OnSecondaryFixed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("30-45 min • ¿Imprevisto médico o familiar?", color = OnSurfaceVariant, fontSize = 11.sp)
                    }
                }
                Button(
                    onClick = onOpenExpress,
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryTerracotta),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Solicitar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 6 Care Modules
        Text("Catálogo de Cuidados (6 Módulos)", fontWeight = FontWeight.Bold, fontSize = 14.sp)

        val modules = listOf(
            Triple("Mod. 01", "Acompañamiento Médico y Trámites", "Traslados a consultas, gestión de turnos y reporte a la familia."),
            Triple("Mod. 02", "Compañía en el Hogar y Estimulación", "Lectura pausada, dinámicas de memoria y charla biográfica."),
            Triple("Mod. 03", "Paseos y Movilidad al Aire Libre", "Caminatas seguras en parques y prevención de caídas."),
            Triple("Mod. 04", "Gestión de Compras y Farmacia", "Retiro de fármacos con receta y rendición de boletas."),
            Triple("Mod. 05", "Supervisión de Rutinas y Dosis", "Recordatorio de medicamentos y control de horarios."),
            Triple("Mod. 06", "Eventos Familiares y Religiosos", "Asistencia en misas, bodas o cumpleaños familiares.")
        )

        modules.forEach { (modNum, title, desc) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(modNum, color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("Desde $14.990 /hr", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryGreen)
                    }
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(desc, fontSize = 11.sp, color = OnSurfaceVariant)

                    Button(
                        onClick = { onBookModule(modNum) },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Agendar Módulo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
