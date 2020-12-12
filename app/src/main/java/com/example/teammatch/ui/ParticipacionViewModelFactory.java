package com.example.teammatch.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.teammatch.EventosRepository;
import com.example.teammatch.ParticipacionRepository;

public class ParticipacionViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final ParticipacionRepository mRepository;

    public ParticipacionViewModelFactory(ParticipacionRepository repository){
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new ParticipacionViewModel(mRepository);
    }
}
