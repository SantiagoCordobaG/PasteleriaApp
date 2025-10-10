package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CatalogoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogo)

        auth = FirebaseAuth.getInstance()

        val listaProductos = listOf(
            Producto("Pastel de Chocolate", "Delicioso pastel con cobertura de chocolate."),
            Producto("Cheesecake", "Suave pastel de queso con salsa de frutos rojos."),
            Producto("Tarta de Manzana", "Clásica tarta con manzanas caramelizadas."),
            Producto("Brownie", "Brownie húmedo con nueces."),
            Producto("Cupcake Vainilla", "Esponjoso cupcake con crema batida.")
        )

        val recycler = findViewById<RecyclerView>(R.id.recyclerCatalogo)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CatalogoAdapter(listaProductos)

        // 🔒 Botón de cerrar sesión
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            auth.signOut() // Cierra sesión en Firebase
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_cuenta -> true
                else -> false
            }
        }
    }
}
