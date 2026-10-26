package com.cuidadomayor.app.ui.screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cuidadomayor.app.data.network.model.CotizacionRequest
import com.cuidadomayor.app.data.network.model.ParadaData
import com.cuidadomayor.app.data.network.model.UbicacionData
import com.cuidadomayor.app.ui.home.AgendarViewModel
import com.cuidadomayor.app.ui.home.CotizacionUiState
import com.cuidadomayor.app.ui.home.CrearServicioUiState
import com.cuidadomayor.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(
    adultoMayorId: String,
    selectedModuleNum: String, // "Mod. 01"
    onBookingSuccess: () -> Unit
) {
    val context = LocalContext.current
    val agendarViewModel: AgendarViewModel = viewModel {
        AgendarViewModel(context.applicationContext as Application)
    }

    // Step state
    var currentStep by remember { mutableStateOf(1) } // 1: Configurar, 2: Cotizar/Pagar
    
    // UI states from ViewModel
    val cotizacionState by agendarViewModel.cotizacionState.collectAsState()
    val crearServicioState by agendarViewModel.crearServicioState.collectAsState()

    // Configuración state
    var tipoServicio by remember { mutableStateOf("programado") }
    var tipoServicioExpanded by remember { mutableStateOf(false) }
    
    var franjaHoraria by remember { mutableStateOf("media") }
    var franjaExpanded by remember { mutableStateOf(false) }
    
    // Fecha y hora dinámica a 2 días en el futuro por defecto
    var fechaHora by remember { mutableStateOf(java.time.OffsetDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0).toString()) } 
    var direccionDestino by remember { mutableStateOf("Clínica de Prueba, Av. Falsa 123") }

    var wheelchair by remember { mutableStateOf(false) }
    var rampVehicle by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(crearServicioState) {
        if (crearServicioState is CrearServicioUiState.Success) {
            agendarViewModel.resetStates()
            onBookingSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Agendar Servicio: $selectedModuleNum", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)

        if (currentStep == 1) {
            // --- PASO 1: Configuración ---
            Text("Paso 1: Configuración del Servicio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)

            Card(colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Modalidad/Tipo
                    ExposedDropdownMenuBox(expanded = tipoServicioExpanded, onExpandedChange = { tipoServicioExpanded = !tipoServicioExpanded }) {
                        OutlinedTextField(
                            value = tipoServicio.replace("_", " ").uppercase(),
                            onValueChange = {}, readOnly = true, label = { Text("Tipo de Agendamiento") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoServicioExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                        )
                        ExposedDropdownMenu(expanded = tipoServicioExpanded, onDismissRequest = { tipoServicioExpanded = false }) {
                            listOf("programado", "lo_antes_posible").forEach { selectionOption ->
                                DropdownMenuItem(text = { Text(selectionOption.replace("_", " ").uppercase()) }, onClick = { tipoServicio = selectionOption; tipoServicioExpanded = false })
                            }
                        }
                    }

                    // Franja
                    ExposedDropdownMenuBox(expanded = franjaExpanded, onExpandedChange = { franjaExpanded = !franjaExpanded }) {
                        OutlinedTextField(
                            value = franjaHoraria.replace("_", " ").uppercase(),
                            onValueChange = {}, readOnly = true, label = { Text("Jornada (Franja)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = franjaExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                        )
                        ExposedDropdownMenu(expanded = franjaExpanded, onDismissRequest = { franjaExpanded = false }) {
                            listOf("corta", "media", "extendida", "sesion_casa").forEach { selectionOption ->
                                DropdownMenuItem(text = { Text(selectionOption.replace("_", " ").uppercase()) }, onClick = { franjaHoraria = selectionOption; franjaExpanded = false })
                            }
                        }
                    }

                    // FechaHora
                    if (tipoServicio == "programado") {
                        OutlinedTextField(
                            value = fechaHora, onValueChange = { fechaHora = it },
                            label = { Text("Fecha y Hora (ISO 8601)") },
                            modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                        )
                    }

                    // Destino
                    OutlinedTextField(
                        value = direccionDestino, onValueChange = { direccionDestino = it },
                        label = { Text("Dirección de Destino o Parada") },
                        modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                    )
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Complementos Extras", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Silla de ruedas portátil", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Checkbox(checked = wheelchair, onCheckedChange = { wheelchair = it }, colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Vehículo con rampa baja", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Checkbox(checked = rampVehicle, onCheckedChange = { rampVehicle = it }, colors = CheckboxDefaults.colors(checkedColor = PrimaryGreen))
                    }
                }
            }

            Button(
                onClick = {
                    val modId = selectedModuleNum.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 1
                    val complementos = mutableListOf<Int>()
                    if (wheelchair) complementos.add(1)
                    if (rampVehicle) complementos.add(2)

                    val req = CotizacionRequest(
                        adultoMayorId = adultoMayorId,
                        modalidadId = modId,
                        tipo = tipoServicio,
                        franja = franjaHoraria,
                        inicioProgramado = fechaHora,
                        paradas = listOf(ParadaData(direccionDestino, UbicacionData(4.6, -74.0), "Destino Médico")),
                        complementosIds = complementos
                    )
                    agendarViewModel.cotizarServicio(req)
                    currentStep = 2
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Obtener Cotización Real", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(50.dp))
        } 
        else {
            // --- PASO 2: Cotización y Pago ---
            Text("Paso 2: Validación y Aprobación", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
            
            when (val state = cotizacionState) {
                is CotizacionUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
                is CotizacionUiState.Error -> {
                    Card(colors = CardDefaults.cardColors(containerColor = EmergencyContainer), modifier = Modifier.fillMaxWidth()) {
                        Text(state.message, color = EmergencyRed, modifier = Modifier.padding(16.dp))
                    }
                    Button(onClick = { currentStep = 1 }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) {
                        Text("Volver e intentar de nuevo")
                    }
                }
                is CotizacionUiState.Success -> {
                    val cot = state.response
                    
                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("Resumen y Paradas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(cot.aviso, fontSize = 12.sp, color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                            Text("Fin estimado: ${cot.finEstimado}", fontSize = 11.sp, color = OnSurfaceVariant)
                            Text("Distancia: ${cot.distanciaEstimadaM / 1000.0} km", fontSize = 11.sp, color = OnSurfaceVariant)
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Desglose del Costo (${cot.modeloTarifa})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            
                            cot.conceptos.forEach { concepto ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(concepto.concepto, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(concepto.detalle, fontSize = 10.sp, color = OnSurfaceVariant)
                                    }
                                    Text("$${concepto.valorCop}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            
                            HorizontalDivider(color = SurfaceContainer)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Final a Pagar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("$${cot.totalAproximadoCop} COP", fontWeight = FontWeight.Bold, color = SecondaryTerracotta, fontSize = 16.sp)
                            }
                        }
                    }
                    
                    AnimatedVisibility(visible = crearServicioState is CrearServicioUiState.Error) {
                        if (crearServicioState is CrearServicioUiState.Error) {
                            val errorMsg = (crearServicioState as CrearServicioUiState.Error).message
                            Card(colors = CardDefaults.cardColors(containerColor = EmergencyContainer), modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Error, contentDescription = null, tint = EmergencyRed)
                                    Text(errorMsg, color = EmergencyRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { currentStep = 1 }, modifier = Modifier.weight(1f).height(52.dp)) {
                            Text("Modificar", color = PrimaryGreen)
                        }
                        Button(
                            onClick = { agendarViewModel.crearServicio() },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            enabled = crearServicioState !is CrearServicioUiState.Loading,
                            modifier = Modifier.weight(2f).height(52.dp)
                        ) {
                            if (crearServicioState is CrearServicioUiState.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Solicitar Servicio", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(50.dp))
                }
                else -> {}
            }
        }
    }
}
