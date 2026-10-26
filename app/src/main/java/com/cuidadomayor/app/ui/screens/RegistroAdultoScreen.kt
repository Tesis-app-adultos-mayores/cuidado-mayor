package com.cuidadomayor.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuidadomayor.app.data.network.model.*
import com.cuidadomayor.app.ui.home.AdultoMayorViewModel
import com.cuidadomayor.app.ui.home.RegistroAdultoUiState
import com.cuidadomayor.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroAdultoScreen(
    viewModel: AdultoMayorViewModel,
    onSuccessRegistro: () -> Unit,
    onCancel: () -> Unit
) {
    val barthelRequest = viewModel.lastBarthelRequest
    if (barthelRequest == null) {
        onCancel()
        return
    }

    // Datos Personales
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var parentesco by remember { mutableStateOf("") }
    var numDoc by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // Selectores Datos Personales
    var tipoDoc by remember { mutableStateOf("CC") }
    var tipoDocExpanded by remember { mutableStateOf(false) }
    
    var genero by remember { mutableStateOf("Femenino") }
    var generoExpanded by remember { mutableStateOf(false) }

    // Dirección
    var direccion by remember { mutableStateOf("") }
    var direccionReferencias by remember { mutableStateOf("") }

    // Ficha Cuidado
    var eps by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("Ninguna") }
    var medicamentos by remember { mutableStateOf("Ninguno") }
    var indicaciones by remember { mutableStateOf("") }
    var movilidad by remember { mutableStateOf("Independiente") }
    
    // Preferencia de Cuidador (Género)
    var prefGeneroAcomp by remember { mutableStateOf("No_informa") }
    var prefGeneroExpanded by remember { mutableStateOf(false) }
    
    // Custom PIN input
    var pinEntrega by remember { mutableStateOf("") }

    // Consentimiento
    var firmadoPorRep by remember { mutableStateOf(false) }
    var incluyeDatosSalud by remember { mutableStateOf(true) }

    val uiState by viewModel.registroState.collectInLaunchedEffect()
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState) {
        if (uiState is RegistroAdultoUiState.Success) {
            viewModel.resetStates()
            onSuccessRegistro()
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
        // Back Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        ) {
            IconButton(onClick = onCancel) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PrimaryGreen)
            }
            Column {
                Text("Paso 2 de 2: Ficha del Adulto Mayor", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                Text("Perfil y Plan de Cuidados", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
            }
        }

        // --- SECCIÓN: Datos Básicos ---
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Datos Personales", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(value = nombres, onValueChange = { nombres = it }, label = "Nombres", modifier = Modifier.weight(1f))
                    CustomTextField(value = apellidos, onValueChange = { apellidos = it }, label = "Apellidos", modifier = Modifier.weight(1f))
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    ExposedDropdownMenuBox(
                        expanded = tipoDocExpanded,
                        onExpandedChange = { tipoDocExpanded = !tipoDocExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = tipoDoc,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoDocExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                        )
                        ExposedDropdownMenu(
                            expanded = tipoDocExpanded,
                            onDismissRequest = { tipoDocExpanded = false }
                        ) {
                            listOf("CC", "NIT", "CE", "Pasaporte").forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        tipoDoc = selectionOption
                                        tipoDocExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    CustomTextField(value = numDoc, onValueChange = { numDoc = it }, label = "No. Documento", keyboardType = KeyboardType.Number, modifier = Modifier.weight(2f))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    CustomTextField(value = fechaNac, onValueChange = { fechaNac = it }, label = "F. Nac (AAAA-MM-DD)", icon = Icons.Default.CalendarToday, modifier = Modifier.weight(1.5f))
                    
                    ExposedDropdownMenuBox(
                        expanded = generoExpanded,
                        onExpandedChange = { generoExpanded = !generoExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = genero,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Género") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = generoExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                        )
                        ExposedDropdownMenu(
                            expanded = generoExpanded,
                            onDismissRequest = { generoExpanded = false }
                        ) {
                            listOf("Femenino", "Masculino", "Otro", "No_informa").forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption.replaceFirstChar { it.uppercase() }) },
                                    onClick = {
                                        genero = selectionOption
                                        generoExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(value = parentesco, onValueChange = { parentesco = it }, label = "Parentesco", modifier = Modifier.weight(1f))
                    CustomTextField(value = telefono, onValueChange = { telefono = it }, label = "Teléfono", keyboardType = KeyboardType.Phone, icon = Icons.Default.Phone, modifier = Modifier.weight(1.5f))
                }
            }
        }

        // --- SECCIÓN: Ubicación ---
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ubicación de Residencia", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                CustomTextField(value = direccion, onValueChange = { direccion = it }, label = "Dirección de Residencia", icon = Icons.Default.LocationOn)
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(120.dp).clickable { /* Aquí iría integración con Google Maps SDK */ }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("📍 Toca para ajustar en el mapa", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                    }
                }

                CustomTextField(value = direccionReferencias, onValueChange = { direccionReferencias = it }, label = "Instrucciones de llegada (Ej. Torre 2)")
            }
        }

        // --- SECCIÓN: Salud y Cuidado ---
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Ficha Médica y Cuidados", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                CustomTextField(value = eps, onValueChange = { eps = it }, label = "EPS o Prepagada", icon = Icons.Default.LocalHospital)
                CustomTextField(value = alergias, onValueChange = { alergias = it }, label = "Alergias Conocidas")
                CustomTextField(value = medicamentos, onValueChange = { medicamentos = it }, label = "Medicamentos Principales")
                CustomTextField(value = indicaciones, onValueChange = { indicaciones = it }, label = "Indicaciones para el cuidador")
                
                // Selector de Preferencia de Cuidador
                ExposedDropdownMenuBox(
                    expanded = prefGeneroExpanded,
                    onExpandedChange = { prefGeneroExpanded = !prefGeneroExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = prefGeneroAcomp.replace("_", " "),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Preferencia Género Cuidador") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prefGeneroExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                    )
                    ExposedDropdownMenu(
                        expanded = prefGeneroExpanded,
                        onDismissRequest = { prefGeneroExpanded = false }
                    ) {
                        listOf("Femenino", "Masculino", "Otro", "No_informa").forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.replace("_", " ")) },
                                onClick = {
                                    prefGeneroAcomp = selectionOption
                                    prefGeneroExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                Column {
                    Text("PIN Seguro de Entrega", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PrimaryGreen)
                    Text("El cuidador deberá digitar este PIN al finalizar el servicio.", fontSize = 11.sp, color = OnSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        for (i in 0 until 4) {
                            val char = pinEntrega.getOrNull(i)?.toString() ?: ""
                            OutlinedTextField(
                                value = char,
                                onValueChange = { 
                                    if (it.length <= 1 && it.all { c -> c.isDigit() }) {
                                        val newPin = pinEntrega.take(i) + it + pinEntrega.drop(i + 1)
                                        pinEntrega = newPin.take(4)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.weight(1f).aspectRatio(0.8f),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen),
                                singleLine = true
                            )
                        }
                    }
                }
            }
        }

        // --- SECCIÓN: Consentimiento ---
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerHigh),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Autorizaciones de Datos y Salud", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Autorizo tratamiento de datos de salud", fontSize = 12.sp)
                    Switch(checked = incluyeDatosSalud, onCheckedChange = { incluyeDatosSalud = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryGreen))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Soy el representante legal / apoderado", fontSize = 12.sp)
                    Switch(checked = firmadoPorRep, onCheckedChange = { firmadoPorRep = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryGreen))
                }
            }
        }

        AnimatedVisibility(visible = uiState is RegistroAdultoUiState.Error) {
            if (uiState is RegistroAdultoUiState.Error) {
                val errorMsg = (uiState as RegistroAdultoUiState.Error).message
                Card(colors = CardDefaults.cardColors(containerColor = EmergencyContainer), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = EmergencyRed)
                        Text(errorMsg, color = EmergencyRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Button(
            onClick = {
                val request = AdultoMayorRequest(
                    parentesco = parentesco, nombres = nombres, apellidos = apellidos, tipoDocumento = tipoDoc,
                    numeroDocumento = numDoc, fechaNacimiento = fechaNac, genero = genero.lowercase(), telefono = telefono,
                    direccion = direccion, direccionReferencias = direccionReferencias.ifBlank { null },
                    epsOPrepagada = eps, tieneDeterioroCognitivo = false, 
                    preferenciaGeneroAcomp = prefGeneroAcomp.lowercase(), // AHORA ES DINÁMICO DESDE EL FORMULARIO
                    temasAEvitar = null, interesesIds = listOf(1, 4), pinEntrega = pinEntrega,
                    ubicacion = UbicacionData(4.7027, -74.0425),
                    contactosEmergencia = listOf(ContactoEmergenciaData("Contacto Principal", telefono, parentesco)),
                    fichaCuidado = FichaCuidadoData(movilidad, listOf(), alergias, medicamentos, indicaciones),
                    consentimiento = ConsentimientoData("1.0", nombres, firmadoPorRep, incluyeDatosSalud, true),
                    barthel = barthelRequest
                )
                viewModel.registrarAdultoMayor(request)
            },
            enabled = uiState !is RegistroAdultoUiState.Loading && nombres.isNotBlank() && numDoc.isNotBlank() && pinEntrega.length == 4,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(54.dp)
        ) {
            if (uiState is RegistroAdultoUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Registrar Adulto Mayor", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
        Spacer(Modifier.height(100.dp))
    }
}
