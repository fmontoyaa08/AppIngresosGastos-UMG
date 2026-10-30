package edu.umg.controlingresosgastos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.umg.programacion2.gastos.modelo.Gasto

class GastoAdapter(
    private var listaGastos: List<Gasto> = emptyList()
) : RecyclerView.Adapter<GastoAdapter.GastoViewHolder>() {

    class GastoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvConcepto: TextView = itemView.findViewById(android.R.id.text1)
        val tvMonto: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return GastoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = listaGastos[position]
        holder.tvConcepto.text = gasto.descripcion
        holder.tvMonto.text = "Q${gasto.monto} - ${gasto.categoria}"
    }

    override fun getItemCount(): Int = listaGastos.size

    fun actualizarGastos(nuevosGastos: List<Gasto>) {
        listaGastos = nuevosGastos
        notifyDataSetChanged()
    }
}