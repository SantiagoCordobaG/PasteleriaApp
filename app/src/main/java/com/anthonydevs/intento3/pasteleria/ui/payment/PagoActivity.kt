package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anthonydevs.intento3.pasteleria.databinding.ActivityPagoBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding

    private val WHATSAPP_NUMBER = "573238787637" // Formato: código país + número
    
    private var subtotal = 0.0
    private var impuestos = 0.0
    private var gastosEnvio = 5000.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calcularTotales()
        setupClickListeners()
    }

    private fun calcularTotales() {
        // Calcular subtotal del carrito
        subtotal = CatalogoActivity.carritoItems.sumOf { it.precio }
        
        // Calcular impuestos (2% del subtotal)
        impuestos = subtotal * 0.02
        
        // Calcular total
        total = subtotal + impuestos + gastosEnvio

        // Formatear moneda
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        
        binding.tvSubtotal.text = formato.format(subtotal)
        binding.tvImpuestos.text = formato.format(impuestos)
        binding.tvEnvio.text = formato.format(gastosEnvio)
        binding.tvTotal.text = formato.format(total)
        binding.tvTotalFinal.text = formato.format(total)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Permitir seleccionar/deseleccionar el método de pago
        binding.cardNequi.setOnClickListener {
            binding.cbNequi.isChecked = !binding.cbNequi.isChecked
        }

        binding.btnPagar.setOnClickListener {
            procesarPago()
        }
    }

    private fun procesarPago() {
        // Verificar que el método de pago esté seleccionado
        if (!binding.cbNequi.isChecked) {
            Toast.makeText(this, "Por favor selecciona un método de pago", Toast.LENGTH_SHORT).show()
            return
        }

        val metodoPago = "Cuenta Nequi"

        // Crear mensaje detallado del pedido
        val mensaje = construirMensajePedido(metodoPago)

        // Abrir WhatsApp con el mensaje
        abrirWhatsApp(mensaje)
    }

    private fun construirMensajePedido(metodoPago: String): String {
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        val sb = StringBuilder()
        
        sb.append(" *NUEVO PEDIDO - Pastelería Vainilla* \n\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")
        
        sb.append(" *PRODUCTOS:*\n")
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
        sb.append(" *Confirmo mi pedido y procedo al pago*")
        
        return sb.toString()
    }

    private fun abrirWhatsApp(mensaje: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$WHATSAPP_NUMBER&text=${Uri.encode(mensaje)}"
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            
            startActivity(intent)
            
            // Después de enviar a WhatsApp, mostrar pantalla de éxito
            android.os.Handler(mainLooper).postDelayed({
                mostrarPantallaExito()
            }, 2000)
            
        } catch (e: Exception) {
            // Si WhatsApp no está instalado, intentar con WhatsApp Web
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
        // Limpiar el carrito
        CatalogoActivity.carritoItems.clear()
        
        // Ir a pantalla de éxito
        val intent = Intent(this, PagoExitosoActivity::class.java)
        startActivity(intent)
        finish()
    }
}

