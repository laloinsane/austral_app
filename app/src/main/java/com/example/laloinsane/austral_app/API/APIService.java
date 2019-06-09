package com.example.laloinsane.austral_app.API;

import com.example.laloinsane.austral_app.Models.CampusRespuesta;
import com.example.laloinsane.austral_app.Models.Nodo;
import com.example.laloinsane.austral_app.Models.Unidad;
import com.example.laloinsane.austral_app.Models.UnidadNodos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {
    @GET("campus")
    Call<CampusRespuesta> getCampus();


    //@GET("campus/{id}")
    @GET("campustest/{id}")
    Call<List<Unidad>> getUnidades(@Path("id") int id);

    @GET("unidades")
    Call<List<Unidad>> getUNIDADES();

    @GET("nodos/{id}")
    Call<List<Nodo>> getNodos(@Path("id") int id);

    //@GET("campus/{id}")
    //@GET("unidad/{id}")
    //Call<Unidad> getUnidad(@Path("id") int id);
    @GET("unidad/{id}&{id_campus}")
    Call<UnidadNodos> getUnidad(@Path("id") int id, @Path("id_campus") int id_campus);
}
