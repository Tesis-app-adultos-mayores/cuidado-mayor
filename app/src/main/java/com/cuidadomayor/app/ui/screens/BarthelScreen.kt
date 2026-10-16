package com.cuidadomayor.app.ui.screens

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
import com.cuidadomayor.app.data.model.BarthelOption
import com.cuidadomayor.app.data.model.BarthelQuestion
import com.cuidadomayor.app.ui.theme.*

@Composable
fun BarthelScreen(onBack: () -> Unit) {
    var consentAccepted by remember { mutableStateOf(true) }
    var q1Points by remember { mutableIntStateOf(10) }
    var q2Points by remember { mutableIntStateOf(5) }
    var q3Points by remember { mutableIntStateOf(10) }
    var q4Points by remember { mutableIntStateOf(15) }
    var q5Points by remember { mutableIntStateOf(5) }

    val baseline = 30
    val totalScore = baseline + q1Points + q2Points + q3Points + q4Points + q5Points

    val scrollState = rememberScrollState()

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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PrimaryGreen)
            }
            Column {
                Text(
                    text = "Paso 1 de 2: Autonomía Funcional",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Text(
                    text = "Valoración Barthel Pre-registro",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceText
                )
            }
        }

        // Informed Consent Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PrimaryGreen.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryGreen.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PrimaryGreen)
                    }
                    Column {
                        Text("Consentimiento Informado", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            "La información ingresada es confidencial y se utilizará para estructurar un plan de asistencia personalizada.",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Consiento la evaluación funcional",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceText
                    )
                    Switch(
                        checked = consentAccepted,
                        onCheckedChange = { consentAccepted = it }
                    )
                }
            }
        }

        // Questions
        QuestionCard(
            number = 1,
            title = "Alimentación",
            selectedPoints = q1Points,
            options = listOf(
                BarthelOption("Totalmente independiente", "Come por sí solo en tiempo razonable", 10),
                BarthelOption("Necesita ayuda", "Para cortar carne o untar pan", 5)
            ),
            onSelect = { q1Points = it }
        )

        QuestionCard(
            number = 2,
            title = "Lavado y aseo personal",
            selectedPoints = q2Points,
            options = listOf(
                BarthelOption("Totalmente independiente", "Entra, se baña y se seca solo", 10),
                BarthelOption("Requiere ayuda leve", "Supervisión o apoyo en zonas difíciles", 5)
            ),
            onSelect = { q2Points = it }
        )

        QuestionCard(
            number = 3,
            title = "Vestirse y desvestirse",
            selectedPoints = q3Points,
            options = listOf(
                BarthelOption("Totalmente independiente", "Se coloca ropa, botones y calzado", 10),
                BarthelOption("Necesita ayuda parcial", "Apoyo con cordones o cremalleras", 5)
            ),
            onSelect = { q3Points = it }
        )

        QuestionCard(
            number = 4,
            title = "Deambulación / Caminata",
            selectedPoints = q4Points,
            options = listOf(
                BarthelOption("Independiente con bastón", "Camina al menos 50 metros sin ayuda", 15),
                BarthelOption("Silla de ruedas o apoyo", "Requiere empuje continuo o andador", 5)
            ),
            onSelect = { q4Points = it }
        )

        QuestionCard(
            number = 5,
            title = "Uso de escaleras",
            selectedPoints = q5Points,
            options = listOf(
                BarthelOption("Independiente", "Sube y baja sin supervisión", 10),
                BarthelOption("Necesita supervisión", "Requiere compañía o barandilla", 5)
            ),
            onSelect = { q5Points = it }
        )

        // Realtime Score Result Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF4EF)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFF8ABDA9), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("RESULTADO EN TIEMPO REAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                    Surface(
                        color = PrimaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$totalScore / 100 pts",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "$totalScore", color = PrimaryFixed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "$totalScore / 100 - Autonomía Leve/Moderada",
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Apta para acompañamiento, paseos y estimulación.",
                            fontSize = 11.sp,
                            color = OnSurfaceText
                        )
                    }
                }

                Button(
                    onClick = onBack,
                    enabled = consentAccepted,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Continuar con el Registro de Cuenta", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    number: Int,
    title: String,
    selectedPoints: Int,
    options: List<BarthelOption>,
    onSelect: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$number", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Text("+$selectedPoints pts", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }

            options.forEach { option ->
                val isSelected = selectedPoints == option.points
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) PrimaryGreen else SurfaceContainerHigh,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(if (isSelected) PrimaryGreen.copy(alpha = 0.05f) else SurfaceBg)
                        .clickable { onSelect(option.points) }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(option.label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(option.description, fontSize = 11.sp, color = OnSurfaceVariant)
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(option.points) },
                        colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen)
                    )
                }
            }
        }
    }
}
