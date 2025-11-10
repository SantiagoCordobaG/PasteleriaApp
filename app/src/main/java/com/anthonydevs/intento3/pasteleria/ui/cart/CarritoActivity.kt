package com.anthonydevs.intento3.pasteleria.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.databinding.ActivityCarritoBinding
import com.anthonydevs.intento3.pasteleria.ui.account.CuentaActivity
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var carritoAdapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        updateUI()
    }

    private fun setupRecyclerView() {
        carritoAdapter = CarritoAdapter(CatalogoActivity.carritoItems) { carritoItem ->
            CatalogoActivity.carritoItems.remove(carritoItem)
            carritoAdapter.notifyDataSetChanged()
            updateUI()
        }

        binding.recyclerCarrito.apply {
            layoutManager = LinearLayoutManager(this@CarritoActivity)
            adapter = carritoAdapter
        }
        
        binding.btnPagar.setOnClickListener {
            if (CatalogoActivity.carritoItems.isNotEmpty()) {
                startActivity(Intent(this, com.anthonydevs.intento3.pasteleria.ui.payment.PagoActivity::class.java))
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.selectedItemId = R.id.navigation_carrito

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, CatalogoActivity::class.java))
                    true
                }
                R.id.navigation_carrito -> true
                R.id.navigation_cuenta -> {
                    startActivity(Intent(this, CuentaActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun updateUI() {
        if (CatalogoActivity.carritoItems.isEmpty()) {
            binding.tvCarritoVacio.visibility = View.VISIBLE
            binding.recyclerCarrito.visibility = View.GONE
            binding.tvTotal.visibility = View.GONE
            binding.btnPagar.visibility = View.GONE
        } else {
            binding.tvCarritoVacio.visibility = View.GONE
            binding.recyclerCarrito.visibility = View.VISIBLE
            binding.tvTotal.visibility = View.VISIBLE
            binding.btnPagar.visibility = View.VISIBLE

            val total = CatalogoActivity.carritoItems.sumOf { it.getPrecioTotal() }
            val formato = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CO").build())
            binding.tvTotal.text = "Total: ${formato.format(total)}"
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.navigation_carrito
        carritoAdapter.notifyDataSetChanged()
        updateUI()
    }
}
