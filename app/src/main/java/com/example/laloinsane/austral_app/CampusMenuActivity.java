package com.example.laloinsane.austral_app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.laloinsane.austral_app.API.APIClient;
import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Adapters.CampusAdapter;
import com.example.laloinsane.austral_app.Models.Campus;
import com.example.laloinsane.austral_app.Models.CampusRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampusMenuActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private APIService api;
    private RecyclerView recyclerView;
    private CampusAdapter listaCampus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Campus");
        progressBar = findViewById(R.id.progress_campus_menu);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_campus_menu);
        listaCampus = new CampusAdapter(this);
        recyclerView.setAdapter(listaCampus);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        obtenerDatos();
    }

    private void obtenerDatos(){
        api = APIClient.getAPIClient().create(APIService.class);
        Call<CampusRespuesta> apiCall = api.getCampus();

        apiCall.enqueue(new Callback<CampusRespuesta>() {
            @Override
            public void onResponse(Call<CampusRespuesta> call, Response<CampusRespuesta> response) {
                progressBar.setVisibility(View.GONE);
                CampusRespuesta apiRespuesta = response.body();
                ArrayList<Campus> datos= apiRespuesta.getCampus();
                listaCampus.addCampus(datos);
            }

            @Override
            public void onFailure(Call<CampusRespuesta> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
