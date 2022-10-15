package com.example.musicalevents.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import java.lang.String
import kotlin.Int

class EventoAdapter(list: ArrayList<Event>) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {
    private val list: ArrayList<Event>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNombreEvento.setText(list[position].nombreEvento)
        holder.tvUbicacionEvento.setText(String.valueOf(list[position].ubicacion))
        holder.tvHoraEvento.setText(list[position].horaComienzo)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(list: List<Event>?) {
        this.list.clear()
        this.list.addAll(list!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNombreEvento: TextView
        var tvUbicacionEvento: TextView
        var tvHoraEvento: TextView

        init {
            tvNombreEvento = itemView.findViewById(R.id.tvNombreEvento)
            tvUbicacionEvento = itemView.findViewById(R.id.tvUbicacionEvento)
            tvHoraEvento = itemView.findViewById(R.id.tvHoraEvento)
        }
    }

    init {
        this.list = list
    }
}
