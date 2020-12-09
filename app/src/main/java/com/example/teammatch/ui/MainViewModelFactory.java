package com.example.teammatch.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.teammatch.EventosRepository;
import com.example.teammatch.PistasRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final EventosRepository mRepository;

    public MainViewModelFactory(EventosRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new MainActivityViewModel(mRepository);
    }
}
