package com.example.teammatch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.teammatch.EventosRepository;
import com.example.teammatch.ParticipacionRepository;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.ParticipacionUserEvento;

import java.util.List;

public class ParticipacionViewModel extends ViewModel {
    private static final String LOG_TAG = EventosRepository.class.getSimpleName();

    private final ParticipacionRepository mRepository;
    private final LiveData<ParticipacionUserEvento> mParticipacion;
    private long evento,usuario;


    //Constructor
    public ParticipacionViewModel(ParticipacionRepository repository){
        mRepository = repository;
        mParticipacion = mRepository.getCurrentParticipacion();
    }

    public void seteventoUsuario(long evento,long usuario) {
        this.evento = evento;
        this.usuario = usuario;
        mRepository.setEventoUsuario(evento,usuario);

    }

    //Peticion de Eventos a la BD
    public void onRefresh(long userID){
       // mRepository.getCurrentEventosCreados(userID);
    }

    public LiveData<ParticipacionUserEvento> getParticipacion() {
        return mParticipacion;
    }

}
