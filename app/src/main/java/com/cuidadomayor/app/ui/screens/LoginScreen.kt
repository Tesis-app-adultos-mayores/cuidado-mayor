package com.cuidadomayor.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cuidadomayor.app.ui.auth.AuthUiState
import com.cuidadomayor.app.ui.auth.AuthViewModel
import com.cuidadomayor.app.ui.theme.*
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel
) {
    var authMode by remember { mutableIntStateOf(0) } // 0: Login, 1: Reg Familia, 2: Reg Acompañante

    // State for Login
    var loginCorreo by remember { mutableStateOf("") }
    var loginClave by remember { mutableStateOf("") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    // State for Reg Familia
    var regFamCorreo by remember { mutableStateOf("") }
    var regFamClave by remember { mutableStateOf("") }
    var regFamNombres by remember { mutableStateOf("") }
    var regFamApellidos by remember { mutableStateOf("") }
    var regFamTipoDoc by remember { mutableStateOf("CC") }
    var regFamTipoDocExpanded by remember { mutableStateOf(false) }
    var regFamNumDoc by remember { mutableStateOf("") }
    var regFamTel by remember { mutableStateOf("") }
    var regFamPasswordVisible by remember { mutableStateOf(false) }

    // State for Reg Acompañante
    var regAcompCorreo by remember { mutableStateOf("") }
    var regAcompClave by remember { mutableStateOf("") }
    var regAcompNombres by remember { mutableStateOf("") }
    var regAcompApellidos by remember { mutableStateOf("") }
    var regAcompTipoDoc by remember { mutableStateOf("CC") }
    var regAcompTipoDocExpanded by remember { mutableStateOf(false) }
    var regAcompNumDoc by remember { mutableStateOf("") }
    var regAcompFechaNac by remember { mutableStateOf("") }
    
    var regAcompGenero by remember { mutableStateOf("Femenino") }
    var regAcompGeneroExpanded by remember { mutableStateOf(false) }
    
    var regAcompTel by remember { mutableStateOf("") }
    
    var regAcompVinculacion by remember { mutableStateOf("Tercero_externo") }
    var regAcompVinculacionExpanded by remember { mutableStateOf(false) }
    
    var regAcompPerfilCorto by remember { mutableStateOf("") }
    var regAcompPasswordVisible by remember { mutableStateOf(false) }

    val uiState by authViewModel.uiState.collectInLaunchedEffect()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceBg)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Hero Logo & Title
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(PrimaryFixed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.HealthAndSafety,
                contentDescription = "Logo",
                tint = PrimaryGreen,
                modifier = Modifier.size(40.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Cuidado Mayor",
                color = PrimaryGreen,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Bienestar y acompañamiento de confianza",
                color = OnSurfaceVariant,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        // Mode Selector Tabs (Styled)
        Surface(
            color = SurfaceContainerHigh,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = authMode,
                containerColor = Color.Transparent,
                contentColor = PrimaryGreen,
                indicator = { },
                divider = { }
            ) {
                TabItem("Ingresar", 0, authMode) { authMode = 0; authViewModel.resetState() }
                TabItem("Familia", 1, authMode) { authMode = 1; authViewModel.resetState() }
                TabItem("Cuidador", 2, authMode) { authMode = 2; authViewModel.resetState() }
            }
        }

        // Error & Success Banners
        AnimatedVisibility(visible = uiState is AuthUiState.Error) {
            if (uiState is AuthUiState.Error) {
                val errorMsg = (uiState as AuthUiState.Error).message
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmergencyContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = EmergencyRed)
                        Text(errorMsg, color = EmergencyRed, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        AnimatedVisibility(visible = uiState is AuthUiState.Success) {
            if (uiState is AuthUiState.Success) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryFixed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircleOutline, contentDescription = null, tint = OnPrimaryFixed)
                        Text("¡Autenticación exitosa! Ingresando...", color = OnPrimaryFixed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // FORMS
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (authMode) {
                    0 -> { // LOGIN FORM
                        Text("Inicia sesión en tu cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)

                        CustomTextField(
                            value = loginCorreo,
                            onValueChange = { loginCorreo = it },
                            label = "Correo Electrónico",
                            icon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email
                        )

                        CustomPasswordField(
                            value = loginClave,
                            onValueChange = { loginClave = it },
                            label = "Contraseña",
                            visible = loginPasswordVisible,
                            onVisibilityChange = { loginPasswordVisible = it }
                        )

                        Button(
                            onClick = { authViewModel.login(loginCorreo, loginClave) },
                            enabled = uiState !is AuthUiState.Loading,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) {
                            if (uiState is AuthUiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }

                    1 -> { // REGISTRO FAMILIA FORM
                        Text("Crea una cuenta familiar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)

                        CustomTextField(value = regFamNombres, onValueChange = { regFamNombres = it }, label = "Nombres", icon = Icons.Default.Person)
                        CustomTextField(value = regFamApellidos, onValueChange = { regFamApellidos = it }, label = "Apellidos", icon = Icons.Default.Badge)
                        CustomTextField(value = regFamCorreo, onValueChange = { regFamCorreo = it }, label = "Correo Electrónico", icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
                        CustomPasswordField(value = regFamClave, onValueChange = { regFamClave = it }, label = "Contraseña", visible = regFamPasswordVisible, onVisibilityChange = { regFamPasswordVisible = it })
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            ExposedDropdownMenuBox(
                                expanded = regFamTipoDocExpanded,
                                onExpandedChange = { regFamTipoDocExpanded = !regFamTipoDocExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = regFamTipoDoc,
                                    onValueChange = {}, readOnly = true, label = { Text("Tipo Doc") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regFamTipoDocExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                                )
                                ExposedDropdownMenu(expanded = regFamTipoDocExpanded, onDismissRequest = { regFamTipoDocExpanded = false }) {
                                    listOf("CC", "NIT", "CE", "Pasaporte").forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = { regFamTipoDoc = selectionOption; regFamTipoDocExpanded = false }
                                        )
                                    }
                                }
                            }
                            CustomTextField(value = regFamNumDoc, onValueChange = { regFamNumDoc = it }, label = "Número Doc", keyboardType = KeyboardType.Number, modifier = Modifier.weight(2f))
                        }
                        CustomTextField(value = regFamTel, onValueChange = { regFamTel = it }, label = "Teléfono Móvil", icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)

                        Button(
                            onClick = {
                                authViewModel.registroFamilia(
                                    correo = regFamCorreo, clave = regFamClave, nombres = regFamNombres, apellidos = regFamApellidos,
                                    tipoDocumento = regFamTipoDoc, numeroDocumento = regFamNumDoc, telefono = regFamTel
                                )
                            },
                            enabled = uiState !is AuthUiState.Loading,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) {
                            if (uiState is AuthUiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("Registrarme", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }

                    2 -> { // REGISTRO ACOMPAÑANTE FORM
                        Text("Postula como Cuidador Pro", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)

                        CustomTextField(value = regAcompNombres, onValueChange = { regAcompNombres = it }, label = "Nombres", icon = Icons.Default.Person)
                        CustomTextField(value = regAcompApellidos, onValueChange = { regAcompApellidos = it }, label = "Apellidos", icon = Icons.Default.Badge)
                        CustomTextField(value = regAcompCorreo, onValueChange = { regAcompCorreo = it }, label = "Correo Electrónico", icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
                        CustomPasswordField(value = regAcompClave, onValueChange = { regAcompClave = it }, label = "Contraseña", visible = regAcompPasswordVisible, onVisibilityChange = { regAcompPasswordVisible = it })
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            ExposedDropdownMenuBox(
                                expanded = regAcompTipoDocExpanded,
                                onExpandedChange = { regAcompTipoDocExpanded = !regAcompTipoDocExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = regAcompTipoDoc,
                                    onValueChange = {}, readOnly = true, label = { Text("Tipo Doc") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regAcompTipoDocExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                                )
                                ExposedDropdownMenu(expanded = regAcompTipoDocExpanded, onDismissRequest = { regAcompTipoDocExpanded = false }) {
                                    listOf("CC", "NIT", "CE", "Pasaporte").forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = { regAcompTipoDoc = selectionOption; regAcompTipoDocExpanded = false }
                                        )
                                    }
                                }
                            }
                            CustomTextField(value = regAcompNumDoc, onValueChange = { regAcompNumDoc = it }, label = "Número Doc", keyboardType = KeyboardType.Number, modifier = Modifier.weight(2f))
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            CustomTextField(value = regAcompFechaNac, onValueChange = { regAcompFechaNac = it }, label = "F. Nac. (AAAA-MM-DD)", icon = Icons.Default.CalendarToday, modifier = Modifier.weight(1f))
                        }
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            ExposedDropdownMenuBox(
                                expanded = regAcompGeneroExpanded,
                                onExpandedChange = { regAcompGeneroExpanded = !regAcompGeneroExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = regAcompGenero.replace("_", " "),
                                    onValueChange = {}, readOnly = true, label = { Text("Género") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regAcompGeneroExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                                )
                                ExposedDropdownMenu(expanded = regAcompGeneroExpanded, onDismissRequest = { regAcompGeneroExpanded = false }) {
                                    listOf("Femenino", "Masculino", "Otro", "No_informa").forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption.replace("_", " ")) },
                                            onClick = { regAcompGenero = selectionOption; regAcompGeneroExpanded = false }
                                        )
                                    }
                                }
                            }
                            
                            ExposedDropdownMenuBox(
                                expanded = regAcompVinculacionExpanded,
                                onExpandedChange = { regAcompVinculacionExpanded = !regAcompVinculacionExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = regAcompVinculacion.replace("_", " "),
                                    onValueChange = {}, readOnly = true, label = { Text("Vinculación") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regAcompVinculacionExpanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen, focusedLabelColor = PrimaryGreen)
                                )
                                ExposedDropdownMenu(expanded = regAcompVinculacionExpanded, onDismissRequest = { regAcompVinculacionExpanded = false }) {
                                    listOf("Empleado_interno", "Tercero_externo", "Independiente").forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption.replace("_", " ")) },
                                            onClick = { regAcompVinculacion = selectionOption; regAcompVinculacionExpanded = false }
                                        )
                                    }
                                }
                            }
                        }
                        
                        CustomTextField(value = regAcompTel, onValueChange = { regAcompTel = it }, label = "Teléfono", icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
                        CustomTextField(value = regAcompPerfilCorto, onValueChange = { regAcompPerfilCorto = it }, label = "Perfil Profesional Corto", icon = Icons.Default.WorkOutline)

                        Button(
                            onClick = {
                                authViewModel.registroAcompanante(
                                    correo = regAcompCorreo, clave = regAcompClave, nombres = regAcompNombres, apellidos = regAcompApellidos,
                                    tipoDocumento = regAcompTipoDoc, numeroDocumento = regAcompNumDoc, fechaNacimiento = regAcompFechaNac,
                                    genero = regAcompGenero.lowercase(), telefono = regAcompTel, vinculacion = regAcompVinculacion.lowercase(),
                                    interesesIds = listOf(1, 4, 14), perfilCorto = regAcompPerfilCorto
                                )
                            },
                            enabled = uiState !is AuthUiState.Loading,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(54.dp)
                        ) {
                            if (uiState is AuthUiState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text("Registrarme", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TabItem(text: String, index: Int, selectedIndex: Int, onClick: () -> Unit) {
    val isSelected = index == selectedIndex
    Tab(
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SurfaceContainerLowest else Color.Transparent)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) PrimaryGreen else OnSurfaceVariant,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = icon?.let { { Icon(it, contentDescription = null, tint = PrimaryGreen) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen,
            cursorColor = PrimaryGreen
        ),
        singleLine = true
    )
}

@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryGreen) },
        trailingIcon = {
            val image = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { onVisibilityChange(!visible) }) {
                Icon(image, contentDescription = "Toggle password", tint = OnSurfaceVariant)
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            focusedLabelColor = PrimaryGreen,
            cursorColor = PrimaryGreen
        ),
        singleLine = true
    )
}

@Composable
fun <T> StateFlow<T>.collectInLaunchedEffect(): State<T> {
    val state = remember { mutableStateOf(this.value) }
    LaunchedEffect(this) {
        this@collectInLaunchedEffect.collect {
            state.value = it
        }
    }
    return state
}
