package com.anthonydevs.intento3.pasteleria.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.databinding.ItemCarritoBinding
import java.text.NumberFormat
import java.util.Locale

class CarritoAdapter(
    private val listaCarrito: MutableList<Producto>,
    private val onEliminarClick: (Producto) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(private val binding: ItemCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.nombreProducto.text = producto.nombre
            
            val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
            binding.precioProducto.text = formato.format(producto.precio)

            binding.btnEliminar.setOnClickListener {
                onEliminarClick(producto)
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
