package com.anthonydevs.intento3.pasteleria.util

import com.anthonydevs.intento3.pasteleria.R

object ImageHelper {
    /**
     * Obtiene el recurso de imagen según el ID del producto
     * 
     * INSTRUCCIONES PARA AGREGAR IMÁGENES:
     * 1. Coloca las imágenes en: app/src/main/res/drawable/
     * 2. Nombres de archivos DEBEN ser exactamente como se muestran abajo (sin espacios, usar guiones bajos)
     * 3. Formatos soportados: .png, .jpg, .jpeg, .webp
     * 4. Si la imagen no existe, se usará la imagen por defecto (imagen_fondo_login)
     * 
     * NOMBRES DE ARCHIVOS NECESARIOS:
     * - pastel_cereza.png (o .jpg/.jpeg/.webp) para ID "1"
     * - pastel_azul.png para ID "2"
     * - pastel_arandano.png para ID "3"
     * - pastel_colorida.png para ID "4"
     * - pastel_chocolate.png para ID "5"
     * - pastel_zanahoria.png para ID "6"
     */
    fun getImageResource(productId: String): Int {
        return when (productId) {
            "1" -> tryGetDrawable("pastel_cereza") // Pastel vintage de cereza
            "2" -> tryGetDrawable("pastel_azul") // Pastel de cumpleaños azul
            "3" -> tryGetDrawable("pastel_arandano") // Pastel de Arándano
            "4" -> tryGetDrawable("pastel_colorida") // Pastel fiesta colorida
            "5" -> tryGetDrawable("pastel_chocolate") // Pastel de Chocolate
            "6" -> tryGetDrawable("pastel_zanahoria") // Torta de Zanahoria
            else -> R.drawable.imagen_fondo_login // Imagen por defecto
        }
    }
    
    /**
     * Intenta obtener un recurso drawable por nombre usando reflexión
     * Si no existe, retorna la imagen por defecto
     */
    private fun tryGetDrawable(name: String): Int {
        return try {
            val field = R.drawable::class.java.getField(name)
            field.getInt(null)
        } catch (e: Exception) {
            // Si el recurso no existe, usar imagen por defecto
            R.drawable.imagen_fondo_login
        }
    }
}

