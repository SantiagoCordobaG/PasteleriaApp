package com.anthonydevs.intento3.pasteleria.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.anthonydevs.intento3.pasteleria.databinding.ActivityPagoBinding
import com.anthonydevs.intento3.pasteleria.ui.catalog.CatalogoActivity
import java.text.NumberFormat
import java.util.Locale

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding

    private val WHATSAPP_NUMBER = "573022462123" // Formato: código país + número
    
    // Locale para formateo de moneda colombiana
    private val localeCO = Locale.Builder().setLanguage("es").setRegion("CO").build()
    
    private var subtotal = 0.0
    private var impuestos = 0.0
    private var gastosEnvio = 5000.0
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
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

        calcularTotales()
        setupClickListeners()
    }

    private fun calcularTotales() {
        // Calcular subtotal del carrito usando CarritoItem
        subtotal = CatalogoActivity.carritoItems.sumOf { it.getPrecioTotal() }
        
        // Calcular impuestos (2% del subtotal)
        impuestos = subtotal * 0.02
        
        // Calcular total
        total = subtotal + impuestos + gastosEnvio

        // Formatear moneda
        val formato = NumberFormat.getCurrencyInstance(localeCO)
        
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

        // Permitir seleccionar/deseleccionar el método de pago Nequi
        binding.cardNequi.setOnClickListener {
            if (!binding.cbNequi.isChecked) {
                // Seleccionar Nequi y deseleccionar tarjeta de débito
                binding.cbNequi.isChecked = true
                binding.cbTarjetaDebito.isChecked = false
            }
        }

        // Permitir seleccionar/deseleccionar el método de pago Tarjeta de Débito
        binding.cardTarjetaDebito.setOnClickListener {
            if (!binding.cbTarjetaDebito.isChecked) {
                // Seleccionar tarjeta de débito y deseleccionar Nequi
                binding.cbTarjetaDebito.isChecked = true
                binding.cbNequi.isChecked = false
            }
        }

        // Listeners para los CheckBoxes para mantener la selección mutuamente exclusiva
        binding.cbNequi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbTarjetaDebito.isChecked = false
            }
        }

        binding.cbTarjetaDebito.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbNequi.isChecked = false
            }
        }

        binding.btnPagar.setOnClickListener {
            procesarPago()
        }
    }

    private fun procesarPago() {
        // Verificar que algún método de pago esté seleccionado
        val metodoPago = when {
            binding.cbNequi.isChecked -> "Cuenta Nequi"
            binding.cbTarjetaDebito.isChecked -> "Tarjeta de Débito"
            else -> {
                Toast.makeText(this, "Por favor selecciona un método de pago", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Crear mensaje detallado del pedido
        val mensaje = construirMensajePedido(metodoPago)

        // Abrir WhatsApp con el mensaje
        abrirWhatsApp(mensaje)
    }

    private fun construirMensajePedido(metodoPago: String): String {
        val formato = NumberFormat.getCurrencyInstance(localeCO)
        val sb = StringBuilder()
        
        sb.append(" *NUEVO PEDIDO - Pastelería Vainilla* \n\n")
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")
        
        sb.append(" *PRODUCTOS:*\n")
        CatalogoActivity.carritoItems.forEachIndexed { index, carritoItem ->
            val cantidadTexto = if (carritoItem.cantidad > 1) " x${carritoItem.cantidad}" else ""
            sb.append("${index + 1}. ${carritoItem.producto.nombre}$cantidadTexto\n")
            sb.append("   💰 ${formato.format(carritoItem.producto.precio)}")
            if (carritoItem.cantidad > 1) {
                sb.append(" (Total: ${formato.format(carritoItem.getPrecioTotal())})")
            }
            sb.append("\n")
        }
        
        sb.append("\n━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("💵 *RESUMEN DE PAGO:*\n")
        sb.append("Subtotal: ${formato.format(subtotal)}\n")
        sb.append("Impuestos: ${formato.format(impuestos)}\n")
        sb.append("Envío: ${formato.format(gastosEnvio)}\n")
        sb.append("*TOTAL: ${formato.format(total)}*\n\n")
        
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")
        sb.append("💳 *Método de pago:* $metodoPago\n")
        if (metodoPago == "Tarjeta de Débito") {
            sb.append("   (Aceptamos Visa, Mastercard)\n")
        } else if (metodoPago == "Cuenta Nequi") {
            sb.append("   (Número: 3022462123)\n")
        }
        sb.append("\n📦 *Tiempo estimado:* 15-30 minutos\n\n")
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
        // Guardar la orden antes de limpiar el carrito
        val metodoPago = when {
            binding.cbNequi.isChecked -> "Cuenta Nequi"
            binding.cbTarjetaDebito.isChecked -> "Tarjeta de Débito"
            else -> "No especificado"
        }
        
        val orden = OrdenManager.crearOrden(
            productos = CatalogoActivity.carritoItems.toList(),
            subtotal = subtotal,
            impuestos = impuestos,
            gastosEnvio = gastosEnvio,
            total = total,
            metodoPago = metodoPago
        )
        
        // Guardar orden en Firestore
        OrdenManager.guardarOrden(
            orden = orden,
            onSuccess = {
                // Limpiar el carrito después de guardar exitosamente
                CatalogoActivity.carritoItems.clear()
                
                // Ir a pantalla de éxito
                val intent = Intent(this, PagoExitosoActivity::class.java)
                startActivity(intent)
                finish()
            },
            onFailure = { exception ->
                // Mostrar error pero aún así continuar
                Toast.makeText(
                    this,
                    "Orden guardada localmente. Error al guardar en servidor: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
                
                // Limpiar el carrito
                CatalogoActivity.carritoItems.clear()
                
                // Ir a pantalla de éxito
                val intent = Intent(this, PagoExitosoActivity::class.java)
                startActivity(intent)
                finish()
            }
        )
    }
}

