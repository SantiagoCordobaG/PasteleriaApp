package com.anthonydevs.intento3.pasteleria.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.databinding.ActivityMainBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.tvError.text = getString(R.string.error_empty_fields)
                binding.tvError.visibility = View.VISIBLE
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
                        binding.tvError.text = msg
                        binding.tvError.visibility = View.VISIBLE
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if (user != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }
    }
}

