# Cuidado Mayor - Aplicación Android (Jetpack Compose)

**Cuidado Mayor** es una plataforma nativa de Android desarrollada en Kotlin y Jetpack Compose para la gestión y coordinación del cuidado de personas mayores.

## 📱 Módulos y Roles

La aplicación incluye tres experiencias según el rol del usuario:

1. **Familiar**
   - Catálogo de cuidados de 6 módulos (Acompañamiento médico, Estimulación, Paseos, Gestión de compras, Medicación, Eventos).
   - Test de Autonomía Funcional Barthel.
   - Agendamiento con desglose transparente de costos y adicionales de movilidad (vehículo con rampa, silla de ruedas).
   - Seguimiento En Vivo con telemetría GPS, contacto con cuidador y bitácora de bienestar.

2. **Cuidador Pro**
   - Radar de solicitudes en tiempo real con % de afinidad.
   - Cockpit de servicio activo con temporizador, expediente del paciente, alertas médicas (alergias) e integración SOS.
   - Bitácora rápida de bienestar (agua, ánimo, medicación).

3. **Central 24/7 (Operador)**
   - Panel de monitoreo de emergencias SOS.
   - Despacho de transporte adaptado y móviles de respaldo.
   - Aprobación y validación de personal de salud.

## 🛠️ Requisitos e Instalación

- **Android Studio**: Ladybug / Hedgehog o superior.
- **JDK**: 17 o superior.
- **SDK Mínimo**: Android 8.0 (API level 26).
- **SDK Objetivo**: Android 15 (API level 35).

Para ejecutar la aplicación:
1. Abre el proyecto en **Android Studio**.
2. Sincroniza el proyecto con los archivos de Gradle (`Sync Project with Gradle Files`).
3. Ejecuta la app en un emulador o dispositivo físico Android con `MainActivity`.
