package com.anthonydevs.intento3.pasteleria.ui.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    if (document.exists()) {
                        binding.tvDireccion.text = document.getString("direccion") ?: "No especificada"
                    } else {
                        binding.tvDireccion.text = "No especificada"
                    }
                }
                .addOnFailureListener {
                    binding.tvDireccion.text = "No especificada"
                }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEditarPerfil.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            logout()
        }
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
                    AoverridePendingTransition(0, 0)
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
