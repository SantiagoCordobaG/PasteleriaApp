package com.anthonydevs.intento3.pasteleria.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.databinding.ActivityProductoDetalleBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class ProductoDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoDetalleBinding
    private var producto: Producto? = null
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ Habilitar edge-to-edge
        enableEdgeToEdge()

        binding = ActivityProductoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⭐ Aplicar insets para evitar que las barras del sistema tapen contenido
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        loadProductData()
        setupClickListeners()
        setupCantidadControls()
    }

    private fun loadProductData() {
        val nombre = intent.getStringExtra("PRODUCTO_NOMBRE") ?: ""
        val descripcion = intent.getStringExtra("PRODUCTO_DESCRIPCION") ?: ""
        val precio = intent.getDoubleExtra("PRODUCTO_PRECIO", 0.0)
        val id = intent.getStringExtra("PRODUCTO_ID") ?: ""

        producto = Producto(id, nombre, descripcion, precio)

        binding.tvNombreProducto.text = nombre
        binding.tvDescripcion.text = descripcion

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        binding.tvPrecio.text = formato.format(precio)

        val rating = 4.7 + (Math.random() * 0.3)
        binding.tvRating.text = String.format("%.1f", rating)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAgregarCarrito.setOnClickListener {
            val edad = binding.etEdad.text.toString()

            if (edad.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa la edad del cumpleañero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            producto?.let { prod ->
                repeat(cantidad) {
                    CatalogoActivity.carritoItems.add(prod)
                }
                Toast.makeText(
                    this,
                    "$cantidad ${prod.nombre} agregado al carrito (Edad: $edad años)",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

        binding.btnSearch.setOnClickListener {
            finish()
        }
    }

    private fun setupCantidadControls() {
        binding.tvCantidad.text = cantidad.toString()

        binding.btnAumentar.setOnClickListener {
            if (cantidad < 99) {
                cantidad++
                binding.tvCantidad.text = cantidad.toString()
            }
        }

        binding.btnDisminuir.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                binding.tvCantidad.text = cantidad.toString()
            }
        }
    }
}
