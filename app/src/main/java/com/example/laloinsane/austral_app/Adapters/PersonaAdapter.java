package com.example.laloinsane.austral_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laloinsane.austral_app.Models.Persona;
import com.example.laloinsane.austral_app.PersonasActivity;
import com.example.laloinsane.austral_app.R;
import com.example.laloinsane.austral_app.RutaGpsActivity;

import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.MyViewHolder>{
    private List<Persona> personas;
    private Context context;

    private Dialog calcular_ruta;
    private Button btn_calcular_ruta_gps;
    private Button btn_calcular_ruta_cancelar;
    private TextView text_calcular_ruta;

    private int campus;
    private double lat;
    private double lon;


    public PersonaAdapter(List<Persona> personas, Context context, int campus, double lat, double lon) {
        this.personas = personas;
        this.context = context;
        this.campus = campus;
        this.lat = lat;
        this.lon = lon;
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
        holder.unidad_persona.setText(personas.get(position).getNombre_unidad());
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre_persona, correo_persona, unidad_persona;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nombre_persona = itemView.findViewById(R.id.nombre_persona);
            correo_persona = itemView.findViewById(R.id.correo_persona);
            unidad_persona = itemView.findViewById(R.id.unidad_persona);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                final Persona clickedDataItem = personas.get(pos);

                calcular_ruta = new Dialog(context);
                calcular_ruta.requestWindowFeature(Window.FEATURE_NO_TITLE);
                calcular_ruta.setContentView(R.layout.layout_calcular_ruta);
                calcular_ruta.setCanceledOnTouchOutside(true);
                calcular_ruta.setCancelable(true);

                text_calcular_ruta = (TextView) calcular_ruta.findViewById(R.id.text_calcular_ruta);
                text_calcular_ruta.setText(clickedDataItem.getNombre_persona());


                //text_calcular_ruta.setText("¿Quieres calcular el camino mas corto hasta la unidad "+'"'+clickedDataItem.getNombre_persona()+'"'+" ?");

                btn_calcular_ruta_gps = (Button) calcular_ruta.findViewById(R.id.btn_calcular_ruta_gps);
                //btn_calcular_ruta_gps.setTag(id);//a cada boton le agregas un tag
                btn_calcular_ruta_gps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, RutaGpsActivity.class);

                        //int valor = (Integer) view.getTag();//tomas el tag asignado

                        //Traspaso de datos al CampusActivity
                        intent.putExtra("campus_gps", campus);
                        intent.putExtra("unidad_id_gps", clickedDataItem.getId_unidad());
                        intent.putExtra("latitud_gps", lat);
                        intent.putExtra("longitud_gps", lon);
                        intent.putExtra("latitud_gps_unidad", clickedDataItem.getLatitud_unidad());
                        intent.putExtra("longitud_gps_unidad", clickedDataItem.getLongitud_unidad());
                        intent.putExtra("nombre", clickedDataItem.getNombre_persona());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent);
                        calcular_ruta.dismiss();
                    }
                });

                btn_calcular_ruta_cancelar = (Button) calcular_ruta.findViewById(R.id.btn_calcular_ruta_cancelar);
                btn_calcular_ruta_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calcular_ruta.dismiss();
                    }
                });

                calcular_ruta.show();

                //Campus clickedDataItem = dataset.get(pos);
                //Intent intent = new Intent(context, PersonasActivity.class);

                //Traspaso de datos al CampusActivity
                /*intent.putExtra("campus_id",clickedDataItem.getId_campus());
                intent.putExtra("campus_name",clickedDataItem.getNombre_campus());
                intent.putExtra("latitud",clickedDataItem.getLatitud_campus());
                intent.putExtra("longitud",clickedDataItem.getLongitud_campus());*/

                /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getNombre_persona(), Toast.LENGTH_SHORT).show();*/
            }
        }
    }
}
