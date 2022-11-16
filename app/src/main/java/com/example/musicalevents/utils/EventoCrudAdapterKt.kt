package com.example.musicalevents.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event
import java.util.*

class EventoCrudAdapterKt(eventos: ArrayList<Event>, listener: OnManageEventoListener) :
    RecyclerView.Adapter<EventoCrudAdapterKt.ViewHolder>() {
    var eventos: MutableList<Event>
    var listener: OnManageEventoListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        UtilsKt.onBindViewHolder(holder = holder, position = position, eventos = eventos, listener = listener)
    }

    override fun getItemCount(): Int {
        return eventos.size
    }

    fun update(l: List<Event>?) {
        eventos.clear()
        eventos.addAll(l!!)
        notifyDataSetChanged()
    }

    fun removeItem(item: Event): Boolean {
        val position = eventos.indexOf(item)
        if (position == -1) return false
        eventos.remove(item)
        notifyItemRemoved(position)
        return true
    }

    interface OnManageEventoListener {
        fun onEditEvento(l: Event)
        fun onDeleteEvento(l: Event)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView
        var ubicacion: TextView
        var hora: TextView
        fun bind(l: Event, listener: OnManageEventoListener) {
            itemView.setOnClickListener { view: View? ->
                listener.onEditEvento(
                    l
                )
            }
            itemView.setOnLongClickListener { view: View? ->
                listener.onDeleteEvento(l)
                true
            }
        }

        init {
            nombre = itemView.findViewById(R.id.tvNombreEvento)
            ubicacion = itemView.findViewById(R.id.tvUbicacionEvento)
            hora = itemView.findViewById(R.id.tvHoraEvento)
        }
    }

    init {
        this.eventos = eventos
        this.listener = listener
    }
}