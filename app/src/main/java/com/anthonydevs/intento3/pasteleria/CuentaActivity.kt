package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CuentaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvCorreoUsuario: TextView
    private lateinit var tvEstado2FA: TextView
    private lateinit var btnToggle2FA: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var bottomNav: BottomNavigationView

    private var is2FAEnabled = false // luego se conectará con Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        auth = FirebaseAuth.getInstance()

        // 🔹 Referencias UI
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario)
        tvEstado2FA = findViewById(R.id.tvEstado2FA)
        btnToggle2FA = findViewById(R.id.btnToggle2FA)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        bottomNav = findViewById(R.id.bottomNavigationView)

        // 🔹 Mostrar info del usuario autenticado
        val user = auth.currentUser
        if (user != null) {
            tvNombreUsuario.text = "Nombre: ${user.displayName ?: "Usuario"}"
            tvCorreoUsuario.text = "Correo: ${user.email ?: "Sin correo"}"
        } else {
            Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 🔹 Botón habilitar/deshabilitar 2FA
        btnToggle2FA.setOnClickListener {
            is2FAEnabled = !is2FAEnabled
            val estado = if (is2FAEnabled) "Habilitado" else "Deshabilitado"
            tvEstado2FA.text = "2FA: $estado"
            btnToggle2FA.text = if (is2FAEnabled) "Deshabilitar 2FA" else "Habilitar 2FA"
            Toast.makeText(this, "Autenticación de dos factores $estado", Toast.LENGTH_SHORT).show()
        }

        // 🔹 Botón cerrar sesión
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

            // Ir correctamente al login (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        // 🔹 Configurar navegación inferior
        bottomNav.selectedItemId = R.id.navigation_cuenta

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_cuenta -> true
                else -> false
            }
        }
    }
}
