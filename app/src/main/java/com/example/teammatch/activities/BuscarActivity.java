package com.example.teammatch.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.adapters.EventAdapter;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BuscarActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private RecyclerView listaEventos;
    private RecyclerView.LayoutManager mlayoutManager;
    private EventAdapter mAdapter;

    private SearchView buscador;
    private Button btn_futbol;
    private Button btn_baloncesto;
    private Button btn_voleibol;
    private Button btn_tenis;

    public static final int GO_DETAILS_ITEM = 0;

    private SharedPreferences preferences;

    private static final String TAG = "BUSCAR_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        btn_futbol = findViewById(R.id.btn_searchfutbol);
        btn_baloncesto = findViewById(R.id.btn_searchbaloncesto);
        btn_voleibol = findViewById(R.id.btn_searchvoleibol);
        btn_tenis = findViewById(R.id.btn_searchtenis);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);

        //Inicio variable bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Pongo seleccion de "Inicio".
        bottomNavigationView.setSelectedItemId(R.id.ic_buscar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.ic_usuario:
                    if (usuario_id > 0 && name != null && email != null && password != null) {
                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "No estas registrado en la aplicación", Toast.LENGTH_LONG).show();

                        return false;
                    }


                case R.id.ic_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class ));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.ic_buscar:
                    startActivity(new Intent(getApplicationContext(), BuscarActivity.class ));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.ic_equipos:
                    startActivity(new Intent(getApplicationContext(), EquiposActivity.class ));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        loadItems();

        listaEventos = findViewById(R.id.listaEventos);

        listaEventos.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(this);
        listaEventos.setLayoutManager(mlayoutManager);

        mAdapter = new EventAdapter(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {
                Intent eventoIntent = new Intent(BuscarActivity.this, EventDetailsActivity.class);
                Evento.packageIntent(eventoIntent,item.getNombre(),item.FORMAT.format(item.getFecha()),item.getParticipantes(),item.getDescripcion(),item.getDeporte(),item.getPista(),item.getUserCreatorId(), item.getLatitud(),item.getLongitud(), item.getEventoPhotoPath());
                startActivityForResult(eventoIntent, GO_DETAILS_ITEM);            }
        });

        listaEventos.setAdapter(mAdapter);

        // BUSCADOR
        buscador = findViewById(R.id.svSearch);
        buscador.setOnQueryTextListener(this);

        //BUSCADORES POR CATEGORÍA
        btn_futbol.setOnClickListener(v -> {
            String categoria = "FUTBOL";
            log("Buscamos por cat futbol");
            onQueryCatSubmit(categoria);
        });

        btn_baloncesto.setOnClickListener(v -> {
            String categoria = "BALONCESTO";
            log("Buscamos por cat basket");
            onQueryCatSubmit(categoria);
        });

        btn_voleibol.setOnClickListener(v -> {
            String categoria = "VOLEIBOL";
            log("Buscamos por cat voley");
            onQueryCatSubmit(categoria);
        });

        btn_tenis.setOnClickListener(v -> {
            String categoria = "TENIS";
            log("Buscamos por cat tenis");
            onQueryCatSubmit(categoria);
        });
    }

    // Load stored Eventos
    private void loadItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> eventos = TeamMatchDataBase.getInstance(BuscarActivity.this).getDao().getAllEventos();
                runOnUiThread(() -> mAdapter.load(eventos));
            }
        });
    }

    //BUSCADOR
    @Override
    public boolean onQueryTextSubmit(String query) {

       /* Toast.makeText(BuscarActivity.this,"Our word : "+query,Toast.LENGTH_SHORT).show();
        mAdapter.loadBuscardor(mAdapter.filtrado(query));*/

        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.loadBuscador(mAdapter.filtrado(newText));
       /* if(newText.isEmpty()){
            loadItems();
        }else{

        }*/
        return false;
    }

    public boolean onQueryCatSubmit(String query) {

     //   Toast.makeText(BuscarActivity.this,"Our word : "+query,Toast.LENGTH_SHORT).show();
        mAdapter.loadBuscador(mAdapter.filtradoporCategoria(query));
        return false;
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
