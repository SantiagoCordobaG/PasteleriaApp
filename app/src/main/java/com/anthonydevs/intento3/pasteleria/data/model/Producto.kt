package com.anthonydevs.intento3.pasteleria.data.model

data class Producto(
    val id: String = "",
    val nombre: String,
    val descripcion: String,
    val precio: Double = 0.0,
    val imagenUrl: String = ""
)


