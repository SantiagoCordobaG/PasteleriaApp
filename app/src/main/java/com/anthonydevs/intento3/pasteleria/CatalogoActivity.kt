package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CatalogoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CatalogoAdapter
    private lateinit var etBuscar: EditText
    private lateinit var btnBuscar: Button

    // ✅ Lista general de productos
    private val productos = listOf(
        Producto("Pastel vintage de corazón", "$45.000", R.drawable.imagen_fondo_login),
        Producto("Pastel de cumpleaños azul", "$42.000", R.drawable.imagen_fondo_login),
        Producto("Pastel de fondant", "$50.000", R.drawable.imagen_fondo_login),
        Producto("Pastel fiesta colorido", "$47.000", R.drawable.imagen_fondo_login),
        Producto("Cheesecake", "$38.000", R.drawable.imagen_fondo_login),
        Producto("Cupcake de vainilla", "$15.000", R.drawable.imagen_fondo_login),
        Producto("Tarta de manzana", "$40.000", R.drawable.imagen_fondo_login)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.recyclerCatalogo)
        etBuscar = findViewById(R.id.etBuscarProducto)
        btnBuscar = findViewById(R.id.btnBuscar)

        // ✅ Configurar adaptador y RecyclerView
        adapter = CatalogoAdapter(productos)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // 🔍 Evento de búsqueda
        btnBuscar.setOnClickListener {
            val texto = etBuscar.text.toString().trim()
            adapter.filtrar(texto)
        }

        // 🔽 Barra inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnItemSelectedListener { item ->
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
}
