package com.anthonydevs.intento3.pasteleria.ui.payment

import com.anthonydevs.intento3.pasteleria.data.model.Orden
import com.anthonydevs.intento3.pasteleria.data.model.CarritoItem
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

object OrdenManager {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val COLLECTION_ORDENES = "ordenes"

    /**
     * Guarda una orden en Firestore
     */
    fun guardarOrden(
        orden: Orden,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val ordenData = ordenToMap(orden)
        ordenData["userId"] = userId

        db.collection(COLLECTION_ORDENES)
            .document(orden.id)
            .set(ordenData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Obtiene todas las órdenes del usuario actual desde Firestore
     * Nota: Requiere un índice compuesto en Firestore: userId (Ascending) + fecha (Descending)
     * Firestore creará automáticamente este índice la primera vez que se ejecute esta consulta
     */
    fun obtenerTodasLasOrdenes(
        onSuccess: (List<Orden>) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        db.collection(COLLECTION_ORDENES)
            .whereEqualTo("userId", userId)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val ordenes = mutableListOf<Orden>()
                for (document in documents) {
                    @Suppress("UNCHECKED_CAST")
                    val data = document.data as? Map<String, Any>
                    if (data != null) {
                        val orden = mapToOrden(data, document.id)
                        orden?.let { ordenes.add(it) }
                    }
                }
                onSuccess(ordenes)
            }
            .addOnFailureListener { e ->
                // Si falla por falta de índice, intentar sin orderBy y ordenar en memoria
                if (e.message?.contains("index") == true) {
                    // Fallback: obtener todas las órdenes y ordenar en memoria
                    db.collection(COLLECTION_ORDENES)
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener { documents ->
                            val ordenes = mutableListOf<Orden>()
                            for (document in documents) {
                                @Suppress("UNCHECKED_CAST")
                                val data = document.data as? Map<String, Any>
                                if (data != null) {
                                    val orden = mapToOrden(data, document.id)
                                    orden?.let { ordenes.add(it) }
                                }
                            }
                            // Ordenar por fecha descendente en memoria
                            ordenes.sortByDescending { it.fecha }
                            onSuccess(ordenes)
                        }
                        .addOnFailureListener { e2 ->
                            onFailure(e2)
                        }
                } else {
                    onFailure(e)
                }
            }
    }

    /**
     * Obtiene una orden por ID desde Firestore
     */
    fun getOrdenPorId(
        id: String,
        onSuccess: (Orden?) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        db.collection(COLLECTION_ORDENES)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists() && document.getString("userId") == userId) {
                    @Suppress("UNCHECKED_CAST")
                    val data = document.data as? Map<String, Any>
                    val orden = if (data != null) mapToOrden(data, document.id) else null
                    onSuccess(orden)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Obtiene la última orden del usuario
     * Nota: Requiere un índice compuesto en Firestore: userId (Ascending) + fecha (Descending)
     */
    fun obtenerUltimaOrden(
        onSuccess: (Orden?) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        db.collection(COLLECTION_ORDENES)
            .whereEqualTo("userId", userId)
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onSuccess(null)
                } else {
                    val document = documents.documents[0]
                    @Suppress("UNCHECKED_CAST")
                    val data = document.data as? Map<String, Any>
                    val orden = if (data != null) mapToOrden(data, document.id) else null
                    onSuccess(orden)
                }
            }
            .addOnFailureListener { e ->
                // Si falla por falta de índice, obtener todas y tomar la primera
                if (e.message?.contains("index") == true) {
                    db.collection(COLLECTION_ORDENES)
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                onSuccess(null)
                            } else {
                                val ordenes = mutableListOf<Orden>()
                                for (document in documents) {
                                    @Suppress("UNCHECKED_CAST")
                                    val data = document.data as? Map<String, Any>
                                    if (data != null) {
                                        val orden = mapToOrden(data, document.id)
                                        orden?.let { ordenes.add(it) }
                                    }
                                }
                                // Ordenar por fecha descendente y tomar la primera
                                ordenes.sortByDescending { it.fecha }
                                onSuccess(ordenes.firstOrNull())
                            }
                        }
                        .addOnFailureListener { e2 ->
                            onFailure(e2)
                        }
                } else {
                    onFailure(e)
                }
            }
    }

    /**
     * Crea una nueva orden
     */
    fun crearOrden(
        productos: List<CarritoItem>,
        subtotal: Double,
        impuestos: Double,
        gastosEnvio: Double,
        total: Double,
        metodoPago: String
    ): Orden {
        val ordenId = UUID.randomUUID().toString().take(8).uppercase()
        return Orden(
            id = ordenId,
            fecha = Date(),
            productos = productos.toList(), // Crear copia de la lista
            subtotal = subtotal,
            impuestos = impuestos,
            gastosEnvio = gastosEnvio,
            total = total,
            metodoPago = metodoPago,
            estado = "Pendiente"
        )
    }

    /**
     * Convierte un objeto Orden a Map para Firestore
     */
    private fun ordenToMap(orden: Orden): HashMap<String, Any> {
        val productosMap = orden.productos.map { carritoItem ->
            hashMapOf(
                "producto" to hashMapOf(
                    "id" to carritoItem.producto.id,
                    "nombre" to carritoItem.producto.nombre,
                    "descripcion" to carritoItem.producto.descripcion,
                    "precio" to carritoItem.producto.precio,
                    "imagenUrl" to carritoItem.producto.imagenUrl
                ),
                "cantidad" to carritoItem.cantidad
            )
        }

        return hashMapOf(
            "id" to orden.id,
            "fecha" to com.google.firebase.Timestamp(orden.fecha),
            "productos" to productosMap,
            "subtotal" to orden.subtotal,
            "impuestos" to orden.impuestos,
            "gastosEnvio" to orden.gastosEnvio,
            "total" to orden.total,
            "metodoPago" to orden.metodoPago,
            "estado" to orden.estado
        )
    }

    /**
     * Convierte un Map de Firestore a objeto Orden
     */
    private fun mapToOrden(data: Map<String, Any>, documentId: String): Orden? {
        
        return try {
            val productosList = (data["productos"] as? List<*>)?.mapNotNull { item ->
                val itemMap = item as? Map<*, *>
                if (itemMap == null) return@mapNotNull null
                
                // Convertir a Map<String, Any>
                @Suppress("UNCHECKED_CAST")
                val itemMapString = itemMap as? Map<String, Any> ?: return@mapNotNull null
                
                @Suppress("UNCHECKED_CAST")
                val productoMap = itemMapString["producto"] as? Map<String, Any>
                val cantidad = when (val cant = itemMapString["cantidad"]) {
                    is Long -> cant.toInt()
                    is Int -> cant
                    is Number -> cant.toInt()
                    else -> 1
                }

                productoMap?.let { prod ->
                    val producto = Producto(
                        id = prod["id"] as? String ?: "",
                        nombre = prod["nombre"] as? String ?: "",
                        descripcion = prod["descripcion"] as? String ?: "",
                        precio = when (val precio = prod["precio"]) {
                            is Double -> precio
                            is Number -> precio.toDouble()
                            else -> 0.0
                        },
                        imagenUrl = prod["imagenUrl"] as? String ?: ""
                    )
                    CarritoItem(producto, cantidad)
                }
            } ?: emptyList()

            val fechaTimestamp = data["fecha"] as? com.google.firebase.Timestamp
            val fecha = fechaTimestamp?.toDate() ?: Date()

            Orden(
                id = documentId,
                fecha = fecha,
                productos = productosList,
                subtotal = when (val subt = data["subtotal"]) {
                    is Double -> subt
                    is Number -> subt.toDouble()
                    else -> 0.0
                },
                impuestos = when (val imp = data["impuestos"]) {
                    is Double -> imp
                    is Number -> imp.toDouble()
                    else -> 0.0
                },
                gastosEnvio = when (val envio = data["gastosEnvio"]) {
                    is Double -> envio
                    is Number -> envio.toDouble()
                    else -> 0.0
                },
                total = when (val tot = data["total"]) {
                    is Double -> tot
                    is Number -> tot.toDouble()
                    else -> 0.0
                },
                metodoPago = data["metodoPago"] as? String ?: "",
                estado = data["estado"] as? String ?: "Pendiente"
            )
        } catch (e: Exception) {
            null
        }
    }
}

