package com.anthonydevs.intento3.pasteleria.ui.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.databinding.ActivityCuentaBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import com.anthonydevs.intento3.pasteleria.ui.cart.CarritoActivity
import com.anthonydevs.intento3.pasteleria.ui.login.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuentaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        // 🔥 Activa edge-to-edge
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 Ajusta automáticamente los paddings para NO cortar el layout
        setupEdgeToEdgeInsets()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        checkUserAuthentication()
        setupClickListeners()
        setupBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        displayUserInfo()
        binding.bottomNavigationView.selectedItemId = R.id.navigation_cuenta
    }

    // ============================================================
    // 🔥 EDGE-TO-EDGE FIX
    // ============================================================
    private fun setupEdgeToEdgeInsets() {
        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Agrega padding top para que NO se tape con la barra
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                systemBars.bottom
            )

            insets
        }
    }

    // ============================================================

    private fun checkUserAuthentication() {
        if (auth.currentUser == null) {
            Toast.makeText(this, getString(R.string.error_no_user), Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun displayUserInfo() {
        auth.currentUser?.let { user ->
            binding.tvNombreUsuario.text = user.displayName ?: "Usuario"
            binding.tvCorreoUsuario.text = user.email ?: "correo@ejemplo.com"

            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    binding.tvDireccion.text =
                        document.getString("direccion") ?: "No especificada"
                }
                .addOnFailureListener {
                    binding.tvDireccion.text = "No especificada"
                }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnEditarPerfil.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener { logout() }
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(this, getString(R.string.session_closed), Toast.LENGTH_SHORT).show()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.selectedItemId = R.id.navigation_cuenta

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
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
