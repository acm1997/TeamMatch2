package com.example.teammatch.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;

import java.util.ArrayList;
import java.util.List;

public class PistasNetworkDataSource {
  private static final String LOG_TAG = PistasNetworkDataSource.class.getSimpleName();

  private static PistasNetworkDataSource sInstance;

  private final MutableLiveData<List<Pista>> mDownloadedPistas;//Almacena pistas.

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

  public LiveData<List<Pista>> getCurrentRepos() {
    return mDownloadedPistas;
  }

  public List<Pista> obtenerPistasDesdeObjetoPistasAPI(List<Binding> b){
    List<Pista> listaPistasBD = new ArrayList<Pista>();
    for(Binding binding : b) {
      Pista pNueva = new Pista(binding.getFoafName().getValue(), binding.getSchemaAddressAddressLocality().getValue(),"",binding.getGeoLong().getValue(),binding.getGeoLat().getValue());
      listaPistasBD.add(pNueva);
    }
    return listaPistasBD;
  }
  /**
   * Gets the newest repos
   */
  public void fetchPistas() {
    Log.d(LOG_TAG, "Fetch pistas started");
    // Get gata from network and pass it to LiveData
    AppExecutors.getInstance().networkIO().execute( new PistasLoaderRunnable(pista -> mDownloadedPistas.postValue(obtenerPistasDesdeObjetoPistasAPI(pista))));
  }
}
