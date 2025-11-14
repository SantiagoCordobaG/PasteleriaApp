package com.anthonydevs.intento3.pasteleria.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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

        // 👉 Activa modo edge-to-edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 👉 Ajusta el header según la barra del sistema
        aplicarPaddingParaStatusBar()

        setupRecyclerView()
        setupBottomNavigation()
        setupSearch()
        setupClickListeners()
        loadProducts()
    }

    /** --------------------------------------
     *      🔥 AQUI SE SOLUCIONA TU PROBLEMA
     * -------------------------------------- */
    private fun aplicarPaddingParaStatusBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.header) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                statusBar, // 👉 empuja tu header hacia abajo
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
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
            Producto("1", "Pastel vintage de cereza",
                "Pastel en forma de corazón, personalizable con mensaje.", 40000.0),
            Producto("2", "Pastel de cumpleaños azul",
                "Pastel decorado con crema azul perfecta para celebraciones.", 45000.0),
            Producto("3", "Pastel de Arándano para cumpleaños",
                "Delicioso pastel con arándanos frescos.", 38000.0),
            Producto("4", "Pastel fiesta colorida",
                "Decoraciones vibrantes y múltiples sabores.", 42000.0),
            Producto("5", "Pastel de Chocolate",
                "Clásico irresistible con cobertura de cacao.", 35000.0),
            Producto("6", "Torta de Zanahoria",
                "Torta húmeda con frosting de queso crema.", 37000.0)
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
