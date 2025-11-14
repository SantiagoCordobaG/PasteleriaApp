package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anthonydevs.intento3.pasteleria.databinding.ActivityPagoBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding

    private val WHATSAPP_NUMBER = "573238787637"

    private var subtotal = 0.0
    private var impuestos = 0.0
    private var gastosEnvio = 5000.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Activa Edge-to-Edge
        enableEdgeToEdge()

        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 Ajusta los insets para evitar que la barra tape el contenido
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Ajuste superior e inferior
            view.setPadding(
                view.paddingLeft,
                bars.top,     // margen debajo del notch / barra de estado
                view.paddingRight,
                bars.bottom   // margen sobre los botones de navegación
            )

            WindowInsetsCompat.CONSUMED
        }

        calcularTotales()
        setupClickListeners()
    }

    private fun calcularTotales() {
        subtotal = CatalogoActivity.carritoItems.sumOf { it.precio }
        impuestos = subtotal * 0.02
        total = subtotal + impuestos + gastosEnvio

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

        binding.tvSubtotal.text = formato.format(subtotal)
        binding.tvImpuestos.text = formato.format(impuestos)
        binding.tvEnvio.text = formato.format(gastosEnvio)
        binding.tvTotal.text = formato.format(total)
        binding.tvTotalFinal.text = formato.format(total)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.cardNequi.setOnClickListener {
            binding.cbNequi.isChecked = !binding.cbNequi.isChecked
        }

        binding.btnPagar.setOnClickListener {
            procesarPago()
        }
    }

    private fun procesarPago() {
        if (!binding.cbNequi.isChecked) {
            Toast.makeText(this, "Por favor selecciona un método de pago", Toast.LENGTH_SHORT).show()
            return
        }

        val metodoPago = "Cuenta Nequi"
        val mensaje = construirMensajePedido(metodoPago)

        abrirWhatsApp(mensaje)
    }

    private fun construirMensajePedido(metodoPago: String): String {
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        val sb = StringBuilder()

        sb.append("🎂 *NUEVO PEDIDO - Pastelería Vainilla* 🎂\n\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")

        sb.append("📋 *PRODUCTOS:*\n")
        CatalogoActivity.carritoItems.forEachIndexed { index, producto ->
            sb.append("${index + 1}. ${producto.nombre}\n")
            sb.append("   💰 ${formato.format(producto.precio)}\n")
        }

        sb.append("\n━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("💵 *RESUMEN DE PAGO:*\n")
        sb.append("Subtotal: ${formato.format(subtotal)}\n")
        sb.append("Impuestos: ${formato.format(impuestos)}\n")
        sb.append("Envío: ${formato.format(gastosEnvio)}\n")
        sb.append("*TOTAL: ${formato.format(total)}*\n\n")

        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("💳 *Método de pago:* $metodoPago\n\n")
        sb.append("📦 *Tiempo estimado:* 15-30 minutos\n\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("✅ *Confirmo mi pedido y procedo al pago*")

        return sb.toString()
    }

    private fun abrirWhatsApp(mensaje: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$WHATSAPP_NUMBER&text=${Uri.encode(mensaje)}"
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")

            startActivity(intent)

            android.os.Handler(mainLooper).postDelayed({
                mostrarPantallaExito()
            }, 2000)

        } catch (e: Exception) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val url = "https://web.whatsapp.com/send?phone=$WHATSAPP_NUMBER&text=${Uri.encode(mensaje)}"
                intent.data = Uri.parse(url)
                startActivity(intent)

                android.os.Handler(mainLooper).postDelayed({
                    mostrarPantallaExito()
                }, 2000)

            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Error: WhatsApp no está instalado. Por favor instala WhatsApp para continuar.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun mostrarPantallaExito() {
        CatalogoActivity.carritoItems.clear()

        val intent = Intent(this, PagoExitosoActivity::class.java)
        startActivity(intent)
        finish()
    }
}
