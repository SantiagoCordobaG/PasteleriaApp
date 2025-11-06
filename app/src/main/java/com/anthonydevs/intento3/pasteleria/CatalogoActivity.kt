package com.samuel.intento3.pasteleria

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CatalogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        // Datos de ejemplo para el catálogo
        val listaProductos = listOf(
            Producto("Pastel de Chocolate", "Delicioso pastel con cobertura de chocolate."),
            Producto("Cheesecake", "Suave pastel de queso con salsa de frutos rojos."),
            Producto("Tarta de Manzana", "Clásica tarta con manzanas caramelizadas."),
            Producto("Brownie", "Brownie húmedo con nueces."),
            Producto("Cupcake Vainilla", "Esponjoso cupcake con crema batida.")
        )

        // Configurar RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recyclerCatalogo)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CatalogoAdapter(listaProductos)

        // Configurar la barra de navegación inferior
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // TODO: Ir a HomeActivity
                    true
                }

                R.id.navigation_cuenta -> {
                    // TODO: Ir a PerfilActivity
                    true
                }
                else -> false
            }
        }
    }
}
