package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.anthonydevs.intento3.pasteleria.databinding.ActivityPagoExitosoBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity

class PagoExitosoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoExitosoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoExitosoBinding.inflate(layoutInflater)
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
