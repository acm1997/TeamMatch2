package com.example.teammatch.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.teammatch.EventosRepository;

public class EventoParticipacionViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final EventosRepository mRepository;

    public EventoParticipacionViewModelFactory(EventosRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new EventosParticipacionFragmentViewModel(mRepository);
    }
}
