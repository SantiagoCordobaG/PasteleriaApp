package com.anthonydevs.intento3.pasteleria.ui.product

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.databinding.ActivityProductoDetalleBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import com.anthonydevs.intento3.pasteleria.util.ImageHelper
import java.text.NumberFormat
import java.util.Locale

class ProductoDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoDetalleBinding
    private var producto: Producto? = null
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        
        // Cargar imagen según el ID del producto
        val imageResource = ImageHelper.getImageResource(id)
        binding.imgProducto.setImageResource(imageResource)
        
        val formato = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CO").build())
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
                CatalogoActivity.agregarAlCarritoConCantidad(prod, cantidad)
                val textoCantidad = if (cantidad > 1) "$cantidad unidades de" else ""
                Toast.makeText(
                    this, 
                    "$textoCantidad ${prod.nombre} agregado al carrito (Edad: $edad años)", 
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

