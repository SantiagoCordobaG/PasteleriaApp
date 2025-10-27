package com.anthonydevs.intento3.pasteleria.ui.product

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.databinding.ActivityProductoDetalleBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class ProductoDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductoDetalleBinding
    private var producto: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadProductData()
        setupClickListeners()
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
            producto?.let { prod ->
                CatalogoActivity.carritoItems.add(prod)
                Toast.makeText(this, "${prod.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnSearch.setOnClickListener {
            finish()
        }
    }
}

