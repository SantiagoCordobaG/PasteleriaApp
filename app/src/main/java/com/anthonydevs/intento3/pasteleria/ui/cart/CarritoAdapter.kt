package com.anthonydevs.intento3.pasteleria.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anthonydevs.intento3.pasteleria.data.model.CarritoItem
import com.anthonydevs.intento3.pasteleria.databinding.ItemCarritoBinding
import com.anthonydevs.intento3.pasteleria.util.ImageHelper
import java.text.NumberFormat
import java.util.Locale

class CarritoAdapter(
    private val listaCarrito: MutableList<CarritoItem>,
    private val onEliminarClick: (CarritoItem) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(carritoItem: CarritoItem) {
            binding.nombreProducto.text = carritoItem.producto.nombre
            
            val formato = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CO").build())
            // Mostrar precio unitario
            binding.precioProducto.text = formato.format(carritoItem.producto.precio)
            
            // Mostrar cantidad
            binding.cantidadProducto.text = "Cantidad: ${carritoItem.cantidad}"
            
            // Cargar imagen según el ID del producto
            val imageResource = ImageHelper.getImageResource(carritoItem.producto.id)
            binding.imagenProducto.setImageResource(imageResource)

            binding.btnEliminar.setOnClickListener {
                onEliminarClick(carritoItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(listaCarrito[position])
    }

    override fun getItemCount(): Int = listaCarrito.size
}
