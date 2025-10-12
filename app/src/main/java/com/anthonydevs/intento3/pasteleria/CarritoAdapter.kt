package com.anthonydevs.intento3.pasteleria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarritoAdapter(
    private val listaCarrito: MutableList<Producto>,
    private val onEliminarClick: (Producto) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenProducto: ImageView = itemView.findViewById(R.id.imagenProducto)
        val nombreProducto: TextView = itemView.findViewById(R.id.nombreProducto)
        val precioProducto: TextView = itemView.findViewById(R.id.precioProducto)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val producto = listaCarrito[position]
        holder.nombreProducto.text = producto.nombre
        holder.precioProducto.text = producto.precio

        // Acción al presionar "Eliminar"
        holder.btnEliminar.setOnClickListener {
            onEliminarClick(producto)
        }
    }

    override fun getItemCount(): Int = listaCarrito.size
}
