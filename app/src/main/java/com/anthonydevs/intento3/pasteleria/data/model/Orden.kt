package com.anthonydevs.intento3.pasteleria.data.model

import java.util.Date

data class Orden(
    val id: String,
    val fecha: Date,
    val productos: List<CarritoItem>,
    val subtotal: Double,
    val impuestos: Double,
    val gastosEnvio: Double,
    val total: Double,
    val metodoPago: String,
    val estado: String = "Pendiente" // Pendiente, En preparación, En camino, Entregado
) {
    fun getFechaFormateada(): String {
        val locale = java.util.Locale.Builder().setLanguage("es").setRegion("CO").build()
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
        return formato.format(fecha)
    }
}

