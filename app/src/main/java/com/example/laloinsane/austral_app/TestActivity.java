package com.example.laloinsane.austral_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Models.Conexion;
import com.example.laloinsane.austral_app.Models.Nodo;

import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import nose.Grafo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TestActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private TextView id_test;
    private TextView id_test_distancia;
    private TextView id_test_ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.9/tesis/austral_api/public/index.php/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos();
    }


    private void obtenerDatos(){
        APIService service = retrofit.create(APIService.class);
        Call<List<Nodo>> call = service.getNodos(3);
        //Call<CampusRespuesta> apiRespuestaCall = service.getCampus();

        call.enqueue(new Callback<List<Nodo>>() {
            @Override
            public void onResponse(Call<List<Nodo>> call, Response<List<Nodo>> response) {
                if(response.isSuccessful()){
                    List<Nodo> nodos = response.body();
                    //List<Nodo> nodos = response.body();
                    ///ArrayList<OverlayItem> anotherOverlayItemArray;
                    //anotherOverlayItemArray = new ArrayList<OverlayItem>();


                    nose.Vertice[] vertices = new nose.Vertice[nodos.size()];
                    for(int i = 0; i < vertices.length; i++){
                        vertices[i] = new nose.Vertice(nodos.get(i).getId_nodo());

                        List<Conexion> con = nodos.get(i).getConexiones();
                        for(int x = 0; x < con.size(); x++){
                            vertices[i].add_arista(nodos.get(i).getId_nodo(), con.get(x).getDestino(), con.get(x).getDistancia());
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
                //    List<nose.Nodo> lista_nodos = new ArrayList<nose.Nodo>();

		/*ArrayList<Conexion> t0 = new ArrayList<Conexion>();
		Conexion t0_c1 = new Conexion(3, 0.15);
		t0.add(t0_c1);
		Conexion t0_c2 = new Conexion(5, 0.03);
		t0.add(t0_c2);

		Nodo t0_n1 = new Nodo(2, 3, -41.0, -72.0, t0);
		test.add(t0_n1);

		ArrayList<Conexion> t1 = new ArrayList<Conexion>();
		Conexion t1_c1 = new Conexion(2, 0.15);
		t1.add(t1_c1);

		Nodo t1_n1 = new Nodo(3, 3, -41.1, -72.1, t1);
		test.add(t1_n1);

		ArrayList<Conexion> t2 = new ArrayList<Conexion>();
		Conexion t2_c1 = new Conexion(2, 0.03);
		t2.add(t2_c1);

		Nodo t2_n1 = new Nodo(5, 3, -41.2, -72.2, t2);
		test.add(t2_n1);*/

             /*       ArrayList<nose.Conexion> t0 = new ArrayList<nose.Conexion>();
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
                    lista_nodos.add(t7_n1);*/

                    //Calculos
                    //indices
                    //Grafo b = new Grafo(lista_nodos.size());

                    //List<nose.Nodo> listax = new ArrayList<nose.Nodo>();

                    //Grafo b = new Grafo(lista_nodos);

                    //nose.direccion hola = b.camino_mas_corto(0, 7);

                    Grafo b = new Grafo(vertices, matriz_adyacencia);
                    nose.direccion hola = b.camino_mas_corto(1, 2);

                    id_test_distancia = (TextView) findViewById(R.id.id_test_distancia);
                    id_test_distancia.setText("distancia: "+hola.getDistancia());

                    id_test_ruta = (TextView) findViewById(R.id.id_test_ruta);
                    id_test_ruta.setText("ruta: "+hola.getRuta());

                    for (Nodo nodo : nodos){
                        if (nodo.getId_nodo() == 3) {
                            List<Conexion> con = nodo.getConexiones();
                            for (Conexion co : con){
                                if (co.getDestino() == 2) {
                                    id_test = (TextView) findViewById(R.id.id_test);
                                    id_test.setText("holo"+nodo.getLongitud_nodo()+co.getDistancia());
                                }
                            }
                        }
                    }
                }else{
                    Log.e("TIPO_MENU", " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Nodo>> call, Throwable t) {
                Log.e("TIPO_MENU", " onFailure: " + t.getMessage());
            }
        });
    }
}
