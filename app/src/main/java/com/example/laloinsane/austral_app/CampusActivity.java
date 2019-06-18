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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampusActivity extends AppCompatActivity {

    private Dialog calcular_ruta;
    private Button btn_calcular_ruta_aceptar;
    private TextView text_calcular_ruta;
    MapView map = null;
    private Retrofit retrofit;
    private ItemizedOverlay<OverlayItem> markers;
    private MyLocationNewOverlay mLocationOverlay;
    private int id_campus;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();
        id_campus = extras.getInt("campus_id");
        lat = extras.getDouble("latitud");
        lon = extras.getDouble("longitud");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(extras.getString("campus_name"));

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_campus);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //My Location
        //note you have handle the permissions yourself, the overlay did not do it for you
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        retrofit = new Retrofit.Builder()
                .baseUrl("base_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);
        Call<List<Unidad>> call = api.getUnidades(id_campus);

        call.enqueue(new Callback<List<Unidad>>() {
            @Override
            public void onResponse(Call<List<Unidad>> call, Response<List<Unidad>> response) {
                if (!response.isSuccessful()) {
                    //hora.setText("Code: "+response.code());
                    return;
                }
                List<Unidad> unidades = response.body();
                ArrayList<OverlayItem> anotherOverlayItemArray;
                anotherOverlayItemArray = new ArrayList<OverlayItem>();

                for (final Unidad unidad : unidades) {
                    List<Conexion> con = unidad.getConexiones();
                    if (con.size() != 0) {
                        for (Conexion co : con) {
                            anotherOverlayItemArray.add(new OverlayItem(unidad.getNombre_unidad(), unidad.getId_unidad() + "", new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                            markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                        @Override
                                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                            createDialog(Integer.parseInt(item.getSnippet()), item.getTitle());
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
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Unidad>> call, Throwable t) {
                //hora.setText(t.getMessage());
            }
        });

        //map.getController().setZoom(17.5);
        //mi localizacion
        //GeoPoint geoPoint = mLocationOverlay.getMyLocation();
        //map.getController().animateTo(geoPoint);

        IMapController mapController = map.getController();
        mapController.setZoom(17.5);

        GeoPoint startPoint = new GeoPoint(lat, lon);
        mapController.setCenter(startPoint);
    }

    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public void onPause() {
        super.onPause();
        map.onPause();
    }

    protected void createDialog(int id, String nombre){
        calcular_ruta = new Dialog(this);
        calcular_ruta.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calcular_ruta.setContentView(R.layout.layout_calcular_ruta);
        calcular_ruta.setCanceledOnTouchOutside(true);
        calcular_ruta.setCancelable(true);

        text_calcular_ruta = (TextView) calcular_ruta.findViewById(R.id.text_calcular_ruta);
        text_calcular_ruta.setText("¿Quieres calcular el camino mas corto hasta la unidad "+'"'+nombre+'"'+" ?");

        btn_calcular_ruta_aceptar = (Button) calcular_ruta.findViewById(R.id.btn_calcular_ruta_aceptar);
        btn_calcular_ruta_aceptar.setTag(id);//a cada boton le agregas un tag

        btn_calcular_ruta_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CampusActivity.this, RutaActivity.class);

                int valor = (Integer) view.getTag();//tomas el tag asignado

                //Traspaso de datos al CampusActivity
                intent.putExtra("campus", id_campus);
                intent.putExtra("unidad_id", valor);
                intent.putExtra("latitud", 	lat);
                intent.putExtra("longitud", lon);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                calcular_ruta.dismiss();
            }
        });
    }
}