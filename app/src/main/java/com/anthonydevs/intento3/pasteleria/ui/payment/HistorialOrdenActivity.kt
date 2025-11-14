package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthonydevs.intento3.pasteleria.databinding.ActivityHistorialOrdenBinding
import com.anthonydevs.intento3.pasteleria.data.model.Orden

class HistorialOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialOrdenBinding
    private lateinit var historialAdapter: HistorialOrdenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        cargarOrdenes()
    }

    private fun setupRecyclerView() {
        historialAdapter = HistorialOrdenAdapter(emptyList()) { orden ->
            // Al hacer clic en una orden, abrir detalles de pago
            val intent = Intent(this, DetallesPagoActivity::class.java).apply {
                putExtra("ORDEN_ID", orden.id)
            }
            startActivity(intent)
        }

        binding.recyclerHistorial.apply {
            layoutManager = LinearLayoutManager(this@HistorialOrdenActivity)
            adapter = historialAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun cargarOrdenes() {
        // Mostrar loading (opcional, puedes agregar un ProgressBar si quieres)
        binding.tvHistorialVacio.visibility = View.GONE
        binding.recyclerHistorial.visibility = View.GONE

        OrdenManager.obtenerTodasLasOrdenes(
            onSuccess = { ordenes ->
                historialAdapter.actualizarOrdenes(ordenes)
                actualizarUI(ordenes)
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Error al cargar órdenes: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                actualizarUI(emptyList())
            }
        )
    }

    private fun actualizarUI(ordenes: List<Orden>) {
        if (ordenes.isEmpty()) {
            binding.tvHistorialVacio.visibility = View.VISIBLE
            binding.recyclerHistorial.visibility = View.GONE
        } else {
            binding.tvHistorialVacio.visibility = View.GONE
            binding.recyclerHistorial.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar órdenes cada vez que se resume la actividad
        cargarOrdenes()
    }
}

