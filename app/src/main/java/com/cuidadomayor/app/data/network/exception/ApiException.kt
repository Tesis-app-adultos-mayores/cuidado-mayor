package com.cuidadomayor.app.data.network.exception

class ApiException(
    val httpCode: Int,
    val error: String,
    val detalle: String
) : Exception("API Error [$httpCode] $error: $detalle")
