package com.example.laloinsane.austral_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laloinsane.austral_app.MainActivity;
import com.example.laloinsane.austral_app.Models.Campus;
import com.example.laloinsane.austral_app.R;

import java.util.ArrayList;

public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.ViewHolder>{
    private ArrayList<Campus> dataset;
    private Context context;

    public CampusAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }

    public void addCampus(ArrayList<Campus> lista){
        dataset.addAll(lista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_campus_menu, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Campus p = dataset.get(i);
        viewHolder.id_campus.setText(Integer.toString(p.getId_campus()));
        viewHolder.nombre_campus.setText(p.getNombre_campus());
        viewHolder.direccion_campus.setText(p.getDireccion_campus());
        viewHolder.latitud_campus.setText(Double.toString(p.getLatitud_campus()));
        viewHolder.longitud_campus.setText(Double.toString(p.getLongitud_campus()));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView id_campus;
        private TextView nombre_campus;
        private TextView direccion_campus;
        private TextView latitud_campus;
        private TextView longitud_campus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            id_campus = (TextView) itemView.findViewById(R.id.id_campus);
            nombre_campus = (TextView) itemView.findViewById(R.id.nombre_campus);
            direccion_campus = (TextView) itemView.findViewById(R.id.direccion_campus);
            latitud_campus = (TextView) itemView.findViewById(R.id.latitud_campus);
            longitud_campus = (TextView) itemView.findViewById(R.id.longitud_campus);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                Campus clickedDataItem = dataset.get(pos);
                Intent intent = new Intent(context, MainActivity.class);
                //intent.putExtra("latitud",clickedDataItem.getLatitud_campus());
                //intent.putExtra("longitud",clickedDataItem.getLongitud_campus());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getId_campus(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
