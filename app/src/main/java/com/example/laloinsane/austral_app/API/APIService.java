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

    @GET("campus/{id_campus}")
    Call<List<Unidad>> getUnidades(@Path("id_campus") int id_campus);

    @GET("campus/{id_campus}/unidad/{id_unidad}")
    Call<UnidadNodos> getUnidad(@Path("id_campus") int id_campus, @Path("id_unidad") int id_unidad);
}
