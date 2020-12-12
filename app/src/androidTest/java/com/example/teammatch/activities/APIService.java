package com.example.teammatch.activities;

import android.content.Context;

import com.example.teammatch.network.OpenDataService;
import com.example.teammatch.network.PistasLoaderRunnable;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;

import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertTrue;

public class APIService {

    @Test
    public void crearLlamadaAPITest() throws IOException{

        String nombrePrimeraPista = "Espacio deportivo instalación: Ciudad Deportiva De La Junta De Extremadura";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://opendata.caceres.es/GetData/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenDataService service = retrofit.create(OpenDataService.class);//Retrofit me crea instancia de interfaz de arriba
        Call<Pistas> call = service.cogerPistas();

        Response<Pistas> response = call.execute();
        Pistas p = response.body();

        assertTrue(response != null);
        assertTrue(p.getResults().getBindings().get(0).getFoafName().getValue().equals(nombrePrimeraPista));

    }
}
