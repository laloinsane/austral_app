package com.example.laloinsane.austral_app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIClient;
import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Conexion;
import com.example.laloinsane.austral_app.Models.UnidadNodos;
import com.example.laloinsane.austral_app.Tools.Haversine;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutaGpsActivity extends AppCompatActivity implements View.OnClickListener{
    private int var_id_unidad;
    private int var_id_campus;
    private double var_lat;
    private double var_lon;
    private Polyline line_old;
    private Button btn_get_ruta;
    private APIService api;
    MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    Marker startMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();
        var_id_unidad = extras.getInt("unidad_id_gps");
        var_id_campus = extras.getInt("campus_gps");
        var_lat = extras.getDouble("latitud_gps_unidad");
        var_lon = extras.getDouble("longitud_gps_unidad");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(extras.getString("nombre"));

        //osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_ruta_gps);

        btn_get_ruta = (Button) findViewById(R.id.btn_get_ruta);
        btn_get_ruta.setOnClickListener(this);
        line_old = new Polyline();

        //osmdroid
        map = (MapView) findViewById(R.id.map_ruta_gps);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //osmdroid
        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        //osmdroid
        GeoPoint startPoint = new GeoPoint(extras.getDouble("latitud_gps"), extras.getDouble("longitud_gps"));
        mapController.setCenter(startPoint);

        //My Location
        //note you have handle the permissions yourself, the overlay did not do it for you
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), map);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);

        startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(var_lat, var_lon));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setInfoWindow(null);
        map.getOverlays().add(startMarker);
    }

    private void obtenerDatos(final int id_unidad, final int id_campus) {
        map.getOverlayManager().remove(line_old);
        api = APIClient.getAPIClient().create(APIService.class);
        Call<UnidadNodos> call = api.getUnidad(id_campus, id_unidad);

        call.enqueue(new Callback<UnidadNodos>() {
            @Override
            public void onResponse(Call<UnidadNodos> call, Response<UnidadNodos> response) {
                if (response.isSuccessful()) {
                    UnidadNodos apiRespuesta = response.body();

                    ArrayList<Conexion> datos = apiRespuesta.getConexiones();
                    ArrayList<com.example.laloinsane.austral_app.Models.Nodo> datos_nodos = apiRespuesta.getNodos();

                    //dijkstra library
                    List<dijkstra.Nodo> lista_nodos_ejemplo = new ArrayList<dijkstra.Nodo>();

                    int nodo_origen = 0;
                    double distancia_origen = 0;

                    for (com.example.laloinsane.austral_app.Models.Nodo nodo : datos_nodos) {
                        //dijkstra library
                        ArrayList<dijkstra.Conexion> conexion_nueva = new ArrayList<dijkstra.Conexion>();

                        List<Conexion> con = nodo.getConexiones();
                        if (con.size() != 0) {
                            for (Conexion co : con) {
                                //dijkstra library
                                dijkstra.Conexion n_c = new dijkstra.Conexion(co.getDestino(), co.getDistancia());
                                conexion_nueva.add(n_c);
                            }
                        }
                        //dijkstra library
                        dijkstra.Nodo nodo_nuevo = new dijkstra.Nodo(nodo.getId_nodo(), nodo.getId_campus(), nodo.getLatitud_nodo(), nodo.getLongitud_nodo(), conexion_nueva);
                        lista_nodos_ejemplo.add(nodo_nuevo);

                    }
                    //dijkstra library
                    dijkstra.Vertice[] vertices = new dijkstra.Vertice[lista_nodos_ejemplo.size()];

                    for (int i = 0; i < vertices.length; i++) {
                        //dijkstra library
                        vertices[i] = new dijkstra.Vertice(lista_nodos_ejemplo.get(i).getId_nodo());

                        Haversine jue = new Haversine(6371);
                        double distancia = jue.CalculationByDistance(mLocationOverlay.getMyLocationProvider().getLastKnownLocation().getLatitude(), mLocationOverlay.getMyLocationProvider().getLastKnownLocation().getLongitude(), lista_nodos_ejemplo.get(i).getLatitud_nodo(), lista_nodos_ejemplo.get(i).getLongitud_nodo());

                        if (nodo_origen == 0) {
                            nodo_origen = lista_nodos_ejemplo.get(i).getId_nodo();
                            distancia_origen = distancia;
                        } else {
                            if (distancia < distancia_origen) {
                                nodo_origen = lista_nodos_ejemplo.get(i).getId_nodo();
                                distancia_origen = distancia;
                            }
                        }
                        //dijkstra library
                        List<dijkstra.Conexion> con = lista_nodos_ejemplo.get(i).getConexiones();

                        for (int x = 0; x < con.size(); x++) {
                            vertices[i].add_arista(lista_nodos_ejemplo.get(i).getId_nodo(), con.get(x).getDestino(), con.get(x).getDistancia());
                        }
                    }

                    double[][] matriz_adyacencia = new double[vertices.length][vertices.length];
                    for (int i = 0; i < vertices.length; i++) {
                        for (int j = 0; j < vertices.length; j++) {
                            boolean is_empty = false;
                            double distancia = 0;

                            //dijkstra library
                            ArrayList<dijkstra.Arista> arr = vertices[i].getAristas();

                            for (int x = 0; x < arr.size(); x++) {
                                if (vertices[j].getId_vertice() == arr.get(x).getId_vertice_destino()) {
                                    is_empty = true;
                                    distancia = arr.get(x).getDistancia();
                                }
                            }
                            if (is_empty == true) {
                                matriz_adyacencia[i][j] = distancia;
                            } else {
                                matriz_adyacencia[i][j] = 0;
                            }
                        }
                    }
                    //dijkstra library
                    dijkstra.Grafo b = new dijkstra.Grafo(vertices, matriz_adyacencia);
                    dijkstra.Direccion hol = b.camino_mas_corto(nodo_origen, datos.get(0).getDestino());

                    ArrayList<Integer> ruta = hol.getRuta();

                    List<GeoPoint> geoPoints = new ArrayList<>();

                    for (int i = 0; i < ruta.size(); i++) {
                        for (com.example.laloinsane.austral_app.Models.Nodo nodo : datos_nodos) {
                            if (ruta.get(i) == nodo.getId_nodo()) {
                                GeoPoint gpt = new GeoPoint(nodo.getLatitud_nodo(), nodo.getLongitud_nodo());
                                geoPoints.add(gpt);
                            }
                        }
                    }

                    line_old.setPoints(geoPoints);
                    map.getOverlayManager().add(line_old);
                    map.invalidate();

                    map.getOverlayManager().remove(startMarker);
                    map.invalidate();

                    startMarker = new Marker(map);
                    startMarker.setPosition(new GeoPoint(var_lat, var_lon));
                    startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    startMarker.setInfoWindow(null);
                    map.getOverlays().add(startMarker);
                    map.invalidate();

                } else {
                    Log.e("TIPO_MENU", " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UnidadNodos> call, Throwable t) {
                Log.e("TIPO_MENU", " onFailure: " + t.getMessage());
            }
        });
    }

    //osmdroid
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    //osmdroid
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_get_ruta: {
                if(mLocationOverlay.getMyLocation() == null) {
                    Toast.makeText(this, "No se ha podido encontrar la señal del GPS", Toast.LENGTH_LONG).show();
                }else {
                    obtenerDatos(var_id_unidad, var_id_campus);
                }
                break;
            }
        }
    }
}
