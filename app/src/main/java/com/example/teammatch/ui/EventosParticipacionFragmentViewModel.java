package com.example.teammatch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.teammatch.EventosRepository;
import com.example.teammatch.objects.Evento;

import java.util.List;

public class EventosParticipacionFragmentViewModel extends ViewModel {
    private static final String LOG_TAG = EventosRepository.class.getSimpleName();

    private final EventosRepository mRepository;
    private final LiveData<List<Evento>> mEventosCreados;
    private long userID;


    //Constructor
    public EventosParticipacionFragmentViewModel(EventosRepository repository){
        mRepository = repository;
        mEventosCreados = mRepository.getCurrentEventosParticipacion();
    }

    public void setUserID(long userID) {
        this.userID = userID;
        mRepository.setUsername(userID);

    }

    //Peticion de Eventos a la BD
    public void onRefresh(long userID){
       // mRepository.getCurrentEventosCreados(userID);
    }

    public LiveData<List<Evento>> getEventosParticipacion() {
        return mEventosCreados;
    }

}
