package com.example.laloinsane.austral_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.laloinsane.austral_app.CampusActivity;
import com.example.laloinsane.austral_app.Models.Campus;
import com.example.laloinsane.austral_app.PersonasSearchActivity;
import com.example.laloinsane.austral_app.R;

import java.util.ArrayList;

public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.ViewHolder>{

    private ArrayList<Campus> dataset;
    private Context context;
    private Dialog seleccionar_busqueda;
    private Button btn_busqueda_campus;
    private Button btn_busqueda_personas;

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
        viewHolder.nombre_campus.setText(p.getNombre_campus());
        viewHolder.direccion_campus.setText(p.getDireccion_campus());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nombre_campus;
        private TextView direccion_campus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre_campus = (TextView) itemView.findViewById(R.id.nombre_campus);
            direccion_campus = (TextView) itemView.findViewById(R.id.direccion_campus);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                final Campus clickedDataItem = dataset.get(pos);

                seleccionar_busqueda = new Dialog(context);
                seleccionar_busqueda.requestWindowFeature(Window.FEATURE_NO_TITLE);
                seleccionar_busqueda.setContentView(R.layout.layout_seleccionar_busqueda);
                seleccionar_busqueda.setCanceledOnTouchOutside(true);
                seleccionar_busqueda.setCancelable(true);

                btn_busqueda_campus = (Button) seleccionar_busqueda.findViewById(R.id.btn_busqueda_campus);
                btn_busqueda_campus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, CampusActivity.class);

                        //Traspaso de datos al CampusActivity
                        intent.putExtra("campus_id",clickedDataItem.getId_campus());
                        intent.putExtra("campus_name",clickedDataItem.getNombre_campus());
                        intent.putExtra("latitud",clickedDataItem.getLatitud_campus());
                        intent.putExtra("longitud",clickedDataItem.getLongitud_campus());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        seleccionar_busqueda.dismiss();
                    }
                });

                btn_busqueda_personas = (Button) seleccionar_busqueda.findViewById(R.id.btn_busqueda_personas);
                btn_busqueda_personas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, PersonasSearchActivity.class);

                        //Traspaso de datos a PersonasSearchActivity
                        intent.putExtra("campus_id_busqueda",clickedDataItem.getId_campus());
                        intent.putExtra("campus_name_busqueda",clickedDataItem.getNombre_campus());
                        intent.putExtra("latitud_busqueda",clickedDataItem.getLatitud_campus());
                        intent.putExtra("longitud_busqueda",clickedDataItem.getLongitud_campus());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        seleccionar_busqueda.dismiss();
                    }
                });

                seleccionar_busqueda.show();
            }
        }
    }
}
