package com.example.teammatch.network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.PistasRepository;
import com.example.teammatch.activities.MainActivity;
import com.example.teammatch.network.OpenDataService;
import com.example.teammatch.R;
import com.example.teammatch.adapters.PistaAdapter;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.FoafName;
import com.example.teammatch.objects.GeoLat;
import com.example.teammatch.objects.GeoLong;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.objects.SchemaAddressAddressCountry;
import com.example.teammatch.objects.SchemaAddressAddressLocality;
import com.example.teammatch.objects.SchemaAddressStreetAddress;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PistasActivity extends AppCompatActivity implements PistaAdapter.OnListInteractionListener, SearchView.OnQueryTextListener {
    private static final String TAG = "Pistas: ";

    private RecyclerView recyclerView;
    private PistaAdapter mAdapter;
    private PistaAdapter mAdapterResults;
    private RecyclerView.LayoutManager layoutManager;
    private PistasRepository mPistaRepository;
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

        mPistaRepository = PistasRepository.getInstance(TeamMatchDataBase.getInstance(this).getDao(), PistasNetworkDataSource.getInstance());
        mPistaRepository.getCurrentRepos().observe(this, new Observer<List<Pista>>() {
            @Override
            public void onChanged(List<Pista> pistas) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.swap(obtenerBindingDesdeListaPistas(pistas));//Se mete esta llamada aquí porque es una llamada asincrona.
                    }
                });
            }
        });

        mPistaRepository.setPista();

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPistaRepository.doFetchPistas();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });



        //loadPistas();

        recyclerView.setAdapter(mAdapter);

        //BUSCADOR DE PISTAS
        SearchView buscadorPistas = findViewById(R.id.search_pista);
        buscadorPistas.setOnQueryTextListener(this);
    }

  /*  public void loadPistas(){
        // Create a very simple REST adapter which points to the API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://opendata.caceres.es/GetData/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenDataService service = retrofit.create(OpenDataService.class);//Retrofit me crea instancia de interfaz de arriba

        service.cogerPistas().enqueue(new Callback<Pistas>() {
            @Override
            public void onResponse(Call<Pistas> call, Response<Pistas> response) {

                Pistas p = response.body();
                //Meto pìstas en room
                insertarPistasEnBD(p);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.swap(p.getResults().getBindings());//Se mete esta llamada aquí porque es una llamada asincrona.
                    }
                });
            }

            @Override
            public void onFailure(Call<Pistas> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }*/

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
