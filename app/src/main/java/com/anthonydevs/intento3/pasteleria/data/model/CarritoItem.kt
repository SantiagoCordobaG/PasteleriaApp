package com.anthonydevs.intento3.pasteleria.data.model

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
) {
    fun getPrecioTotal(): Double = producto.precio * cantidad
}

