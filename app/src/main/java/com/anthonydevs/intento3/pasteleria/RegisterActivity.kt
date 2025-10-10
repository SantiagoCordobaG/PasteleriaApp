package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvErrorRegister: TextView
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referencias a vistas
        etEmail = findViewById(R.id.register_email_edit_text)
        etPassword = findViewById(R.id.register_password_edit_text)
        etConfirmPassword = findViewById(R.id.register_confirm_password_edit_text)
        btnRegister = findViewById(R.id.register_button)
        tvErrorRegister = findViewById(R.id.tvErrorRegister)
        loginLink = findViewById(R.id.login_link)

        // Acción al presionar "Registrarse"
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validaciones básicas
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showError("La contraseña debe tener al menos 6 caracteres.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showError("Las contraseñas no coinciden.")
                return@setOnClickListener
            }

            // Crear usuario en Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Se ha enviado un correo de verificación a $email. Verifica tu cuenta antes de iniciar sesión.",
                                    Toast.LENGTH_LONG
                                ).show()

                                // Cerrar sesión para obligar a verificar
                                auth.signOut()

                                // Redirigir al login
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                showError("Error al enviar correo de verificación: ${verifyTask.exception?.localizedMessage}")
                            }
                        }
                    } else {
                        val errorMsg = task.exception?.localizedMessage ?: "Error desconocido"
                        showError("No se pudo registrar: $errorMsg")
                    }
                }
        }

        // Ir al login si ya tiene cuenta
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Mostrar error en el TextView
    private fun showError(message: String) {
        tvErrorRegister.text = message
        tvErrorRegister.visibility = TextView.VISIBLE
    }
}
