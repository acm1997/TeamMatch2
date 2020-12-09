package com.example.teammatch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.teammatch.EventosRepository;
import com.example.teammatch.objects.Evento;

import java.util.List;

public class  EventosActivityViewModel extends ViewModel {

    private final EventosRepository mRepository;
    private final LiveData<List<Evento>> mEventos;



    //Constructor
    public EventosActivityViewModel(EventosRepository repository){
        mRepository = repository;
        mEventos = mRepository.getCurrentEventos();
    }

    //Peticion de Eventos a la BD
    public void onRefresh(){
        mRepository.getCurrentEventos();
    }

    //Devuelvo el LiveData de todos los eventos
    public LiveData<List<Evento>> getEventos() {
        return mEventos;
    }
}
