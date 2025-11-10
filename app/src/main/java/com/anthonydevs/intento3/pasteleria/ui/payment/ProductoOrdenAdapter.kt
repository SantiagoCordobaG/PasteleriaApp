package com.anthonydevs.intento3.pasteleria.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anthonydevs.intento3.pasteleria.data.model.CarritoItem
import com.anthonydevs.intento3.pasteleria.databinding.ItemProductoOrdenBinding
import java.text.NumberFormat
import java.util.Locale

class ProductoOrdenAdapter(
    private val productos: List<CarritoItem>
) : RecyclerView.Adapter<ProductoOrdenAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(private val binding: ItemProductoOrdenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(carritoItem: CarritoItem) {
            val formato = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CO").build())

            binding.tvNombreProducto.text = carritoItem.producto.nombre
            binding.tvCantidadProducto.text = if (carritoItem.cantidad > 1) "x${carritoItem.cantidad}" else ""
            binding.tvPrecioProducto.text = formato.format(carritoItem.getPrecioTotal())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoOrdenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    override fun getItemCount(): Int = productos.size
}

