package com.cuidadomayor.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.cuidadomayor.app.data.model.UserRole
import com.cuidadomayor.app.ui.screens.*
import com.cuidadomayor.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuidadoMayorApp() {
    var currentRole by remember { mutableStateOf(UserRole.FAMILIAR) }
    var currentTab by remember { mutableStateOf("inicio") }
    var activeModal by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(PrimaryGreen)) {
                // Role Switcher Ribbon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rol:",
                        color = PrimaryFixed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        RoleChip(
                            label = "Familiar",
                            selected = currentRole == UserRole.FAMILIAR,
                            onClick = {
                                currentRole = UserRole.FAMILIAR
                                currentTab = "inicio"
                            }
                        )
                        RoleChip(
                            label = "Cuidador Pro",
                            selected = currentRole == UserRole.CUIDADOR,
                            onClick = {
                                currentRole = UserRole.CUIDADOR
                                currentTab = "solicitudes"
                            }
                        )
                        RoleChip(
                            label = "Central 24/7",
                            selected = currentRole == UserRole.OPERADOR,
                            onClick = {
                                currentRole = UserRole.OPERADOR
                                currentTab = "monitoreo"
                            }
                        )
                    }

                    // Direct test launcher
                    AssistChip(
                        onClick = { activeModal = "barthel" },
                        label = { Text("Test Barthel", fontSize = 11.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            labelColor = PrimaryFixed
                        )
                    )
                }

                // Top Bar Title
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Cuidado Mayor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = when (currentRole) {
                                    UserRole.FAMILIAR -> "Acompañamiento Familiar"
                                    UserRole.CUIDADOR -> "Cuidador Pro • Cockpit"
                                    UserRole.OPERADOR -> "Central 24/7 • Monitoreo"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryFixedDim
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryGreen,
                        titleContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = { activeModal = "login" }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Perfil", tint = Color.White)
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = SurfaceBg) {
                when (currentRole) {
                    UserRole.FAMILIAR -> {
                        NavigationBarItem(
                            selected = currentTab == "inicio",
                            onClick = { currentTab = "inicio" },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                            label = { Text("Inicio") }
                        )
                        NavigationBarItem(
                            selected = currentTab == "agendar",
                            onClick = { currentTab = "agendar" },
                            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Agendar") },
                            label = { Text("Agendar") }
                        )
                        NavigationBarItem(
                            selected = currentTab == "en-vivo",
                            onClick = { currentTab = "en-vivo" },
                            icon = { Icon(Icons.Default.LocationOn, contentDescription = "En Vivo") },
                            label = { Text("En Vivo") }
                        )
                    }
                    UserRole.CUIDADOR -> {
                        NavigationBarItem(
                            selected = currentTab == "solicitudes",
                            onClick = { currentTab = "solicitudes" },
                            icon = { Icon(Icons.Default.WorkOutline, contentDescription = "Solicitudes") },
                            label = { Text("Solicitudes") }
                        )
                        NavigationBarItem(
                            selected = currentTab == "en-servicio",
                            onClick = { currentTab = "en-servicio" },
                            icon = { Icon(Icons.Default.Explore, contentDescription = "En Servicio") },
                            label = { Text("En Servicio") }
                        )
                    }
                    UserRole.OPERADOR -> {
                        NavigationBarItem(
                            selected = currentTab == "monitoreo",
                            onClick = { currentTab = "monitoreo" },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Monitoreo") },
                            label = { Text("Monitoreo") }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                activeModal == "barthel" -> BarthelScreen(onBack = { activeModal = null })
                activeModal == "login" -> LoginScreen(onClose = { activeModal = null })
                currentRole == UserRole.FAMILIAR && currentTab == "inicio" -> CatalogoCuidadosScreen(
                    onOpenExpress = { activeModal = "express" },
                    onOpenBarthel = { activeModal = "barthel" },
                    onBookModule = { currentTab = "agendar" }
                )
                currentRole == UserRole.FAMILIAR && currentTab == "agendar" -> AgendarScreen(
                    onBookingSuccess = { currentTab = "en-vivo" }
                )
                currentRole == UserRole.FAMILIAR && currentTab == "en-vivo" -> EnVivoScreen()
                currentRole == UserRole.CUIDADOR && currentTab == "solicitudes" -> SolicitudesScreen(
                    onOpenService = { currentTab = "en-servicio" }
                )
                currentRole == UserRole.CUIDADOR && currentTab == "en-servicio" -> EnServicioScreen()
                currentRole == UserRole.OPERADOR -> MonitoreoScreen()
                else -> CatalogoCuidadosScreen(
                    onOpenExpress = { activeModal = "express" },
                    onOpenBarthel = { activeModal = "barthel" },
                    onBookModule = { currentTab = "agendar" }
                )
            }
        }
    }
}

@Composable
fun RoleChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) PrimaryFixed else Color.Transparent,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) OnPrimaryFixed else Color.White.copy(alpha = 0.8f)
        )
    }
}
