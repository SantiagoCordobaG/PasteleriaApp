package com.anthonydevs.intento3.pasteleria.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthonydevs.intento3.pasteleria.databinding.ActivityDetallesPagoBinding
import com.anthonydevs.intento3.pasteleria.data.model.Orden
import java.text.NumberFormat
import java.util.Locale

class DetallesPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallesPagoBinding
    private var orden: Orden? = null

    // Locale para formateo de moneda colombiana
    private val localeCO = Locale.Builder().setLanguage("es").setRegion("CO").build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 EXACTO MISMO FIX QUE USASTE EN CatalogoActivity
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.updatePadding(
                top = status.top,     // espacio para barra superior
                bottom = nav.bottom   // espacio para barra inferior
            )

            insets
        }

        setupClickListeners()
        
        // Obtener la orden pasada como extra y cargar desde Firestore
        val ordenId = intent.getStringExtra("ORDEN_ID")
        if (ordenId != null && ordenId.isNotEmpty()) {
            cargarOrden(ordenId)
        } else {
            Toast.makeText(this, "Error: ID de orden no válido.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun cargarOrden(ordenId: String) {
        OrdenManager.getOrdenPorId(
            id = ordenId,
            onSuccess = { ordenCargada ->
                if (ordenCargada != null) {
                    orden = ordenCargada
                    setupRecyclerView()
                    mostrarDetalles()
                } else {
                    Toast.makeText(this, "Orden no encontrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Error al cargar orden: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        )
    }

    private fun setupRecyclerView() {
        orden?.let { orden ->
            val adapter = ProductoOrdenAdapter(orden.productos)
            binding.recyclerProductos.apply {
                layoutManager = LinearLayoutManager(this@DetallesPagoActivity)
                this.adapter = adapter
            }
        }
    }

    private fun mostrarDetalles() {
        orden?.let { orden ->
            val formato = NumberFormat.getCurrencyInstance(localeCO)

            binding.tvMetodoPago.text = orden.metodoPago
            binding.tvFecha.text = orden.getFechaFormateada()
            binding.tvEstado.text = orden.estado
            binding.tvSubtotal.text = formato.format(orden.subtotal)
            binding.tvImpuestos.text = formato.format(orden.impuestos)
            binding.tvEnvio.text = formato.format(orden.gastosEnvio)
            binding.tvTotal.text = formato.format(orden.total)
        }
    }
}

