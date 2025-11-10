package com.anthonydevs.intento3.pasteleria.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anthonydevs.intento3.pasteleria.data.model.Orden
import com.anthonydevs.intento3.pasteleria.databinding.ItemHistorialOrdenBinding
import java.text.NumberFormat
import java.util.Locale

class HistorialOrdenAdapter(
    private var listaOrdenes: List<Orden>,
    private val onOrdenClick: (Orden) -> Unit
) : RecyclerView.Adapter<HistorialOrdenAdapter.HistorialViewHolder>() {

    inner class HistorialViewHolder(private val binding: ItemHistorialOrdenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orden: Orden) {
            val formato = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CO").build())

            binding.tvFechaOrden.text = orden.getFechaFormateada()
            binding.tvIdOrden.text = "Orden #${orden.id.takeLast(5)}"
            binding.tvEstadoOrden.text = orden.estado
            binding.tvCantidadProductos.text = "${orden.productos.size} producto${if (orden.productos.size != 1) "s" else ""}"
            binding.tvTotalOrden.text = formato.format(orden.total)

            binding.root.setOnClickListener {
                onOrdenClick(orden)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val binding = ItemHistorialOrdenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistorialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        holder.bind(listaOrdenes[position])
    }

    override fun getItemCount(): Int = listaOrdenes.size

    fun actualizarOrdenes(nuevasOrdenes: List<Orden>) {
        listaOrdenes = nuevasOrdenes
        notifyDataSetChanged()
    }
}

