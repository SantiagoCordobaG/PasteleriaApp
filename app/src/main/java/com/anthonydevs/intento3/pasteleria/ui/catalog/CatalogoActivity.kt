package com.anthonydevs.intento3.pasteleria.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.data.model.CarritoItem
import com.anthonydevs.intento3.pasteleria.databinding.ActivityCatalogoBinding
import com.anthonydevs.intento3.pasteleria.ui.account.CuentaActivity
import com.anthonydevs.intento3.pasteleria.ui.cart.CarritoActivity
import android.widget.Toast

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var catalogoAdapter: CatalogoAdapter
    private var listaProductos = mutableListOf<Producto>()
    private var listaFiltrada = mutableListOf<Producto>()

    companion object {
        val carritoItems = mutableListOf<CarritoItem>()
        
        fun agregarAlCarritoConCantidad(producto: Producto, cantidad: Int) {
            // Buscar si el producto ya existe en el carrito
            val itemExistente = carritoItems.find { it.producto.id == producto.id }
            
            if (itemExistente != null) {
                // Si existe, incrementar la cantidad
                itemExistente.cantidad += cantidad
            } else {
                // Si no existe, agregar nuevo item
                carritoItems.add(CarritoItem(producto, cantidad))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupSearch()
        setupClickListeners()
        loadProducts()
    }

    private fun setupRecyclerView() {
        catalogoAdapter = CatalogoAdapter(listaFiltrada) { producto ->
            agregarAlCarrito(producto)
        }
        binding.recyclerCatalogo.apply {
            layoutManager = GridLayoutManager(this@CatalogoActivity, 2)
            adapter = catalogoAdapter
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.selectedItemId = R.id.navigation_home

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.navigation_cuenta -> {
                    startActivity(Intent(this, CuentaActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupClickListeners() {
        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, CuentaActivity::class.java))
        }

        binding.cartIcon.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }

    private fun setupSearch() {
        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarProductos(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnBuscar.setOnClickListener {
            filtrarProductos(binding.etBuscarProducto.text.toString())
        }
    }

    private fun filtrarProductos(query: String) {
        listaFiltrada.clear()
        if (query.isEmpty()) {
            listaFiltrada.addAll(listaProductos)
        } else {
            listaProductos.forEach { producto ->
                if (producto.nombre.contains(query, ignoreCase = true)) {
                    listaFiltrada.add(producto)
                }
            }
        }
        catalogoAdapter.notifyDataSetChanged()
    }

    private fun loadProducts() {
        listaProductos = mutableListOf(
            Producto(
                id = "1",
                nombre = "Pastel vintage de cereza",
                descripcion = "Pastel en forma de corazón, personalizable con el mensaje que tú quieras. Hecho con amor, sorprendente y colado. Disponible en chocolate, vainilla y mora con cubito.",
                precio = 40000.0
            ),
            Producto(
                id = "2",
                nombre = "Pastel de cumpleaños azul",
                descripcion = "Hermoso pastel decorado con crema azul perfecta para celebraciones especiales. Sabor personalizable.",
                precio = 45000.0
            ),
            Producto(
                id = "3",
                nombre = "Pastel de Arándano para cumpleaños",
                descripcion = "Delicioso pastel con arándanos frescos y crema suave, ideal para fiestas familiares.",
                precio = 38000.0
            ),
            Producto(
                id = "4",
                nombre = "Pastel fiesta colorida",
                descripcion = "Pastel alegre con decoraciones vibrantes y múltiples sabores para hacer tu fiesta inolvidable.",
                precio = 42000.0
            ),
            Producto(
                id = "5",
                nombre = "Pastel de Chocolate",
                descripcion = "Rico pastel de chocolate con cobertura de cacao y relleno cremoso. Un clásico irresistible.",
                precio = 35000.0
            ),
            Producto(
                id = "6",
                nombre = "Torta de Zanahoria",
                descripcion = "Torta húmeda de zanahoria con frosting de queso crema y nueces. Perfecta para cualquier ocasión.",
                precio = 37000.0
            )
        )
        
        listaFiltrada.clear()
        listaFiltrada.addAll(listaProductos)
        catalogoAdapter.notifyDataSetChanged()
    }

    private fun agregarAlCarrito(producto: Producto) {
        // Buscar si el producto ya existe en el carrito
        val itemExistente = carritoItems.find { it.producto.id == producto.id }
        
        if (itemExistente != null) {
            // Si existe, incrementar la cantidad
            itemExistente.cantidad++
            Toast.makeText(this, "${producto.nombre} agregado al carrito (Cantidad: ${itemExistente.cantidad})", Toast.LENGTH_SHORT).show()
        } else {
            // Si no existe, agregar nuevo item
            carritoItems.add(CarritoItem(producto, 1))
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.navigation_home
    }
}
