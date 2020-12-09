package com.example.teammatch.network;

import android.util.Log;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PistasLoaderRunnable implements Runnable{
    private static final String TAG = "Pistas: ";


    private final OnReposLoadedListener mOnReposLoadedListener;

    public PistasLoaderRunnable(OnReposLoadedListener onReposLoadedListener){

        mOnReposLoadedListener = onReposLoadedListener;
    }

    @Override
    public void run() {

        // Create a very simple REST adapter which points to the API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://opendata.caceres.es/GetData/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenDataService service = retrofit.create(OpenDataService.class);//Retrofit me crea instancia de interfaz de arriba
        Call<Pistas> call = service.cogerPistas();
        try {
            Response<Pistas> response = call.execute();
            Pistas p = response.body();
            AppExecutors.getInstance().mainThread().execute(() -> mOnReposLoadedListener.onReposLoaded(p.getResults().getBindings()));
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

}

