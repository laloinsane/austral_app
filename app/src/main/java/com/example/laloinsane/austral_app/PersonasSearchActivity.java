package com.example.laloinsane.austral_app;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laloinsane.austral_app.API.APIService;
import com.example.laloinsane.austral_app.Adapters.PersonaAdapter;
import com.example.laloinsane.austral_app.Models.Persona;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonasSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Persona> personas;
    private PersonaAdapter adapter;
    private APIService apiInterface;
    ProgressBar progressBar;
    //TextView search;
    //String[] item;

    private int campus;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_search);

        //Obtencion de los datos del Activity anterior
        Bundle extras = getIntent().getExtras();
        campus = extras.getInt("campus_id_busqueda");
        lat = extras.getDouble("latitud_busqueda");
        lon = extras.getDouble("longitud_busqueda");

        progressBar = findViewById(R.id.progress_personas);
        recyclerView = findViewById(R.id.recyclerView_personas);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fetchContact(campus, "");//sin ninguna palabra de filtrado
        //fetchContact("users", "");
    }

    public void fetchContact(int id_campus,String key){
        apiInterface = ApiClient.getApiClient().create(APIService.class);
        //Call<List<Persona>> call = apiInterface.getPersonas(type, key);
        Call<List<Persona>> call = apiInterface.getPersonas(id_campus, key);

        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                progressBar.setVisibility(View.GONE);
                personas = response.body();
                adapter = new PersonaAdapter(personas, PersonasSearchActivity.this, campus, lat, lon);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PersonasSearchActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /*public void fetchContact(String type, String key){

        apiInterface = ApiClient.getApiClient().create(APIService.class);

        //Call<List<Persona>> call = apiInterface.getPersonas(type, key);
        Call<List<Persona>> call = apiInterface.getPersonas();
        call.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                progressBar.setVisibility(View.GONE);
                personas = response.body();
                adapter = new PersonaAdapter(personas, PersonasSearchActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PersonasSearchActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }*/


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchContact(campus, query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchContact(campus, newText);
                return false;
            }
        });
        return true;
    }
}
