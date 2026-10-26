package com.cuidadomayor.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.cuidadomayor.app.data.network.model.BarthelRequest
import com.cuidadomayor.app.ui.home.AdultoMayorViewModel
import com.cuidadomayor.app.ui.home.BarthelUiState
import com.cuidadomayor.app.ui.theme.*

data class BarthelOptionData(val label: String, val points: Int)
data class BarthelQuestionData(val id: String, val title: String, val options: List<BarthelOptionData>)

@Composable
fun BarthelScreen(
    viewModel: AdultoMayorViewModel,
    onBack: () -> Unit,
    onApto: () -> Unit
) {
    var consentAccepted by remember { mutableStateOf(false) }

    var comer by remember { mutableStateOf<Int?>(null) }
    var trasladarse by remember { mutableStateOf<Int?>(null) }
    var aseoPersonal by remember { mutableStateOf<Int?>(null) }
    var usoRetrete by remember { mutableStateOf<Int?>(null) }
    var banarse by remember { mutableStateOf<Int?>(null) }
    var desplazarse by remember { mutableStateOf<Int?>(null) }
    var subirEscaleras by remember { mutableStateOf<Int?>(null) }
    var vestirse by remember { mutableStateOf<Int?>(null) }
    var controlHeces by remember { mutableStateOf<Int?>(null) }
    var controlOrina by remember { mutableStateOf<Int?>(null) }

    val todasContestadas = comer != null && trasladarse != null && aseoPersonal != null && 
                           usoRetrete != null && banarse != null && desplazarse != null && 
                           subirEscaleras != null && vestirse != null && controlHeces != null && controlOrina != null

    val isButtonEnabled = consentAccepted && todasContestadas

    val uiState by viewModel.barthelState.collectInLaunchedEffect()
    val scrollState = rememberScrollState()

    // Solo activamos la redirección 1 VEZ y de forma explícita al aprobar el servidor
    LaunchedEffect(uiState) {
        if (uiState is BarthelUiState.Success) {
            val response = (uiState as BarthelUiState.Success).response
            if (response.apto) {
                onApto()
            }
        }
    }

    // Calcular el total solo para dar un feeling de progreso visual
    val totalProgress = listOfNotNull(comer, trasladarse, aseoPersonal, usoRetrete, banarse, desplazarse, subirEscaleras, vestirse, controlHeces, controlOrina).size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PrimaryGreen)
            }
            Column {
                Text("Paso 1 de 2: Autonomía Funcional", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                Text("Test de Barthel", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
            }
        }

        // Progress indicator
        LinearProgressIndicator(
            progress = { totalProgress / 10f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = PrimaryGreen,
            trackColor = SurfaceContainerHigh
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = if (consentAccepted) PrimaryGreen.copy(alpha = 0.05f) else SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, if (consentAccepted) PrimaryGreen else PrimaryGreen.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { consentAccepted = !consentAccepted }.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PrimaryGreen)
                    Column {
                        Text("Consentimiento Informado", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Tratamiento confidencial de datos de salud", fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                }
                Switch(checked = consentAccepted, onCheckedChange = { consentAccepted = it }, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryGreen))
            }
        }

        if (!consentAccepted) {
            Text("Debes aceptar el consentimiento médico para habilitar el botón y enviar las respuestas.", color = EmergencyRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }

        val questions = listOf(
            BarthelQuestionData("comer", "1. Comer", listOf(BarthelOptionData("Totalmente independiente", 10), BarthelOptionData("Necesita ayuda", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("trasladarse", "2. Trasladarse (Silla/Cama)", listOf(BarthelOptionData("Independiente", 15), BarthelOptionData("Mínima ayuda", 10), BarthelOptionData("Gran ayuda", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("aseo", "3. Aseo personal", listOf(BarthelOptionData("Independiente", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("retrete", "4. Uso del retrete", listOf(BarthelOptionData("Independiente", 10), BarthelOptionData("Necesita ayuda", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("banarse", "5. Bañarse/Ducharse", listOf(BarthelOptionData("Independiente", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("desplazarse", "6. Desplazarse (Deambular)", listOf(BarthelOptionData("Independiente", 15), BarthelOptionData("Necesita ayuda", 10), BarthelOptionData("Silla de ruedas", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("escaleras", "7. Subir/bajar escaleras", listOf(BarthelOptionData("Independiente", 10), BarthelOptionData("Necesita ayuda", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("vestirse", "8. Vestirse/Desvestirse", listOf(BarthelOptionData("Independiente", 10), BarthelOptionData("Necesita ayuda", 5), BarthelOptionData("Dependiente", 0))),
            BarthelQuestionData("heces", "9. Control de heces", listOf(BarthelOptionData("Continente", 10), BarthelOptionData("Accidente ocasional", 5), BarthelOptionData("Incontinente", 0))),
            BarthelQuestionData("orina", "10. Control de orina", listOf(BarthelOptionData("Continente", 10), BarthelOptionData("Accidente ocasional", 5), BarthelOptionData("Incontinente", 0)))
        )

        questions.forEach { q ->
            val currentValue = when (q.id) {
                "comer" -> comer; "trasladarse" -> trasladarse; "aseo" -> aseoPersonal; "retrete" -> usoRetrete; "banarse" -> banarse
                "desplazarse" -> desplazarse; "escaleras" -> subirEscaleras; "vestirse" -> vestirse; "heces" -> controlHeces; else -> controlOrina
            }

            QuestionCardUi(
                title = q.title,
                options = q.options,
                selectedValue = currentValue,
                onSelect = {
                    when (q.id) {
                        "comer" -> comer = it; "trasladarse" -> trasladarse = it; "aseo" -> aseoPersonal = it; "retrete" -> usoRetrete = it; "banarse" -> banarse = it
                        "desplazarse" -> desplazarse = it; "escaleras" -> subirEscaleras = it; "vestirse" -> vestirse = it; "heces" -> controlHeces = it; else -> controlOrina = it
                    }
                }
            )
        }

        AnimatedVisibility(visible = uiState is BarthelUiState.Error) {
            if (uiState is BarthelUiState.Error) {
                val errorMsg = (uiState as BarthelUiState.Error).message
                Card(colors = CardDefaults.cardColors(containerColor = EmergencyContainer), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = EmergencyRed)
                        Text(errorMsg, color = EmergencyRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Si es exitoso pero NO apto (menor a 61), mostramos la advertencia del backend directamente.
        AnimatedVisibility(visible = uiState is BarthelUiState.Success && !(uiState as BarthelUiState.Success).response.apto) {
            Card(colors = CardDefaults.cardColors(containerColor = EmergencyContainer), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = EmergencyRed)
                    Text("Puntaje bajo. Por ahora, el servicio es para adultos con Barthel de 61 o superior.", color = EmergencyRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Button(
            onClick = {
                if (isButtonEnabled && uiState !is BarthelUiState.Loading) {
                    val request = BarthelRequest(
                        comer = comer!!, trasladarse = trasladarse!!, aseoPersonal = aseoPersonal!!, usoRetrete = usoRetrete!!,
                        banarse = banarse!!, desplazarse = desplazarse!!, subirEscaleras = subirEscaleras!!, vestirse = vestirse!!,
                        controlHeces = controlHeces!!, controlOrina = controlOrina!!
                    )
                    viewModel.evaluarBarthel(request)
                }
            },
            enabled = isButtonEnabled && uiState !is BarthelUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGreen,
                disabledContainerColor = SurfaceContainerHigh,
                disabledContentColor = OnSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(54.dp)
        ) {
            if (uiState is BarthelUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(
                    text = if (todasContestadas && consentAccepted) "Evaluar y Continuar" else if (!todasContestadas) "Completa las 10 preguntas" else "Acepta el consentimiento", 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 15.sp
                )
            }
        }
        
        Spacer(Modifier.height(100.dp))
    }
}

@Composable
fun QuestionCardUi(title: String, options: List<BarthelOptionData>, selectedValue: Int?, onSelect: (Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, if (selectedValue == null) SurfaceContainerHigh else PrimaryFixedDim.copy(alpha=0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = OnSurfaceText)
                if (selectedValue != null) {
                    Surface(color = PrimaryGreen.copy(alpha=0.1f), shape = RoundedCornerShape(8.dp)) {
                        Text("+$selectedValue pts", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
            options.forEach { option ->
                val isSelected = selectedValue == option.points
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .border(1.dp, if (isSelected) PrimaryGreen else Color.Transparent, RoundedCornerShape(12.dp))
                        .background(if (isSelected) PrimaryGreen.copy(alpha = 0.05f) else SurfaceBg)
                        .clickable { onSelect(option.points) }.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(option.label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp, color = if (isSelected) PrimaryGreen else OnSurfaceText)
                    RadioButton(selected = isSelected, onClick = { onSelect(option.points) }, colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen))
                }
            }
        }
    }
}
