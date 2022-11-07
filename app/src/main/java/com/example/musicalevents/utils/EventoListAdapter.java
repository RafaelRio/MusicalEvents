package com.example.musicalevents.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicalevents.R;
import com.example.musicalevents.data.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventoListAdapter extends RecyclerView.Adapter<EventoListAdapter.ViewHolder>{

    List<Event> eventos;
    onManageEventoListener listener;

    public EventoListAdapter(ArrayList<Event> eventos, onManageEventoListener listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evento, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ubicacion.setText(eventos.get(position).getUbicacion());
        holder.nombre.setText(eventos.get(position).getNombreEvento());
        Calendar eventDate = Calendar.getInstance();
        eventDate.setTimeInMillis(eventos.get(position).getFechaInicioMiliSegundos());
        holder.hora.setText(String.format("%02d", eventDate.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", eventDate.get(Calendar.MINUTE)));
        holder.bind(eventos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public void update(List<Event> l) {
        this.eventos.clear();
        this.eventos.addAll(l);
        notifyDataSetChanged();
    }

    public interface onManageEventoListener {
        void onInfoEvent(Event l);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        TextView ubicacion;
        TextView hora;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreEvento);
            ubicacion = itemView.findViewById(R.id.tvUbicacionEvento);
            hora = itemView.findViewById(R.id.tvHoraEvento);
        }

        public void bind(Event event, onManageEventoListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onInfoEvent(event);
                }
            });
        }

    }
}
