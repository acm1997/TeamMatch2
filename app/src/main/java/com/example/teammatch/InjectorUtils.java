package com.example.teammatch;

import android.content.Context;

import com.example.teammatch.PistasRepository;
import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.PistaViewModelFactory;


/**
 * Provides static methods to inject the various classes needed for the app
 */
public class InjectorUtils {

    public static PistasRepository provideRepository(Context context) {
        TeamMatchDataBase database = TeamMatchDataBase.getInstance(context.getApplicationContext());
        PistasNetworkDataSource networkDataSource = PistasNetworkDataSource.getInstance();
        return PistasRepository.getInstance(database.getDao(), networkDataSource);
    }

    public static PistaViewModelFactory provideMainActivityViewModelFactory(Context context) {
        PistasRepository repository = provideRepository(context.getApplicationContext());
        return new PistaViewModelFactory(repository);
    }

}