package com.example.teammatch;

import com.example.teammatch.objects.Pistas;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OpenDataService {
    @GET("GetData?dataset=om:EspacioDeportivo&format=json")
    Call<Pistas> cogerPistas();
}