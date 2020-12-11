package com.example.teammatch.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.teammatch.EquiposRepository;
import com.example.teammatch.EventosRepository;

public class EquipoViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final EquiposRepository mRepository;

    public EquipoViewModelFactory(EquiposRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EquiposActivityViewModel(mRepository);
    }
}
