package com.example.musicalevents.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicalevents.R;
import com.example.musicalevents.data.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder>{

    List<Event> eventos;
    onManageEventoListener listener;

    public EventoAdapter(ArrayList<Event> eventos, onManageEventoListener listener) {
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
        holder.hora.setText(eventos.get(position).getHoraComienzo());
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
        void onEditEvento(Event l);

        void onDeleteEvento(Event l);
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

        public void bind(Event l, onManageEventoListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEditEvento(l);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onDeleteEvento(l);
                    return true;
                }
            });
        }

    }

    public boolean removeItem(Event item)
    {
        int position = eventos.indexOf(item);

        if (position == -1) return false;

        eventos.remove(item);
        notifyItemRemoved(position);

        return true;
    }
}
