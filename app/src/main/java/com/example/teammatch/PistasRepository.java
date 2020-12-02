package com.example.teammatch;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.teammatch.network.PistasActivity;
import com.example.teammatch.network.PistasLoaderRunnable;
import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PistasRepository {

    private static final String LOG_TAG = PistasRepository.class.getSimpleName();
    private static final String TAG = "Pistas: ";

    // For Singleton instantiation
    private static PistasRepository sInstance;
    private final TeamMatchDAO mTeamMatchDao;
    private final PistasNetworkDataSource mPistasNetworkDataSource;//Carga en liveData pistas.
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    private final Map<String, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 30000;

    private PistasRepository(TeamMatchDAO teamMatchDAO, PistasNetworkDataSource pistasNetworkDataSource) {
        mTeamMatchDao = teamMatchDAO;
        mPistasNetworkDataSource = pistasNetworkDataSource;
        // LiveData that fetches bildings from network. Cargo pistas.
        LiveData<List<Pista>> networkData = mPistasNetworkDataSource.getCurrentRepos();
        // observe the network LiveData que me llega de pistas.
        // If that LiveData changes, update the database.
        networkData.observeForever(newPistasFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                //Aquí insertar pistas.
//                List<Pista> listaPista = obtenerPistasDesdeObjetoPistasAPI(networkData.getValue());
                // Deleting cached repos of user
                if (newPistasFromNetwork != null){
                    mTeamMatchDao.deleteAllPistas();
                }
                // Insert our new repos into local database

                mTeamMatchDao.bulkInsert(networkData.getValue());
                Log.d(LOG_TAG, "New values inserted in Room");
            });
        });
    }

    public synchronized static PistasRepository getInstance(TeamMatchDAO dao, PistasNetworkDataSource nds) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            sInstance = new PistasRepository(dao, nds);
            Log.d(LOG_TAG, "Made new repository");
        }
        return sInstance;
    }


    public LiveData<List<Pista>> getCurrentRepos() {
        // Return LiveData from Room. Use Transformation to get owner
        //Ahora devolvemos una transformación.
        //Cogemos LiveData
        return mTeamMatchDao.getLiveDataPistas();
    }

    //Hacer nueva petición de pistas
    public void doFetchPistas(){
        Log.d(LOG_TAG, "Fetching Pistas of OpenData");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mTeamMatchDao.deleteAllPistas();
            mPistasNetworkDataSource.fetchPistas();
            lastUpdateTimeMillisMap.put("pista", System.currentTimeMillis());
        });
    }
    /* * Checks if we have to update the repos data.
     * @return Whether a fetch is needed*/

    private boolean isFetchNeeded() {
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get("pista");
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        //Implement cache policy: When time has passed or no repos in cache
        return mTeamMatchDao.getNumberPistas() == 0 || timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;//Si ocurre 1 de las 2, obtengo los datos de la red haciendo petición.
    }

    public void setPista(){
        //Set value to MutableLiveData in order to filter getCurrentRepos LiveData
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (isFetchNeeded()) {
                doFetchPistas();
            }
        });
    }

    public List<Pista> obtenerPistasDesdeObjetoPistasAPI(Pistas p){
        List<Pista> listaPistasBD = new ArrayList<Pista>();
            for(Binding binding : p.getResults().getBindings()) {
                Pista pNueva = new Pista(binding.getFoafName().getValue(), binding.getSchemaAddressAddressLocality().getValue(),"",binding.getGeoLong().getValue(),binding.getGeoLat().getValue());
                listaPistasBD.add(pNueva);
            }
        log("PISTA INSERTADA: " + listaPistasBD.get(1).getNombrePista());
        return listaPistasBD;
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
