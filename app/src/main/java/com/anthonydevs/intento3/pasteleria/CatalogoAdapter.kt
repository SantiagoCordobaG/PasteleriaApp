package com.anthonydevs.intento3.pasteleria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CatalogoAdapter(private var listaProductos: List<Producto>) :
    RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder>() {

    inner class CatalogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        val btnAgregar: Button = itemView.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalogo, parent, false)
        return CatalogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.imgProducto.setImageResource(producto.imagenResId)
        holder.tvNombre.text = producto.nombre
        holder.tvPrecio.text = producto.precio

        holder.btnAgregar.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = listaProductos.size

    fun filtrar(texto: String) {
        val listaFiltrada = if (texto.isEmpty()) {
            listaProductos
        } else {
            listaProductos.filter { it.nombre.contains(texto, ignoreCase = true) }
        }
        this.listaProductos = listaFiltrada
        notifyDataSetChanged()
    }
}
