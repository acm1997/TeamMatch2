package com.example.teammatch.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.teammatch.PistasRepository;
import com.example.teammatch.objects.Pista;

import java.util.List;

//MODEL PARA OBTENER LAS PISTAS DEL PISTA REPOSITY (SE ACTUALIZAN).

public class PistaActivityViewModel extends ViewModel {

    private final PistasRepository mRepository;
    private final LiveData<List<Pista>> mPistas;



    public PistaActivityViewModel(PistasRepository repository){
        mRepository = repository;
        mPistas = mRepository.getCurrentRepos();
    }

    public void onRefresh(){
        mRepository.doFetchPistas();
    }

    public LiveData<List<Pista>> getPistas() {
        return mPistas;
    }


}
