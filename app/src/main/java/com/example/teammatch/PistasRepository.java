/*
package com.example.teammatch;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.teammatch.network.PistasNetworkDataSource;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.room_db.TeamMatchDAO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PistasRepository {

    private static final String LOG_TAG = PistasRepository.class.getSimpleName();

    // For Singleton instantiation
    private static PistasRepository sInstance;
    private final TeamMatchDAO mTeamMatchDao;
    private final PistasNetworkDataSource mPistasNetworkDataSource;
    private final AppExecutors mExecutors = AppExecutors.getInstance();
    private final MutableLiveData<String> userFilterLiveData = new MutableLiveData<>();
    private final Map<String, Long> lastUpdateTimeMillisMap = new HashMap<>();
    private static final long MIN_TIME_FROM_LAST_FETCH_MILLIS = 30000;

    private PistasRepository(TeamMatchDAO teamMatchDAO, PistasNetworkDataSource pistasNetworkDataSource) {
        mTeamMatchDao = teamMatchDAO;
        mPistasNetworkDataSource = pistasNetworkDataSource;
        // LiveData that fetches repos from network
        LiveData<Pistas> networkData = mPistasNetworkDataSource.getCurrentRepos();
        // observe the network LiveData.
        // If that LiveData changes, update the database.
        networkData.observeForever(newReposFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // Deleting cached repos of user
                if (newReposFromNetwork.length > 0){
                    mRepoDao.deleteReposByUser(newReposFromNetwork[0].getOwner().getLogin());
                }
                // Insert our new repos into local database
                mRepoDao.bulkInsert(Arrays.asList(newReposFromNetwork));
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

    public void setUsername(final String username){
        //Set value to MutableLiveData in order to filter getCurrentRepos LiveData
        userFilterLiveData.setValue(username);//Este LiveData tiene en tod momento una referencia al usuario de la aplicación.
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (isFetchNeeded(username)) {
                doFetchRepos(username);
            }
        });
    }

    public void doFetchRepos(String username){
        Log.d(LOG_TAG, "Fetching Repos from Github");
        AppExecutors.getInstance().diskIO().execute(() -> {
            mRepoDao.deleteReposByUser(username);
            mRepoNetworkDataSource.fetchRepos(username);
            lastUpdateTimeMillisMap.put(username, System.currentTimeMillis());
        });
    }

    */
/**
     * Database related operations
     **//*


    public LiveData<List<Repo>> getCurrentRepos() {
        // Return LiveData from Room. Use Transformation to get owner
        //Ahora devolvemos una transformación.
        //Cogemos LiveData y cada vez que cambie hacemos la transformación.
        return Transformations.switchMap(userFilterLiveData, new Function<String, LiveData<List<Repo>>>() {
            @Override
            public LiveData<List<Repo>> apply(String input) {
                return mRepoDao.getReposByOwner(input);//Input hace referencia al username de arriba.
            }
        });
    }

    */
/**
     * Checks if we have to update the repos data.
     * @return Whether a fetch is needed
     *//*

    private boolean isFetchNeeded(String username) {
        Long lastFetchTimeMillis = lastUpdateTimeMillisMap.get(username);
        lastFetchTimeMillis = lastFetchTimeMillis == null ? 0L : lastFetchTimeMillis;
        long timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis;
        //Implement cache policy: When time has passed or no repos in cache
        return mRepoDao.getNumberReposByUser(username) == 0 || timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS;//Si ocurre 1 de las 2, obtengo los datos de la red haciendo petición.
    }
}
*/
