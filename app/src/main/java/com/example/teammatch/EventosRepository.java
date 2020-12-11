package com.example.teammatch;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.room_db.TeamMatchDAO;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EventosRepository {

    private static final String LOG_TAG = "";
    private static EventosRepository sInstance;
    private final TeamMatchDAO mTeamMatchDao;
    private final AppExecutors mExecutors = AppExecutors.getInstance();

    public EventosRepository(TeamMatchDAO mTeamMatchDao) {
        this.mTeamMatchDao = mTeamMatchDao;
    }

    public synchronized static EventosRepository getInstance(TeamMatchDAO dao) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new EventosRepository(dao);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public LiveData<List<Evento>> getCurrentEventos() {
        // Return LiveData from Room. Use Transformation to get owner
        //Ahora devolvemos una transformación.
        //Cogemos LiveData
        return mTeamMatchDao.getLiveDataAllEventos();
    }

    public LiveData<List<Evento>> getCurrentEventosCreados(long userID){
        return mTeamMatchDao.getAllEventosByUserId_LiveData(userID);
    }

}
