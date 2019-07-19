package com.example.laloinsane.austral_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIClient;
import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Conexion;
import com.example.laloinsane.austral_app.Models.Unidad;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampusActivity extends AppCompatActivity {
    private int id_campus;
    private double lat;
    private double lon;
    private APIService api;
    MapView map = null;
    private ItemizedOverlay<OverlayItem> markers;
    private Dialog calcular_ruta;
    private TextView text_calcular_ruta;
    private Button btn_calcular_ruta_gps;
    private Button btn_calcular_ruta_cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_campus);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();
        id_campus = extras.getInt("campus_id");
        lat = extras.getDouble("latitud");
        lon = extras.getDouble("longitud");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(extras.getString("campus_name"));

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        GeoPoint startPoint = new GeoPoint(lat, lon);
        mapController.setCenter(startPoint);

        obtenerDatos();
    }

    private void obtenerDatos(){
        api = APIClient.getAPIClient().create(APIService.class);
        Call<List<Unidad>> apiCall = api.getUnidades(id_campus);

        apiCall.enqueue(new Callback<List<Unidad>>() {
            @Override
            public void onResponse(Call<List<Unidad>> call, Response<List<Unidad>> response) {
                List<Unidad> unidades = response.body();
                ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

                for (final Unidad unidad : unidades) {
                    List<Conexion> con = unidad.getConexiones();
                    if (con.size() != 0) {
                        for (Conexion co : con) {
                            items.add(new OverlayItem(unidad.getNombre_unidad(), unidad.getId_unidad() + "", new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                            markers = new ItemizedIconOverlay<>(items,
                                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                        @Override
                                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                            createDialog(Integer.parseInt(item.getSnippet()), item.getTitle(), item.getPoint().getLatitude(), item.getPoint().getLongitude());
                                            calcular_ruta.show();
                                            return true;
                                        }

                                        @Override
                                        public boolean onItemLongPress(final int index, final OverlayItem item) {
                                            Toast.makeText(
                                                    CampusActivity.this,
                                                    "Item '" + item.getTitle() + "' (index long=" + index
                                                            + ")", Toast.LENGTH_LONG).show();
                                            return false;
                                        }
                                    }, getApplicationContext());
                            map.getOverlays().add(markers);
                            map.invalidate();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Unidad>> call, Throwable t) {
            }
        });
    }

    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
        overridePendingTransition(0, 0);
    }

    protected void createDialog(int id, final String nombre, final Double la, final Double lo){
        calcular_ruta = new Dialog(this);
        calcular_ruta.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calcular_ruta.setContentView(R.layout.layout_calcular_ruta);
        calcular_ruta.setCanceledOnTouchOutside(true);
        calcular_ruta.setCancelable(true);

        text_calcular_ruta = (TextView) calcular_ruta.findViewById(R.id.text_calcular_ruta);
        text_calcular_ruta.setText(nombre);

        btn_calcular_ruta_gps = (Button) calcular_ruta.findViewById(R.id.btn_calcular_ruta_gps);
        btn_calcular_ruta_gps.setTag(id);//a cada boton le agregas un tag
        btn_calcular_ruta_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CampusActivity.this, RutaGpsActivity.class);

                int valor = (Integer) view.getTag();//tomas el tag asignado
                //Traspaso de datos al CampusActivity
                intent.putExtra("campus_gps", id_campus);
                intent.putExtra("unidad_id_gps", valor);
                intent.putExtra("latitud_gps", 	lat);
                intent.putExtra("longitud_gps", lon);
                intent.putExtra("latitud_gps_unidad", 	la);
                intent.putExtra("longitud_gps_unidad", lo);
                intent.putExtra("nombre", nombre);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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
    }
}