package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var signUpLink: TextView
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Vinculamos vistas
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        signUpLink = findViewById(R.id.signup_link)
        tvError = findViewById(R.id.tvError)

        // LOGIN con Firebase
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = "Por favor llena todos los campos"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            // ✅ Correo verificado → ir al catálogo
                            startActivity(Intent(this, CatalogoActivity::class.java))
                            finish()
                        } else {
                            // 🚫 Correo no verificado
                            Toast.makeText(
                                this,
                                "Debes verificar tu correo antes de ingresar.",
                                Toast.LENGTH_LONG
                            ).show()
                            auth.signOut()
                        }
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

        // ✅ Enlace al registro
        signUpLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && user.isEmailVerified) {
            // ✅ Usuario verificado → ir al catálogo
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        } else {
            // 🚫 Usuario no logueado o no verificado → cerrar sesión
            FirebaseAuth.getInstance().signOut()
        }
    }
}
