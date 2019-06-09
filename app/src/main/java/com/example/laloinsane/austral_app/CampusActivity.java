package com.example.laloinsane.austral_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Conexion;
import com.example.laloinsane.austral_app.Models.Nodo;
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

import nose.Grafo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampusActivity extends AppCompatActivity {

    private Dialog acerca;
    private Button btn_acerca_aceptar_x;
    MapView map = null;
    private Retrofit retrofit;
    private ItemizedOverlay<OverlayItem> markers;
    private MyLocationNewOverlay mLocationOverlay;
    private TextView textejemplo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();

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
        Call<List<Unidad>> call = api.getUnidades(extras.getInt("campus_id"));

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
                    /*if (con.size() != 0) {
                        for (Conexion co : con) {
                            anotherOverlayItemArray.add(new OverlayItem(unidad.getNombre_unidad(), "" + co.getDestino(), new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                            markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                        @Override
                                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {

                                            //Intent intent=new Intent(CampusActivity.this, RutaActivity.class);
                                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            //startActivity(intent);

                                            //acerca.show();

                                            Toast.makeText(
                                                    CampusActivity.this,
                                                    "Item '" + item.getTitle() + "' (single=" + index
                                                            + ")" + " " + item.getSnippet(), Toast.LENGTH_LONG).show();
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
                        }
                    } else {
                        anotherOverlayItemArray.add(new OverlayItem(unidad.getNombre_unidad(), "ejemplo", new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                        markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                    @Override
                                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                        Toast.makeText(
                                                CampusActivity.this,
                                                "Item '" + item.getTitle() + "' (single=" + index
                                                        + ")" + " " + item.getSnippet(), Toast.LENGTH_LONG).show();
                                        //acerca.show();
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
                    }*/

                    /*List<Conexion> con = unidad.getConexiones();
                    if(con.size() != 0){
                        for (Conexion co : con){
                            anotherOverlayItemArray.add(new OverlayItem(unidad.getNombre_unidad(), co.getDestino(), new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                            if (co.getDestino() == 2) {
                                id_test = (TextView) findViewById(R.id.id_test);
                                id_test.setText("holo"+nodo.getLongitud_nodo()+co.getDistancia());
                            }
                        }
                    }else{

                    }*/




                    /*anotherOverlayItemArray.add(new OverlayItem( unidad.getNombre_unidad(), unidad.getId_unidad()+"", new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                    markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                @Override
                                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    createDialog(Integer.parseInt(item.getSnippet()));
                                    acerca.show();

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
                            }, getApplicationContext());*/
                    if (con.size() != 0) {
                        for (Conexion co : con) {
                            anotherOverlayItemArray.add(new OverlayItem(unidad.getNombre_unidad(), unidad.getId_unidad() + "", new GeoPoint(unidad.getLatitud_unidad(), unidad.getLongitud_unidad())));

                            markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                        @Override
                                        public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                            createDialog(Integer.parseInt(item.getSnippet()));
                                            acerca.show();

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
                    //map.getOverlays().add(markers);
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

        GeoPoint startPoint = new GeoPoint(extras.getDouble("latitud"), extras.getDouble("longitud"));
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

    protected void createDialog(int id){
        acerca = new Dialog(this);
        acerca.requestWindowFeature(Window.FEATURE_NO_TITLE);
        acerca.setContentView(R.layout.layout_calcular_ruta);
        acerca.setCanceledOnTouchOutside(true);
        acerca.setCancelable(true);
        //id_x = (TextView) findViewById(R.id.id_x);
        //id_x.setText("ruta: ");
        textejemplo = (TextView) acerca.findViewById(R.id.textejemplo);
        textejemplo.setText("wenawena"+id);
        btn_acerca_aceptar_x = (Button) acerca.findViewById(R.id.btn_acerca_aceptar_x);

        btn_acerca_aceptar_x.setTag(id);//a cada boton le agregas un tag

        btn_acerca_aceptar_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //acerca.dismiss();
                Intent intent=new Intent(CampusActivity.this, RutaActivity.class);

                int valor = (Integer) view.getTag();//tomas el tag asignado

                //Traspaso de datos al CampusActivity
                intent.putExtra("unidad_id", valor);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                acerca.dismiss();
            }
        });
    }
}