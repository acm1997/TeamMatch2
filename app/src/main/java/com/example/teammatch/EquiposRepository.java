package com.example.teammatch;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.teammatch.objects.Equipo;
import com.example.teammatch.room_db.TeamMatchDAO;

import java.util.List;

public class EquiposRepository {
    private static final String LOG_TAG = "";
    private static EquiposRepository sInstance;
    private final TeamMatchDAO mTeamMatchDao;
    private final AppExecutors mExecutors = AppExecutors.getInstance();

    public EquiposRepository(TeamMatchDAO mTeamMatchDao) {
        this.mTeamMatchDao = mTeamMatchDao;
    }

    public synchronized static EquiposRepository getInstance(TeamMatchDAO dao) {
        Log.d(LOG_TAG, "Getting the repository of equipo");
        if (sInstance == null) {
            sInstance = new EquiposRepository(dao);
            Log.d(LOG_TAG, "Made new repository of equipo");
        }
        return sInstance;
    }

    public LiveData<List<Equipo>> getCurrentEventos() {
        // Return LiveData from Room. Use Transformation to get owner
        //Ahora devolvemos una transformación.
        //Cogemos LiveData
        return mTeamMatchDao.getLiveDataAllEquipos();
    }
}
