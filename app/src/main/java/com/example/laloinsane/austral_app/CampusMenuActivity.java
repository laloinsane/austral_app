package com.example.laloinsane.austral_app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Adapters.CampusAdapter;
import com.example.laloinsane.austral_app.Models.Campus;
import com.example.laloinsane.austral_app.Models.CampusRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CampusMenuActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private CampusAdapter lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Campus");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_campus_menu);
        lista = new CampusAdapter(this);
        recyclerView.setAdapter(lista);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit.Builder()
                .baseUrl("base_url")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos();
    }

    private void obtenerDatos(){
        APIService service = retrofit.create(APIService.class);
        Call<CampusRespuesta> apiRespuestaCall = service.getCampus();

        apiRespuestaCall.enqueue(new Callback<CampusRespuesta>() {
            @Override
            public void onResponse(Call<CampusRespuesta> call, Response<CampusRespuesta> response) {
                if(response.isSuccessful()){
                    CampusRespuesta apiRespuesta = response.body();
                    ArrayList<Campus> datos= apiRespuesta.getCampus();
                    lista.addCampus(datos);
                }else{
                    Log.e("TIPO_MENU", " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CampusRespuesta> call, Throwable t) {
                Log.e("TIPO_MENU", " onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
