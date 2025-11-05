package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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
        setupBackPressedCallback()
    }

    private fun setupClickListeners() {
        binding.btnVolver.setOnClickListener {
            navigateToCatalogo()
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToCatalogo()
            }
        })
    }

    private fun navigateToCatalogo() {
        val intent = Intent(this, CatalogoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
