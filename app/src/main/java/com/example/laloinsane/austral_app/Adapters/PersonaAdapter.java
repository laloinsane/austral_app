package com.example.laloinsane.austral_app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.laloinsane.austral_app.Models.Persona;
import com.example.laloinsane.austral_app.R;

import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.MyViewHolder>{
    private List<Persona> personas;
    private Context context;

    public PersonaAdapter(List<Persona> personas, Context context) {
        this.personas = personas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personas, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nombre_persona.setText(personas.get(position).getNombre_persona());
        holder.correo_persona.setText(personas.get(position).getCorreo_persona());
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nombre_persona, correo_persona;
        public MyViewHolder(View itemView) {
            super(itemView);
            nombre_persona = itemView.findViewById(R.id.nombre_persona);
            correo_persona = itemView.findViewById(R.id.correo_persona);
        }
    }
}
