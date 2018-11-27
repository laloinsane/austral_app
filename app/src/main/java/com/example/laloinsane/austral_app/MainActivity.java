package com.example.laloinsane.austral_app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Entidad;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    MapView map = null;
    private Retrofit retrofit;
    private ItemizedOverlay<OverlayItem> mMyLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        retrofit = new Retrofit.Builder()
                .baseUrl("base_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService api = retrofit.create(APIService.class);
        Call<List<Entidad>> call = api.getENTIDADES();

        call.enqueue(new Callback<List<Entidad>>() {
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

                    mMyLocationOverlay = new ItemizedIconOverlay<>(anotherOverlayItemArray,
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
                    map.getOverlays().add(mMyLocationOverlay);
                }
            }

            @Override
            public void onFailure(Call<List<Entidad>> call, Throwable t) {
                //hora.setText(t.getMessage());
            }
        });

        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        GeoPoint startPoint = new GeoPoint(-41.49068, -72.89589);
        mapController.setCenter(startPoint);
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
