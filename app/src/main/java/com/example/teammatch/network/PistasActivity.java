package com.example.teammatch.network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.teammatch.AppContainer;
import com.example.teammatch.AppExecutors;
import com.example.teammatch.MyApplication;
import com.example.teammatch.PistasRepository;
import com.example.teammatch.R;
import com.example.teammatch.adapters.PistaAdapter;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.FoafName;
import com.example.teammatch.objects.GeoLat;
import com.example.teammatch.objects.GeoLong;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.objects.SchemaAddressAddressLocality;
import com.example.teammatch.objects.SchemaAddressStreetAddress;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.PistaActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class PistasActivity extends AppCompatActivity implements PistaAdapter.OnListInteractionListener, SearchView.OnQueryTextListener {
    private static final String TAG = "Pistas: ";

    private RecyclerView recyclerView;
    private PistaAdapter mAdapter;
    private PistaAdapter mAdapterResults;
    private RecyclerView.LayoutManager layoutManager;
    private PistasRepository mPistaRepository;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    // private final OnReposLoadedListener mOnReposLoadedListener;

    public PistasActivity() {
    }

  /*  public PistasActivity(OnReposLoadedListener onReposLoadedListener) {
        mOnReposLoadedListener = onReposLoadedListener;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pistas);
        recyclerView = (RecyclerView) findViewById(R.id.idRecyclerPista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PistaAdapter(new ArrayList<Binding>(),this);
        mProgressBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        //MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;

        //viewModel de Pistas
        PistaActivityViewModel mViewModel = new ViewModelProvider(this, appContainer.factory).get(PistaActivityViewModel.class);

        mViewModel.getPistas().observe(this, (List<Pista> pistas) -> {
            mAdapter.swap(obtenerBindingDesdeListaPistas(pistas));
            // Show the repo list or the loading screen based on whether the repos data exists and is loaded
            if (pistas != null && pistas.size() != 0) showReposDataView();
            else showLoading();
        });
        /*mPistaRepository = PistasRepository.getInstance(TeamMatchDataBase.getInstance(this).getDao(), PistasNetworkDataSource.getInstance());
        mPistaRepository.getCurrentPistas().observe(this, new Observer<List<Pista>>() {
            @Override
            public void onChanged(List<Pista> pistas) {
                log("SIZE "+ pistas.size() );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.swap(obtenerBindingDesdeListaPistas(pistas));//Se mete esta llamada aquí porque es una llamada asincrona.
                    }
                });
            }
        });

        mPistaRepository.setPista();*/

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mViewModel::onRefresh);
        mSwipeRefreshLayout.setRefreshing(false);


        recyclerView.setAdapter(mAdapter);

        //BUSCADOR DE PISTAS
        SearchView buscadorPistas = findViewById(R.id.search_pista);
        buscadorPistas.setOnQueryTextListener(this);
    }


    @Override
    public void onListInteraction(Binding b) {
        Intent pistaIntent = new Intent();
        Binding.packageIntent(pistaIntent,b);
        pistaIntent.putExtra(Evento.PISTA,b.getFoafName().getValue());
        pistaIntent.putExtra("latitud", b.getGeoLat().getValue());
        pistaIntent.putExtra("longitud", b.getGeoLong().getValue());
        setResult(RESULT_OK,pistaIntent);
        finish();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.loadBuscador(mAdapter.filtrado(query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    public List<Pista> insertarPistasEnBD(Pistas p){
        List<Pista> listaPistasBD = new ArrayList<Pista>();

        AppExecutors.getInstance().diskIO().execute(() -> {
                TeamMatchDataBase pista_database = TeamMatchDataBase.getInstance(PistasActivity.this);
                for(Binding binding : p.getResults().getBindings()) {
                    Pista pNueva = new Pista(binding.getFoafName().getValue(), binding.getSchemaAddressAddressLocality().getValue(),"",binding.getGeoLong().getValue(),binding.getGeoLat().getValue());
                    listaPistasBD.add(pNueva);
                }

                log("PISTA INSERTADA: " + pista_database.getDao().getPistaById(7).getNombrePista());
            });
        log("PISTA INSERTADA: " + listaPistasBD.get(1).getNombrePista());
        return listaPistasBD;

    }

    private void showReposDataView(){
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        recyclerView.setVisibility(View.VISIBLE);
    }
    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }


    public List<Binding> obtenerBindingDesdeListaPistas(List<Pista> listaPistas){
        List<Binding> listaBinding = new ArrayList<Binding>();
        for(Pista p : listaPistas) {
            Binding binding = new Binding(null,null,null,null,new GeoLong(p.getGeoLongPista()),new FoafName(p.getNombrePista()),new GeoLat(p.getGeoLatPista()), null,new SchemaAddressAddressLocality(p.getCiudadPista()),new SchemaAddressStreetAddress(p.getCallePista()));
            listaBinding.add(binding);
        }
        return listaBinding;
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
