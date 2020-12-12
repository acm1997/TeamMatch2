package com.example.teammatch;

import android.content.Context;
import android.util.Log;

import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.EquipoViewModelFactory;
import com.example.teammatch.ui.EventoCreadoViewModelFactory;
import com.example.teammatch.ui.EventoParticipacionViewModelFactory;
import com.example.teammatch.ui.EventoViewModelFactory;
import com.example.teammatch.ui.ParticipacionViewModelFactory;
import com.example.teammatch.ui.PistaViewModelFactory;

public class  AppContainer {

    private TeamMatchDataBase database;
    private PistasNetworkDataSource networkDataSource;
    public PistasRepository repositoryPistas;
    public PistaViewModelFactory factory;

    public EventosRepository repositoryEventos;
    public EventoViewModelFactory factoryEventos;

    public EquiposRepository repositoryEquipos;
    public EquipoViewModelFactory factoryEquipos;

    public ParticipacionRepository repositoryParticipacion;
    public ParticipacionViewModelFactory factoryParticipacion;


    private static final String TAG = "AppContainer: ";
    //Eventos creados
    public EventosRepository repositoryEventosCreados;
    public EventoCreadoViewModelFactory factoryEventosCreados;
    //Eventos participacion
    public EventosRepository repositoryEventosParticipacion;
    public EventoParticipacionViewModelFactory factoryEventosParticipacion;


    public AppContainer(Context context){
        database = TeamMatchDataBase.getInstance(context);
        networkDataSource = PistasNetworkDataSource.getInstance();
        repositoryPistas = PistasRepository.getInstance(database.getDao(), networkDataSource);
        factory = new PistaViewModelFactory(repositoryPistas);

        //Iniciar lo de eventos.
        repositoryEventos = EventosRepository.getInstance(database.getDao());
        factoryEventos = new EventoViewModelFactory(repositoryEventos);

        //Iniciciamos equipos.
        repositoryEquipos = EquiposRepository.getInstance(database.getDao());
        factoryEquipos = new EquipoViewModelFactory(repositoryEquipos);



        //Inicar Eventos Creados
        repositoryEventosCreados = EventosRepository.getInstance(database.getDao());
        factoryEventosCreados = new EventoCreadoViewModelFactory(repositoryEventosCreados);

        //Inicar Eventos Participacion
        repositoryEventosParticipacion = EventosRepository.getInstance(database.getDao());
        factoryEventosParticipacion = new EventoParticipacionViewModelFactory(repositoryEventosParticipacion);

        //Inicar Participacion
        repositoryParticipacion = ParticipacionRepository.getInstance(database.getDao());
        factoryParticipacion = new ParticipacionViewModelFactory(repositoryParticipacion);

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
