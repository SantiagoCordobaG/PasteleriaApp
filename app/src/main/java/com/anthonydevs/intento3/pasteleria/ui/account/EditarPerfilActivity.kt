package com.anthonydevs.intento3.pasteleria.ui.account

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anthonydevs.intento3.pasteleria.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {

        // 🔥 Activa edge-to-edge antes de setContentView
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 Ajuste de paddings contra status bar y nav bar
        setupEdgeToEdgeInsets()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        loadUserData()
        setupClickListeners()
    }

    // ============================================================
    // EDGE TO EDGE FIX 🔥
    // ============================================================
    private fun setupEdgeToEdgeInsets() {
        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                view.paddingLeft,
                systemBars.top,      // evita que se tape con la barra superior
                view.paddingRight,
                systemBars.bottom    // evita que se tape con la nav bar
            )

            insets
        }
    }
    // ============================================================

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            binding.etNombre.setText(user.displayName ?: "")
            binding.etCorreo.setText(user.email ?: "")

            db.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        binding.etDireccion.setText(document.getString("direccion") ?: "")
                        binding.etTelefono.setText(document.getString("telefono") ?: "")
                    }
                }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun guardarCambios() {
        val nombre = binding.etNombre.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()

        val user = auth.currentUser
        if (user != null) {
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build()

            user.updateProfile(profileUpdates)
                .addOnSuccessListener {
                    val userData = hashMapOf(
                        "nombre" to nombre,
                        "email" to user.email,
                        "direccion" to direccion,
                        "telefono" to telefono,
                        "uid" to user.uid
                    )

                    db.collection("users").document(user.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar en Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
