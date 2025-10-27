package com.anthonydevs.intento3.pasteleria.ui.catalog

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.databinding.ItemCatalogoBinding
import com.anthonydevs.intento3.pasteleria.ui.product.ProductoDetalleActivity

class CatalogoAdapter(
    private val listaProductos: MutableList<Producto>,
    private val onAgregarClick: (Producto) -> Unit
) : RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder>() {

    inner class CatalogoViewHolder(private val binding: ItemCatalogoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.tvNombreProducto.text = producto.nombre

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ProductoDetalleActivity::class.java).apply {
                    putExtra("PRODUCTO_ID", producto.id)
                    putExtra("PRODUCTO_NOMBRE", producto.nombre)
                    putExtra("PRODUCTO_DESCRIPCION", producto.descripcion)
                    putExtra("PRODUCTO_PRECIO", producto.precio)
                }
                context.startActivity(intent)
            }

            binding.btnAgregarCarrito.setOnClickListener {
                onAgregarClick(producto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogoViewHolder {
        val binding = ItemCatalogoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CatalogoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogoViewHolder, position: Int) {
        holder.bind(listaProductos[position])
    }

    override fun getItemCount(): Int = listaProductos.size
}
