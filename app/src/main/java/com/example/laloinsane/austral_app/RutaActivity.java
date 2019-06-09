package com.example.laloinsane.austral_app;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Conexion;
import com.example.laloinsane.austral_app.Models.Unidad;
import com.example.laloinsane.austral_app.Models.UnidadNodos;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import nose.Nodo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RutaActivity extends AppCompatActivity {

    private Retrofit retrofit;

    /*private TextView id_test_distancia;
    private TextView id_test_ruta;*/

    //osmdroid
    MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("" + extras.getInt("unidad_id"));

        //osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_ruta);

        //osmdroid
        map = (MapView) findViewById(R.id.map_ruta);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //osmdroid
        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        //osmdroid
        GeoPoint startPoint = new GeoPoint(-41.490329, -72.896186);
        mapController.setCenter(startPoint);

        /*//polyline osmdroid
        List<GeoPoint> geoPoints = new ArrayList<>();
        //add your points here
        GeoPoint gPt1 = new GeoPoint(-41.489356, 	-72.895950);
        GeoPoint gPt2 = new GeoPoint(-41.490072, -72.895757);
        geoPoints.add(gPt1);
        geoPoints.add(gPt2);
        Polyline line = new Polyline();   //see note below!
        line.setPoints(geoPoints);
        map.getOverlayManager().add(line);*/

        retrofit = new Retrofit.Builder()
                .baseUrl("base_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos(extras.getInt("unidad_id"));
    }

    private void obtenerDatos(final int id){
        APIService api = retrofit.create(APIService.class);
        Call<UnidadNodos> call = api.getUnidad(id, 1);


        /*APIService service = retrofit.create(APIService.class);
        Call<CampusRespuesta> apiRespuestaCall = service.getCampus();*/

        call.enqueue(new Callback<UnidadNodos>() {
            @Override
            public void onResponse(Call<UnidadNodos> call, Response<UnidadNodos> response) {
                if(response.isSuccessful()){
                    UnidadNodos apiRespuesta = response.body();
                    ArrayList<Conexion> datos= apiRespuesta.getConexiones();
                    ArrayList<com.example.laloinsane.austral_app.Models.Nodo> datos_nodos= apiRespuesta.getNodos();
                    List<Nodo> lista_nodos_ejemplo = new ArrayList<nose.Nodo>();

                    for (com.example.laloinsane.austral_app.Models.Nodo nodo : datos_nodos) {
                        ArrayList<nose.Conexion> conexion_nueva = new ArrayList<nose.Conexion>();
                        List<Conexion> con = nodo.getConexiones();
                        if (con.size() != 0) {
                            for (Conexion co : con) {
                                nose.Conexion n_c = new nose.Conexion(co.getDestino(), co.getDistancia());
                                conexion_nueva.add(n_c);
                            }
                        }

                        nose.Nodo nodo_nuevo = new nose.Nodo(nodo.getId_nodo(), nodo.getId_campus(), nodo.getLatitud_nodo(), nodo.getLongitud_nodo(), conexion_nueva);
                        lista_nodos_ejemplo.add(nodo_nuevo);
                    }

                    nose.Vertice[] vertices = new nose.Vertice[lista_nodos_ejemplo.size()];
                    for(int i = 0; i < vertices.length; i++){
                        vertices[i] = new nose.Vertice(lista_nodos_ejemplo.get(i).getId_nodo());

                        List<nose.Conexion> con = lista_nodos_ejemplo.get(i).getConexiones();
                        for(int x = 0; x < con.size(); x++){
                            vertices[i].add_arista(lista_nodos_ejemplo.get(i).getId_nodo(), con.get(x).getDestino(), con.get(x).getDistancia());
                        }
                    }

                    double[][] matriz_adyacencia = new double[vertices.length][vertices.length];
                    for(int i = 0; i < vertices.length; i++){
                        for(int j = 0; j < vertices.length; j++){
                            boolean is_empty = false;
                            double distancia = 0;

                            ArrayList<nose.Arista> arr = vertices[i].getAristas();
                            for(int x = 0; x < arr.size(); x++){
                                if(vertices[j].getId_vertice() == arr.get(x).getId_vertice_destino()){
                                    is_empty = true;
                                    distancia = arr.get(x).getDistancia();
                                }
                            }
                            if(is_empty == true){
                                matriz_adyacencia[i][j] = distancia;
                            }else{
                                matriz_adyacencia[i][j] = 0;
                            }
                        }
                    }

                    //Api Caminos
                    /*List<Nodo> lista_nodos = new ArrayList<nose.Nodo>();

                    ArrayList<nose.Conexion> t0 = new ArrayList<nose.Conexion>();
                    nose.Conexion t0_c1 = new nose.Conexion(1, 16.0);
                    t0.add(t0_c1);
                    nose.Conexion t0_c2 = new nose.Conexion(2, 10.0);
                    t0.add(t0_c2);
                    nose.Conexion t0_c3 = new nose.Conexion(3, 5.0);
                    t0.add(t0_c3);

                    nose.Nodo t0_n1 = new nose.Nodo(0, 3, -41.0, -72.0, t0);
                    lista_nodos.add(t0_n1);

                    ArrayList<nose.Conexion> t1 = new ArrayList<nose.Conexion>();
                    nose.Conexion t1_c1 = new nose.Conexion(0, 16.0);
                    t1.add(t1_c1);
                    nose.Conexion t1_c2 = new nose.Conexion(2, 2.0);
                    t1.add(t1_c2);
                    nose.Conexion t1_c3 = new nose.Conexion(5, 4.0);
                    t1.add(t1_c3);
                    nose.Conexion t1_c4 = new nose.Conexion(6, 6.0);
                    t1.add(t1_c4);

                    nose.Nodo t1_n1 = new nose.Nodo(1, 3, -41.1, -72.1, t1);
                    lista_nodos.add(t1_n1);

                    ArrayList<nose.Conexion> t2 = new ArrayList<nose.Conexion>();
                    nose.Conexion t2_c1 = new nose.Conexion(0, 10.0);
                    t2.add(t2_c1);
                    nose.Conexion t2_c2 = new nose.Conexion(1, 2.0);
                    t2.add(t2_c2);
                    nose.Conexion t2_c3 = new nose.Conexion(3, 4.0);
                    t2.add(t2_c3);
                    nose.Conexion t2_c4 = new nose.Conexion(4, 10.0);
                    t2.add(t2_c4);
                    nose.Conexion t2_c5 = new nose.Conexion(5, 12.0);
                    t2.add(t2_c5);

                    nose.Nodo t2_n1 = new nose.Nodo(2, 3, -41.2, -72.2, t2);
                    lista_nodos.add(t2_n1);

                    ArrayList<nose.Conexion> t3 = new ArrayList<nose.Conexion>();
                    nose.Conexion t3_c1 = new nose.Conexion(0, 5.0);
                    t3.add(t3_c1);
                    nose.Conexion t3_c2 = new nose.Conexion(2, 4.0);
                    t3.add(t3_c2);
                    nose.Conexion t3_c3 = new nose.Conexion(4, 15.0);
                    t3.add(t3_c3);

                    nose.Nodo t3_n1 = new nose.Nodo(3, 3, -41.3, -72.3, t3);
                    lista_nodos.add(t3_n1);

                    ArrayList<nose.Conexion> t4 = new ArrayList<nose.Conexion>();
                    nose.Conexion t4_c1 = new nose.Conexion(2, 10.0);
                    t4.add(t4_c1);
                    nose.Conexion t4_c2 = new nose.Conexion(3, 15.0);
                    t4.add(t4_c2);
                    nose.Conexion t4_c3 = new nose.Conexion(5, 3.0);
                    t4.add(t4_c3);
                    nose.Conexion t4_c4 = new nose.Conexion(7, 5.0);
                    t4.add(t4_c4);

                    nose.Nodo t4_n1 = new nose.Nodo(4, 3, -41.4, -72.4, t4);
                    lista_nodos.add(t4_n1);

                    ArrayList<nose.Conexion> t5 = new ArrayList<nose.Conexion>();
                    nose.Conexion t5_c1 = new nose.Conexion(1, 4.0);
                    t5.add(t5_c1);
                    nose.Conexion t5_c2 = new nose.Conexion(2, 12.0);
                    t5.add(t5_c2);
                    nose.Conexion t5_c3 = new nose.Conexion(4, 3.0);
                    t5.add(t5_c3);
                    nose.Conexion t5_c4 = new nose.Conexion(6, 8.0);
                    t5.add(t5_c4);
                    nose.Conexion t5_c5 = new nose.Conexion(7, 16.0);
                    t5.add(t5_c5);

                    nose.Nodo t5_n1 = new nose.Nodo(5, 3, -41.5, -72.5, t5);
                    lista_nodos.add(t5_n1);

                    ArrayList<nose.Conexion> t6 = new ArrayList<nose.Conexion>();
                    nose.Conexion t6_c1 = new nose.Conexion(1, 6.0);
                    t6.add(t6_c1);
                    nose.Conexion t6_c2 = new nose.Conexion(5, 8.0);
                    t6.add(t6_c2);
                    nose.Conexion t6_c3 = new nose.Conexion(7, 7.0);
                    t6.add(t6_c3);

                    nose.Nodo t6_n1 = new nose.Nodo(6, 3, -41.6, -72.6, t6);
                    lista_nodos.add(t6_n1);

                    ArrayList<nose.Conexion> t7 = new ArrayList<nose.Conexion>();
                    nose.Conexion t7_c1 = new nose.Conexion(4, 5.0);
                    t7.add(t7_c1);
                    nose.Conexion t7_c2 = new nose.Conexion(5, 16.0);
                    t7.add(t7_c2);
                    nose.Conexion t7_c3 = new nose.Conexion(6, 7.0);
                    t7.add(t7_c3);

                    nose.Nodo t7_n1 = new nose.Nodo(7, 3, -41.7, -72.7, t7);
                    lista_nodos.add(t7_n1);

                    nose.Vertice[] vertices = new nose.Vertice[lista_nodos.size()];
                    for(int i = 0; i < vertices.length; i++){
                        vertices[i] = new nose.Vertice(lista_nodos.get(i).getId_nodo());

                        List<nose.Conexion> con = lista_nodos.get(i).getConexiones();
                        for(int x = 0; x < con.size(); x++){
                            vertices[i].add_arista(lista_nodos.get(i).getId_nodo(), con.get(x).getDestino(), con.get(x).getDistancia());
                        }
                    }

                    double[][] matriz_adyacencia = new double[vertices.length][vertices.length];
                    for(int i = 0; i < vertices.length; i++){
                        for(int j = 0; j < vertices.length; j++){
                            boolean is_empty = false;
                            double distancia = 0;

                            ArrayList<nose.Arista> arr = vertices[i].getAristas();
                            for(int x = 0; x < arr.size(); x++){
                                if(vertices[j].getId_vertice() == arr.get(x).getId_vertice_destino()){
                                    is_empty = true;
                                    distancia = arr.get(x).getDistancia();
                                }
                            }
                            if(is_empty == true){
                                matriz_adyacencia[i][j] = distancia;
                            }else{
                                matriz_adyacencia[i][j] = 0;
                            }
                        }
                    }*/

                    nose.Grafo b = new nose.Grafo(vertices, matriz_adyacencia);
                    nose.direccion hol = b.camino_mas_corto(0, datos.get(0).getDestino());

                    ArrayList<Integer> ruta = hol.getRuta();

                    List<GeoPoint> geoPoints = new ArrayList<>();

                    for(int i= 0; i<ruta.size(); i++){
                        for (com.example.laloinsane.austral_app.Models.Nodo nodo : datos_nodos) {
                            if(ruta.get(i) == nodo.getId_nodo()){
                                GeoPoint gpt = new GeoPoint(nodo.getLatitud_nodo(), 	nodo.getLongitud_nodo());
                                geoPoints.add(gpt);
                            }
                        }
                    }

                    Polyline line = new Polyline();   //see note below!
                    line.setPoints(geoPoints);
                    map.getOverlayManager().add(line);

                    /*for (com.example.laloinsane.austral_app.Models.Nodo nodo : datos_nodos) {
                        if(hol.getRuta().contains(nodo.getId_nodo()) == true){
                            GeoPoint gpt = new GeoPoint(nodo.getLatitud_nodo(), 	nodo.getLongitud_nodo());
                            geoPoints.add(gpt);*/

                            /*//polyline osmdroid
                            List<GeoPoint> geoPoints = new ArrayList<>();
                            //add your points here
                            GeoPoint gPt1 = new GeoPoint(-41.489356, 	-72.895950);
                            GeoPoint gPt2 = new GeoPoint(-41.490072, -72.895757);
                            geoPoints.add(gPt1);
                            geoPoints.add(gPt2);
                            Polyline line = new Polyline();   //see note below!
                            line.setPoints(geoPoints);
                            map.getOverlayManager().add(line);*/
                    //    }
                    //}

                    /*Polyline line = new Polyline();   //see note below!
                    line.setPoints(geoPoints);
                    map.getOverlayManager().add(line);*/

                    //nose.Grafo b = new nose.Grafo(vertices, matriz_adyacencia);
                    //nose.direccion hol = b.camino_mas_corto(4, 7);


                    /*id_test_distancia = (TextView) findViewById(R.id.id_test_distancia);
                    //id_test_distancia.setText("id destino" + datos.get(0).getDestino());
                    id_test_distancia.setText("distancia" + hol.getDistancia());

                    id_test_ruta = (TextView) findViewById(R.id.id_test_ruta);
                    id_test_ruta.setText("ruta" + hol.getRuta());*/

                }else{
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
}
