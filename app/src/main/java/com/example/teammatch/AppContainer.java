package com.example.teammatch;

import android.content.Context;

import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.MainViewModelFactory;
import com.example.teammatch.ui.PistaViewModelFactory;

public class AppContainer {

    private TeamMatchDataBase database;
    private PistasNetworkDataSource networkDataSource;
    public PistasRepository repository;
    public PistaViewModelFactory factory;

    public EventosRepository repositoryEventos;
    public MainViewModelFactory factoryEventos;



    public AppContainer(Context context){
        database = TeamMatchDataBase.getInstance(context);
        networkDataSource = PistasNetworkDataSource.getInstance();
        repository = PistasRepository.getInstance(database.getDao(), networkDataSource);
        factory = new PistaViewModelFactory(repository);

        //Iniciar lo de eventos.
        repositoryEventos = EventosRepository.getInstance(database.getDao());
        factoryEventos = new MainViewModelFactory(repositoryEventos);

    }
}
