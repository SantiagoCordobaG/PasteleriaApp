package com.anthonydevs.intento3.pasteleria.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.databinding.ActivityRecuperarContrasenaBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class RecuperarContrasenaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperarContrasenaBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 EXACTO MISMO FIX QUE USASTE EN CatalogoActivity
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.updatePadding(
                top = status.top,     // espacio para barra superior
                bottom = nav.bottom   // espacio para barra inferior
            )

            insets
        }

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEnviar.setOnClickListener {
            enviarEnlaceRecuperacion()
        }
    }

    private fun enviarEnlaceRecuperacion() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.tvMensaje.text = "Por favor ingresa tu correo electrónico"
            binding.tvMensaje.visibility = View.VISIBLE
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                binding.tvMensaje.text = " Correo enviado exitosamente. Revisa tu bandeja de entrada"
                binding.tvMensaje.visibility = View.VISIBLE
                binding.etEmail.text?.clear()
            }
            .addOnFailureListener { e ->
                binding.tvMensaje.text = "Error: ${e.message}"
                binding.tvMensaje.visibility = View.VISIBLE
            }
    }
}

