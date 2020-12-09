package com.example.teammatch.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.teammatch.PistasRepository;

public class PistaViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final PistasRepository mRepository;
    
    public PistaViewModelFactory(PistasRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PistaActivityViewModel(mRepository);
    }
}
