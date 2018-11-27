package com.example.laloinsane.austral_app.API;

import com.example.laloinsane.austral_app.Models.Entidad;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("entidades")
    Call<List<Entidad>> getENTIDADES();
}
