package com.example.musicalevents.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicalevents.R
import com.example.musicalevents.data.model.Event

/**
 * Adapter que permite el listado de eventos
 * @author Rafa
 */
class EventoListAdapterKt(eventos: ArrayList<Event>, listener: OnManageEventoListener) :
    RecyclerView.Adapter<EventoListAdapterKt.ViewHolder>() {
    var eventos: MutableList<Event>
    private var context: Context? = null
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
        UtilsKt.update(eventos, l)
        notifyDataSetChanged()
    }

    interface OnManageEventoListener {
        fun onInfoEvent(l: Event)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nombre: TextView
        var ubicacion: TextView
        var hora: TextView
        fun bind(event: Event, listener: OnManageEventoListener) {
            itemView.setOnClickListener { listener.onInfoEvent(event) }
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
