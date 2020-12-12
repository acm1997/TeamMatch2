package com.example.teammatch;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.ParticipacionUserEvento;
import com.example.teammatch.room_db.TeamMatchDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipacionRepository {

    private static final String LOG_TAG = "";
    private static ParticipacionRepository sInstance;
    private final TeamMatchDAO mTeamMatchDao;
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    private final MutableLiveData<EventoUsuario> EventoUsuarioLiveData = new MutableLiveData<>();

    public ParticipacionRepository(TeamMatchDAO mTeamMatchDao) {
        this.mTeamMatchDao = mTeamMatchDao;
    }

    public synchronized static ParticipacionRepository getInstance(TeamMatchDAO dao) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new ParticipacionRepository(dao);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }

    public void setEventoUsuario(final long evento, final long usuario){
        EventoUsuario eu= new EventoUsuario(evento,usuario);
        EventoUsuarioLiveData.setValue(eu);
    }


    /**
     * Database related operations
     **/

    public LiveData<ParticipacionUserEvento> getCurrentParticipacion() {
        return Transformations.switchMap(EventoUsuarioLiveData,
                eventoUsuario -> mTeamMatchDao.getParticipacion_LiveData(eventoUsuario.getEvento(), eventoUsuario.getUsuario())); }

    //CLASE
    private class EventoUsuario{
        public Long evento;
        public Long usuario;

        public EventoUsuario(Long evento, Long usuario) {
            this.evento = evento;
            this.usuario = usuario;
        }

        public Long getEvento() {
            return evento;
        }

        public void setEvento(Long evento) {
            this.evento = evento;
        }

        public Long getUsuario() {
            return usuario;
        }

        public void setUsuario(Long usuario) {
            this.usuario = usuario;
        }
    }

}
