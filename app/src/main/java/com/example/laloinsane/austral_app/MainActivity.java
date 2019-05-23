package com.example.laloinsane.austral_app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIService;
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

public class MainActivity extends AppCompatActivity {

    MapView map = null;
    private Retrofit retrofit;
    private ItemizedOverlay<OverlayItem> markers;
    private MyLocationNewOverlay mLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Bundle extras = getIntent().getExtras();
        String latitud_campus = extras.getString("latitud");
        String longitud_campus = extras.getString("longitud");
        double lat_campus = Double.parseDouble(latitud_campus);
        double lon_campus = Double.parseDouble(longitud_campus);*/

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //My Location
        //note you have handle the permissions yourself, the overlay did not do it for you
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        retrofit = new Retrofit.Builder()
                //.baseUrl("base_url")
                .baseUrl("base_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);
        //Call<List<Entidad>> call = api.getENTIDADES();
        Call<List<Unidad>> call = api.getUNIDADES();

        /*call.enqueue(new Callback<List<Entidad>>() {
            @Override
            public void onResponse(Call<List<Entidad>> call, Response<List<Entidad>> response) {
                if(!response.isSuccessful()){
                    //hora.setText("Code: "+response.code());
                    return;
                }
                List<Entidad> entidades = response.body();
                ArrayList<OverlayItem> anotherOverlayItemArray;
                anotherOverlayItemArray = new ArrayList<OverlayItem>();

                for (Entidad entidad : entidades){
                    double lat = Double.parseDouble(entidad.getLATITUD_ENTIDAD());
                    double lon = Double.parseDouble(entidad.getLONGITUD_ENTIDAD());

                    anotherOverlayItemArray.add(new OverlayItem(entidad.getNOMBRE_ENTIDAD(), "Ejemplo", new GeoPoint(lat, lon)));

                    markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                @Override
                                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Item '" + item.getTitle() + "' (index=" + index
                                                    + ")", Toast.LENGTH_LONG).show();
                                    return true;
                                }

                                @Override
                                public boolean onItemLongPress(final int index, final OverlayItem item) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Item '" + item.getTitle() + "' (index=" + index
                                                    + ")", Toast.LENGTH_LONG).show();
                                    return false;
                                }
                            }, getApplicationContext());
                    map.getOverlays().add(markers);
                }
            }

            @Override
            public void onFailure(Call<List<Entidad>> call, Throwable t) {
                //hora.setText(t.getMessage());
            }
        });*/

        call.enqueue(new Callback<List<Unidad>>() {
            @Override
            public void onResponse(Call<List<Unidad>> call, Response<List<Unidad>> response) {
                if(!response.isSuccessful()){
                    //hora.setText("Code: "+response.code());
                    return;
                }
                List<Unidad> unidades = response.body();
                ArrayList<OverlayItem> anotherOverlayItemArray;
                anotherOverlayItemArray = new ArrayList<OverlayItem>();

                for (Unidad unidad : unidades){
                    double lat = Double.parseDouble(unidad.getLATITUD_UNIDAD());
                    double lon = Double.parseDouble(unidad.getLONGITUD_UNIDAD());

                    anotherOverlayItemArray.add(new OverlayItem(unidad.getNOMBRE_UNIDAD(), "Ejemplo", new GeoPoint(lat, lon)));

                    markers = new ItemizedIconOverlay<>(anotherOverlayItemArray,
                            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                                @Override
                                public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Item '" + item.getTitle() + "' (index=" + index
                                                    + ")", Toast.LENGTH_LONG).show();
                                    return true;
                                }

                                @Override
                                public boolean onItemLongPress(final int index, final OverlayItem item) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Item '" + item.getTitle() + "' (index=" + index
                                                    + ")", Toast.LENGTH_LONG).show();
                                    return false;
                                }
                            }, getApplicationContext());
                    map.getOverlays().add(markers);
                }
            }

            @Override
            public void onFailure(Call<List<Unidad>> call, Throwable t) {
                //hora.setText(t.getMessage());
            }
        });

        map.getController().setZoom(17.5);

        //mi localizacion
        GeoPoint geoPoint = mLocationOverlay.getMyLocation();
        map.getController().animateTo(geoPoint);

        IMapController mapController = map.getController();
        //mapController.setZoom(17.5);

        //GeoPoint startPoint = new GeoPoint(lat_campus, lon_campus);
        //mapController.setCenter(startPoint);

        //map.getController().setZoom(15);
        //map.setTilesScaledToDpi(true);
        //map.setBuiltInZoomControls(true);
        //mMapView.setMultiTouchControls(true);
        //map.setFlingEnabled(true);
        //mMapView.getOverlays().add(this.mLocationOverlay);
        //mMapView.getOverlays().add(this.mCompassOverlay);
        //mMapView.getOverlays().add(this.mScaleBarOverlay);
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

}
