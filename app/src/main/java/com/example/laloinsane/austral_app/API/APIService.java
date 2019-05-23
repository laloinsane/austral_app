package com.example.laloinsane.austral_app.API;

import com.example.laloinsane.austral_app.Models.CampusRespuesta;
import com.example.laloinsane.austral_app.Models.Unidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("campus")
    Call<CampusRespuesta> getCampus();

    @GET("unidades")
    Call<List<Unidad>> getUNIDADES();
}
