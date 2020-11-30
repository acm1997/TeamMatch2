package com.example.teammatch.network;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.objects.Pistas;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class PistasLoaderRunnable implements Runnable{

    private final InputStream mInFile;
    private final OnReposLoadedListener mOnReposLoadedListener;

    public PistasLoaderRunnable(InputStream inFile, OnReposLoadedListener onReposLoadedListener){
        mInFile = inFile;
        mOnReposLoadedListener = onReposLoadedListener;
    }

    @Override
    public void run() {
        // Obtención de los datos a partir del InputStream
        JsonReader reader = new JsonReader(new InputStreamReader(mInFile));
        // Parse JsonReader into list of Repo using Gson
        Pistas pistas = new Gson().fromJson(reader, Pistas.class);
/*                for(Pistum p : pistas){
                    try{
                        Thread.sleep(50);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }*/
        // Llamada al Listener con los datos obtenidos
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
               mOnReposLoadedListener.onReposLoaded(pistas);
            }
        });


    }
}

