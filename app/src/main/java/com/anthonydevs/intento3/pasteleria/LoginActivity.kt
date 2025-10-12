package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🔹 Inicializamos vistas
        etEmail = findViewById(R.id.email_edit_text)
        etPassword = findViewById(R.id.password_edit_text)
        btnLogin = findViewById(R.id.login_button)
        tvError = findViewById(R.id.tvError)

        // 🔹 Inicializamos Firebase
        auth = FirebaseAuth.getInstance()

        // 🔹 Botón de inicio de sesión
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CatalogoActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val ex = task.exception
                        val msg = when (ex) {
                            is FirebaseAuthInvalidUserException -> "Usuario no registrado"
                            is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta"
                            else -> "Error: ${ex?.localizedMessage}"
                        }
                        tvError.text = msg
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()

        // ✅ Si el usuario ya está autenticado, lo mandamos directamente al catálogo
        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }
    }
}
