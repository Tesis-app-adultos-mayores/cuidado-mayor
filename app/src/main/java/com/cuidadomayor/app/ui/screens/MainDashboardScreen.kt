package com.cuidadomayor.app.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cuidadomayor.app.data.model.UserRole
import com.cuidadomayor.app.data.network.model.AdultoMayorResumen
import com.cuidadomayor.app.data.network.model.UsuarioData
import com.cuidadomayor.app.ui.auth.AuthViewModel
import com.cuidadomayor.app.ui.home.AdultoMayorViewModel
import com.cuidadomayor.app.ui.home.HomeUiState
import com.cuidadomayor.app.ui.home.HomeViewModel
import com.cuidadomayor.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDashboardScreen(
    authViewModel: AuthViewModel,
    currentUser: UsuarioData
) {
    var activeModal by remember { mutableStateOf<String?>(null) }
    var selectedModule by remember { mutableStateOf<String?>(null) }
    var currentAdultoId by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val adultoMayorViewModel: AdultoMayorViewModel = viewModel {
        AdultoMayorViewModel(context.applicationContext as Application)
    }
    val homeViewModel: HomeViewModel = viewModel {
        HomeViewModel(context.applicationContext as Application)
    }

    val userRole = when (currentUser.rol.lowercase()) {
        "familia" -> UserRole.FAMILIAR
        "acompanante", "cuidador" -> UserRole.CUIDADOR
        "operador", "admin" -> UserRole.OPERADOR
        else -> UserRole.FAMILIAR
    }

    var currentTab by remember { 
        mutableStateOf(if (userRole == UserRole.FAMILIAR) "inicio" else if (userRole == UserRole.CUIDADOR) "solicitudes" else "monitoreo") 
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(PrimaryGreen)) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Cuidado Mayor",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "${currentUser.correo} • ${currentUser.rol.uppercase()}",
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
                        TextButton(onClick = { authViewModel.logout() }) {
                            Text("Salir", color = PrimaryFixed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(containerColor = SurfaceBg) {
                when (userRole) {
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
                // Flujo de Registro interactivo
                activeModal == "barthel" -> BarthelScreen(
                    viewModel = adultoMayorViewModel,
                    onBack = { activeModal = null },
                    onApto = { activeModal = "registro_adulto" }
                )
                activeModal == "registro_adulto" -> RegistroAdultoScreen(
                    viewModel = adultoMayorViewModel,
                    onSuccessRegistro = { 
                        activeModal = null 
                        homeViewModel.cargarAdultosMayores() 
                    },
                    onCancel = { activeModal = null }
                )

                // Vistas estándar
                userRole == UserRole.FAMILIAR && currentTab == "inicio" -> {
                    HomeFamiliarScreen(
                        homeViewModel = homeViewModel,
                        currentUser = currentUser,
                        onOpenBarthel = { activeModal = "barthel" },
                        onBookModule = { modNum, adultoId ->
                            selectedModule = modNum
                            currentAdultoId = adultoId
                            currentTab = "agendar"
                        }
                    )
                }
                userRole == UserRole.FAMILIAR && currentTab == "agendar" -> {
                    if (selectedModule != null && currentAdultoId != null) {
                        AgendarScreen(
                            adultoMayorId = currentAdultoId!!,
                            selectedModuleNum = selectedModule!!,
                            onBookingSuccess = { currentTab = "en-vivo" }
                        )
                    } else {
                        // Fallback en caso de entrar a la pestaña vacía
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Selecciona un módulo en Inicio para cotizar", color = OnSurfaceVariant)
                        }
                    }
                }
                userRole == UserRole.FAMILIAR && currentTab == "en-vivo" -> EnVivoScreen()
                userRole == UserRole.CUIDADOR && currentTab == "solicitudes" -> SolicitudesScreen(
                    onOpenService = { currentTab = "en-servicio" }
                )
                userRole == UserRole.CUIDADOR && currentTab == "en-servicio" -> EnServicioScreen()
                userRole == UserRole.OPERADOR -> MonitoreoScreen()
            }
        }
    }
}

@Composable
fun HomeFamiliarScreen(
    homeViewModel: HomeViewModel,
    currentUser: UsuarioData,
    onOpenBarthel: () -> Unit,
    onBookModule: (String, String) -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsState()

    // Cargar la data al iniciar la pantalla
    LaunchedEffect(Unit) {
        homeViewModel.cargarAdultosMayores()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            is HomeUiState.Error -> {
                val errorMsg = (uiState as HomeUiState.Error).message
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmergencyContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(errorMsg, color = EmergencyRed, modifier = Modifier.padding(16.dp))
                }
                Button(onClick = { homeViewModel.cargarAdultosMayores() }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) {
                    Text("Reintentar")
                }
            }
            is HomeUiState.Empty -> {
                EmptyAdultosWelcome(userName = currentUser.correo.substringBefore("@"), onAddClick = onOpenBarthel)
            }
            is HomeUiState.HasData -> {
                val lista = (uiState as HomeUiState.HasData).adultosMayores
                val primerAdultoId = lista.first().id // Usamos el primer adulto para la demo

                Text("Mis Familiares", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OnSurfaceText)
                
                lista.forEach { adulto ->
                    AdultoMayorCard(adulto = adulto)
                }

                Button(
                    onClick = onOpenBarthel,
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh, contentColor = PrimaryGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registrar otro familiar", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceContainer)
                
                CatalogoCuidadosView(onBookModule = { mod -> onBookModule(mod, primerAdultoId) })
            }
        }
    }
}

@Composable
fun EmptyAdultosWelcome(userName: String, onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Tarjeta principal emocional
        Card(
            colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¡Hola, $userName!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Sabemos que el bienestar de tus seres queridos es tu mayor prioridad. En Cuidado Mayor, no solo ofrecemos un servicio, brindamos compañía, paciencia y profesionalismo a quienes más lo necesitan.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
                
                Text(
                    text = "Nos especializamos en adultos mayores que requieren desde un paseo al aire libre hasta acompañamiento médico, cuidando cada detalle con el amor que tú les darías.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }

        // Tarjeta de acción (El inicio del viaje)
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PrimaryGreen.copy(alpha = 0.1f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Amor",
                        tint = PrimaryGreen,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Text(
                    text = "El primer paso hacia su tranquilidad",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceText,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Para comenzar, necesitamos conocer un poco sobre la salud y autonomía de tu familiar a través del Índice de Barthel. Es un test médico rápido de 10 preguntas que nos permitirá asignarte al cuidador perfecto para su condición.",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Comenzar evaluación y registro", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun AdultoMayorCard(adulto: AdultoMayorResumen) {
    Card(
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("PLAN FAMILIAR ACTIVO", color = PrimaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Surface(color = PrimaryContainer, shape = RoundedCornerShape(8.dp)) {
                    Text("Barthel: ${adulto.puntajeBarthel}", color = PrimaryFixed, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Text("${adulto.nombres} ${adulto.apellidos} (${adulto.edad} años)", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Estado: ${adulto.estado.uppercase()}", color = OnPrimaryContainer, fontSize = 12.sp)
        }
    }
}

@Composable
fun CatalogoCuidadosView(onBookModule: (String) -> Unit) {
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
                    Text("Cotizar / Agendar Módulo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
