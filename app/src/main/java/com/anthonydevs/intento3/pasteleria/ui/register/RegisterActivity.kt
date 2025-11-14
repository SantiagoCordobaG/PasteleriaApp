package com.anthonydevs.intento3.pasteleria.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.databinding.ActivityRegisterBinding
import com.anthonydevs.intento3.pasteleria.ui.login.MainActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // ⬅️ Activa edge-to-edge
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ⬅️ Ajusta los paddings para que no estorben los botones del celular
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                view.paddingLeft,
                systemBars.top,      // status bar
                view.paddingRight,
                systemBars.bottom    // navigation bar
            )
            WindowInsetsCompat.CONSUMED
        }

        auth = FirebaseAuth.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister() {
        val nombre = binding.registerEmailEditText.text.toString().trim()
        val email = binding.registerCorreo.text.toString().trim()
        val edad = binding.registerEdad.text.toString().trim()
        val password = binding.registerPasswordEditText.text.toString().trim()

        if (!validateInputs(nombre, email, edad, password)) return

        registerUser(email, password)
    }

    private fun validateInputs(nombre: String, email: String, edad: String, password: String): Boolean {
        return when {
            nombre.isEmpty() || email.isEmpty() || edad.isEmpty() || password.isEmpty() -> {
                showError(getString(R.string.error_empty_fields))
                false
            }
            password.length < 6 -> {
                showError(getString(R.string.error_password_length))
                false
            }
            else -> true
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerificationEmail(email)
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: getString(R.string.error_unknown)
                    showError(getString(R.string.error_register_failed, errorMsg))
                }
            }
    }

    private fun sendVerificationEmail(email: String) {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
            if (verifyTask.isSuccessful) {
                Toast.makeText(
                    this,
                    getString(R.string.success_verification_email_sent, email),
                    Toast.LENGTH_LONG
                ).show()
                auth.signOut()
                navigateToLogin()
            } else {
                showError(getString(R.string.error_verification_email))
            }
        }
    }

    private fun showError(message: String) {
        binding.tvErrorRegister.text = message
        binding.tvErrorRegister.visibility = View.VISIBLE
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
