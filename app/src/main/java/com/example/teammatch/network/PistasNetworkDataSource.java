package com.example.teammatch.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.objects.Pistas;

public class PistasNetworkDataSource {
  private static final String LOG_TAG = PistasNetworkDataSource.class.getSimpleName();

  private static PistasNetworkDataSource sInstance;

  private final MutableLiveData<Pistas> mDownloadedPistas;//Almacena pistas.

  private PistasNetworkDataSource() {
    mDownloadedPistas = new MutableLiveData<>();
  }

  public synchronized static PistasNetworkDataSource getInstance() {
    Log.d(LOG_TAG, "Getting the network data source");
    if (sInstance == null) {
      sInstance = new PistasNetworkDataSource();
      Log.d(LOG_TAG, "Made new network data source");
    }
    return sInstance;
  }

  public LiveData<Pistas> getCurrentRepos() {
    return mDownloadedPistas;
  }

  /**
   * Gets the newest repos
   */
  public void fetchPistas() {
    Log.d(LOG_TAG, "Fetch pistas started");
    // Get gata from network and pass it to LiveData
//    AppExecutors.getInstance().networkIO().execute((Runnable) new PistasActivity(pistas -> mDownloadedPistas.postValue(pistas)));
  }
}
