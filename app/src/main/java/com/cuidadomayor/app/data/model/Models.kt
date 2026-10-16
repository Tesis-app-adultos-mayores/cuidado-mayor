package com.cuidadomayor.app.data.model

enum class UserRole {
    FAMILIAR, CUIDADOR, OPERADOR
}

data class CareModule(
    val id: String,
    val number: String,
    val title: String,
    val category: String,
    val duration: String,
    val priceHourly: Int,
    val description: String,
    val featureTag: String,
    val imageUrl: String
)

data class SeniorProfile(
    val name: String = "María Elena Morales",
    val age: Int = 78,
    val rut: String = "8.345.120-K",
    val condition: String = "Hipertensión arterial controlada",
    val allergy: String = "PENICILINA",
    val mobility: String = "Bastón ligero",
    val address: String = "Av. Las Tranqueras 1420, Dpto 402, Las Condes",
    val emergencyContact: String = "Carolina Morales (Hija) • +56 9 8765 4321",
    val barthelScore: Int = 75
)

data class BarthelOption(
    val label: String,
    val description: String,
    val points: Int
)

data class BarthelQuestion(
    val id: Int,
    val title: String,
    val options: List<BarthelOption>
)

data class ServiceTrackingState(
    val serviceId: String = "#CM-8492",
    val caregiverName: String = "Valentina Rojas",
    val caregiverRating: Double = 4.9,
    val currentStepIndex: Int = 4,
    val totalSteps: Int = 6,
    val etaMinutes: Int = 8,
    val destination: String = "Clínica Alemana (Consulta Geriatría)",
    val speedKmh: Int = 42,
    val statusText: String = "En trayecto vehicular"
)
