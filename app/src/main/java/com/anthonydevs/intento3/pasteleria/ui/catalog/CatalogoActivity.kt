package com.anthonydevs.intento3.pasteleria.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.data.model.Producto
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
        val carritoItems = mutableListOf<Producto>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupSearch()
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
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_cuenta -> {
                    startActivity(Intent(this, CuentaActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
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
                nombre = "Pastel de Chocolate",
                descripcion = "Delicioso pastel con cobertura de chocolate.",
                precio = 25000.0
            ),
            Producto(
                id = "2",
                nombre = "Cheesecake",
                descripcion = "Suave pastel de queso con salsa de frutos rojos.",
                precio = 28000.0
            ),
            Producto(
                id = "3",
                nombre = "Tarta de Manzana",
                descripcion = "Clásica tarta con manzanas caramelizadas.",
                precio = 22000.0
            ),
            Producto(
                id = "4",
                nombre = "Brownie",
                descripcion = "Brownie húmedo con nueces.",
                precio = 15000.0
            ),
            Producto(
                id = "5",
                nombre = "Cupcake Vainilla",
                descripcion = "Esponjoso cupcake con crema batida.",
                precio = 8000.0
            ),
            Producto(
                id = "6",
                nombre = "Torta de Zanahoria",
                descripcion = "Torta húmeda con frosting de queso crema.",
                precio = 26000.0
            )
        )
        
        listaFiltrada.clear()
        listaFiltrada.addAll(listaProductos)
        catalogoAdapter.notifyDataSetChanged()
    }

    private fun agregarAlCarrito(producto: Producto) {
        carritoItems.add(producto)
        Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.navigation_home
    }
}
