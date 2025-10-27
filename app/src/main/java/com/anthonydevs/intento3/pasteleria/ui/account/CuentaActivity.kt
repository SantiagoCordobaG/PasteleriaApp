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
    private var is2FAEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        checkUserAuthentication()
        displayUserInfo()
        load2FAStatus()
        setupClickListeners()
        setupBottomNavigation()
    }

    private fun checkUserAuthentication() {
        if (auth.currentUser == null) {
            Toast.makeText(this, getString(R.string.error_no_user), Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun displayUserInfo() {
        auth.currentUser?.let { user ->
            binding.tvNombreUsuario.text = getString(
                R.string.user_name_format,
                user.displayName ?: getString(R.string.default_user_name)
            )
            binding.tvCorreoUsuario.text = getString(
                R.string.user_email_format,
                user.email ?: getString(R.string.no_email)
            )
        }
    }

    private fun load2FAStatus() {
        auth.currentUser?.let { user ->
            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        is2FAEnabled = document.getBoolean("twoFactorEnabled") ?: false
                        update2FADisplay()
                    }
                }
        }
    }

    private fun setupClickListeners() {
        binding.btnToggle2FA.setOnClickListener {
            toggle2FA()
        }

        binding.btnCerrarSesion.setOnClickListener {
            logout()
        }
    }

    private fun toggle2FA() {
        is2FAEnabled = !is2FAEnabled
        
        auth.currentUser?.let { user ->
            db.collection("users").document(user.uid)
                .update("twoFactorEnabled", is2FAEnabled)
                .addOnSuccessListener {
                    update2FADisplay()
                    val estado = if (is2FAEnabled) {
                        getString(R.string.twofa_enabled)
                    } else {
                        getString(R.string.twofa_disabled)
                    }
                    Toast.makeText(
                        this,
                        getString(R.string.twofa_toggled, estado),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    is2FAEnabled = !is2FAEnabled
                    Toast.makeText(this, "Error al actualizar 2FA", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun update2FADisplay() {
        val estado = if (is2FAEnabled) {
            getString(R.string.twofa_enabled)
        } else {
            getString(R.string.twofa_disabled)
        }
        
        binding.tvEstado2FA.text = getString(R.string.twofa_status, estado)
        binding.btnToggle2FA.text = if (is2FAEnabled) {
            getString(R.string.disable_twofa)
        } else {
            getString(R.string.enable_twofa)
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

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.selectedItemId = R.id.navigation_cuenta
    }
}
