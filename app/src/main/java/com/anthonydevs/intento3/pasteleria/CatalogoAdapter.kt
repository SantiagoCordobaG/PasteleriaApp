package com.anthonydevs.intento3.pasteleria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Producto(val nombre: String, val descripcion: String)

class CatalogoAdapter(private val listaProductos: List<Producto>) :
    RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder>() {

    class CatalogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.itemNombre)
        val descripcion: TextView = itemView.findViewById(R.id.itemDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalogo, parent, false)
        return CatalogoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.nombre.text = producto.nombre
        holder.descripcion.text = producto.descripcion
    }

    override fun getItemCount(): Int = listaProductos.size
}
