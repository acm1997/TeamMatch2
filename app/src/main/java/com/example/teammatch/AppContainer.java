package com.example.teammatch;

import android.content.Context;
import android.util.Log;

import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.EventoViewModelFactory;
import com.example.teammatch.ui.PistaViewModelFactory;

public class  AppContainer {

    private static final String TAG = "AppContainer: ";
    private TeamMatchDataBase database;
    private PistasNetworkDataSource networkDataSource;
    public PistasRepository repositoryPistas;
    public PistaViewModelFactory factory;

    public EventosRepository repositoryEventos;
    public EventoViewModelFactory factoryEventos;

    public EventosRepository repositoryEventosCreados;
    public EventoViewModelFactory factoryEventosCreados;


    public AppContainer(Context context){
        database = TeamMatchDataBase.getInstance(context);
        networkDataSource = PistasNetworkDataSource.getInstance();
        repositoryPistas = PistasRepository.getInstance(database.getDao(), networkDataSource);
        factory = new PistaViewModelFactory(repositoryPistas);

        //Iniciar lo de eventos.
        repositoryEventos = EventosRepository.getInstance(database.getDao());
        factoryEventos = new EventoViewModelFactory(repositoryEventos);

        //Inicar Eventos Creados
        repositoryEventosCreados = EventosRepository.getInstance(database.getDao());
        factoryEventosCreados = new EventoViewModelFactory(repositoryEventosCreados);


    }
    private void log(String msg) {
        try {
            Thread.sleep(500);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
