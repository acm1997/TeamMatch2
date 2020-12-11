package com.example.teammatch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.teammatch.EquiposRepository;
import com.example.teammatch.EventosRepository;
import com.example.teammatch.activities.EquiposActivity;
import com.example.teammatch.objects.Equipo;

import java.util.List;

public class EquiposActivityViewModel extends ViewModel {


    private final EquiposRepository mRepository;
    private final LiveData<List<Equipo>> mEquipos;



    //Constructor
    public EquiposActivityViewModel(EquiposRepository repository){
        mRepository = repository;
        mEquipos = mRepository.getCurrentEventos();
    }

    //Peticion de Eventos a la BD
    public void onRefresh(){
        mRepository.getCurrentEventos();
    }

    //Devuelvo el LiveData de todos los equipos
    public LiveData<List<Equipo>> getmEquipos() {
        return mEquipos;
    }


}
