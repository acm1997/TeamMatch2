package com.example.teammatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.teammatch.AppContainer;
import com.example.teammatch.AppExecutors;
import com.example.teammatch.EquiposRepository;
import com.example.teammatch.EventosRepository;
import com.example.teammatch.MyApplication;
import com.example.teammatch.R;
import com.example.teammatch.adapters.EquipoAdapter;
import com.example.teammatch.objects.Equipo;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.EquiposActivityViewModel;
import com.example.teammatch.ui.EventosActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.example.teammatch.activities.MainActivity.ADD_EQUIPO_REQUEST;

public class EquiposActivity extends AppCompatActivity {

    private static final String TAG = "EQUIPO ACTIVITY: ";
    private RecyclerView mRecyclerViewEquipos;
    private RecyclerView.LayoutManager mLayoutManagerEquipos;
    private EquipoAdapter mAdapaterEquipos;

    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SharedPreferences preferences;
    private EquiposRepository mEsquiposRepository;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);

        //Solo se muestra el boton si el usuario se ha logeado
        FloatingActionButton fab = findViewById(R.id.fabNuevoEquipo);
        if(usuario_id > 0 && name != null && email != null && password != null){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(EquiposActivity.this, CrearEquipoActivity.class);
                startActivityForResult(intent, ADD_EQUIPO_REQUEST);
            });
        } else {
            fab.setVisibility(View.INVISIBLE);
        }

        //Inicio variable bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Pongo seleccion de "Inicio".
        bottomNavigationView.setSelectedItemId(R.id.ic_equipos);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.ic_usuario:
                    startActivity(new Intent(getApplicationContext(), MyProfileActivity.class ));
                    overridePendingTransition(0,0);
                    return true;

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

        mRecyclerViewEquipos = findViewById(R.id.recyclerView_equipos);
        mRecyclerViewEquipos.setHasFixedSize(true);

        mLayoutManagerEquipos = new LinearLayoutManager(this);
        mRecyclerViewEquipos.setLayoutManager(mLayoutManagerEquipos);

        mProgressBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        //MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        EquiposActivityViewModel mViewModel = new ViewModelProvider(this, appContainer.factoryEquipos).get(EquiposActivityViewModel.class);
        mViewModel.getmEquipos().observe(this, equipos -> {
            mAdapaterEquipos.load(equipos);
            if (equipos != null ) showReposDataView();
            else showLoading();
        });



        mAdapaterEquipos = new EquipoAdapter(item -> {
            Snackbar.make(mRecyclerViewEquipos, "Equipo" +  item.getNombre() + "clicked", Snackbar.LENGTH_SHORT).show(); //TODO enviar a ver equipo
        });
        mRecyclerViewEquipos.setAdapter(mAdapaterEquipos);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EQUIPO_REQUEST && resultCode == RESULT_OK){
            Equipo equipoItem = new Equipo(data);
            AppExecutors.getInstance().diskIO().execute(() -> {
                TeamMatchDataBase equipoDataBase = TeamMatchDataBase.getInstance(EquiposActivity.this);
                long idEquipo = equipoDataBase.getDao().insertEquipo(equipoItem);

                equipoItem.setId(idEquipo);

                runOnUiThread(() -> mAdapaterEquipos.add(equipoItem));
            });
        }
    }
    private void showReposDataView(){
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerViewEquipos.setVisibility(View.VISIBLE);
        log("EQUIPO HOLA: ");
    }
    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerViewEquipos.setVisibility(View.INVISIBLE);
        log("EQUIPO Adios: ");
    }

    //YA NO HACEN FALTA ESTOS METODOS DEL ACTIVITY.

  /*  @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

        if (mAdapaterEquipos.getItemCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO algo habrá que hacer aquí
    }

    @Override
    protected void onDestroy() {
        TeamMatchDataBase.getInstance(this).close();
        super.onDestroy();
    }*/

/*    // Load stored Equipos
    private void loadItems() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Equipo> equipos = TeamMatchDataBase.getInstance(EquiposActivity.this).getDao().getAllEquipos();
            runOnUiThread(() -> mAdapaterEquipos.load(equipos));
        });
    }*/

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
