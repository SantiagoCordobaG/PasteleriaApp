package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.databinding.ActivityPagoExitosoBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity

class PagoExitosoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoExitosoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoExitosoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnVolver.setOnClickListener {
            // Volver al catálogo y limpiar el historial
            val intent = Intent(this, CatalogoActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        // Prevenir que vuelva atrás con el botón del sistema
        val intent = Intent(this, CatalogoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}

