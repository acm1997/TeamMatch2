package com.example.teammatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.teammatch.AppContainer;
import com.example.teammatch.AppExecutors;
import com.example.teammatch.EventosRepository;
import com.example.teammatch.MyApplication;
import com.example.teammatch.R;
import com.example.teammatch.adapters.EventAdapter;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.example.teammatch.ui.EventosActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "MainActivityData.txt";
    public static final int ADD_EVENTO_REQUEST = 0;
    public static final int REGISTER_REQUEST = 1;
    public static final int ADD_EQUIPO_REQUEST = 2;
    public static final int SELECCIONAR_PISTA_EVENTO = 3;
    public static final int GO_DETAILS_ITEM = 4;
    public static final int SELECCIONAR_PISTA_EDITAR_EVENTO = 5;
    private static final String TAG = "MAIN_ACTIVITY";


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private EventAdapter mAdapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SharedPreferences preferences;

    private EventosRepository mEventosRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fabNuevoEvento);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);

        //Solo se muestra el boton si el usuario se ha logeado
        if(usuario_id > 0 && name != null && email != null && password != null){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CrearEventoActivity.class);
                    startActivityForResult(intent, ADD_EVENTO_REQUEST);
                }
            });
        } else{
            fab.setVisibility(View.INVISIBLE);
        }

        //Inicio variable bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Pongo seleccion de "Inicio".
        bottomNavigationView.setSelectedItemId(R.id.ic_home);
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


        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mProgressBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        AppContainer appContainer = ((MyApplication) getApplication()).appContainer;
        EventosActivityViewModel mViewModel = new ViewModelProvider(this, appContainer.factoryEventos).get(EventosActivityViewModel.class);
        mViewModel.getEventos().observe(this, eventos -> {
            mAdapter.load(eventos);
            if (eventos != null ) showReposDataView();
            else showLoading();
        });

        /*mSwipeRefreshLayout.setOnRefreshListener(mViewModel::onRefresh);
        mSwipeRefreshLayout.setRefreshing(false);*/

        mAdapter = new EventAdapter(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {
                Intent eventoIntent = new Intent(MainActivity.this, EventDetailsActivity.class);
                log("EVENTO DETALLADO: "+ item.toString()+ " con fecha: "+ item.getFecha());
                Evento.packageIntent(eventoIntent,item.getNombre(),item.FORMAT.format(item.getFecha()),item.getParticipantes(),item.getDescripcion(),item.getDeporte(),item.getPista(),item.getUserCreatorId(), item.getLatitud(),item.getLongitud(), item.getEventoPhotoPath());
                eventoIntent.putExtra("ID", item.getId());
                log("EVENTO DETALLADO despues package: "+ eventoIntent.getStringExtra("nombre") + " con fecha: "+eventoIntent.getStringExtra("fecha"));

                startActivityForResult(eventoIntent, GO_DETAILS_ITEM);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private void showReposDataView(){
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.VISIBLE);
        log("EVENTO HOLA: ");
    }
    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        log("EVENTO Adios: ");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EVENTO_REQUEST && resultCode == RESULT_OK){
            Evento EventoItem = new Evento(data);
            AppExecutors.getInstance().diskIO().execute(() -> {
                //añadir evento en la BD
                TeamMatchDataBase evento_dataBase = TeamMatchDataBase.getInstance(MainActivity.this);
                long id_evento = evento_dataBase.getDao().insertEvento(EventoItem);

                //actualizar evento
                EventoItem.setId(id_evento);

                //insertar evento en la lista
                runOnUiThread(() -> mAdapter.add(EventoItem));
            });
        }

        if(requestCode == REGISTER_REQUEST && resultCode == RESULT_OK){
            User UserItem = new User(data);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    //Añadir usuario a la BD
                    TeamMatchDataBase usuario_database = TeamMatchDataBase.getInstance(MainActivity.this);
                    long id_user = usuario_database.getDao().insertUser(UserItem);

                    //Actualizar user
                    UserItem.setId(id_user);

                    Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (requestCode == GO_DETAILS_ITEM && resultCode == RESULT_OK){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    log("HE ENTRADO");
                    loadItems();

                }
            });
        }
    }

    //ESTE ONRESUME CON EL VIEW MODEL YA NO VA A HACER FALTA PORQUE CARGO LOS EVENTOS DESDE EL OBSERVER AL LIVEDATA.
/*    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

      //  if (mAdapter.getItemCount() == 0)
            loadItems();
    }*/

/*    @Override
    protected void onPause() {
        super.onPause();
        saveItems();
    }
    @Override
    protected void onDestroy() {
        TeamMatchDataBase.getInstance(this).close();
        super.onDestroy();
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present
         getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if(id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_text_login){
            preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
            Long usuario_id = preferences.getLong("usuario_id", 0);
            String name = preferences.getString("username", null);
            String email = preferences.getString("email", null);
            String password = preferences.getString("password", null);
            if(usuario_id == 0 && name == null && email == null && password == null){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Ya has iniciado sesión en la aplicación", Toast.LENGTH_LONG).show();
            }
        } else if(id == R.id.action_text_share){
            compartirApp();
        }
        return super.onOptionsItemSelected(item);
    }


    // Load stored Eventos
    private void loadItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> eventos = TeamMatchDataBase.getInstance(MainActivity.this).getDao().getAllEventos();
          //      log("ID BD: "+ eventos.get(0).getId());
                runOnUiThread(() -> mAdapter.load(eventos));//USAREMOS ESTA LLAMADA EN EL OBSERVER PARA CARGAR EVENTOS.
            }
        });
    }


    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getItemCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private void compartirApp(){
        try{
            Intent ishare = new Intent(Intent.ACTION_SEND);
            ishare.setType("text/plain");
            ishare.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String aux = "Descarga la aplicación Team Match\n";
            aux += "https://drive.google.com/drive/folders/1GE8CY6bNBtJwEDq6wiZkCdMmB7sdpYfu?usp=sharing";
            ishare.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(ishare);
        }catch (Exception e){ }
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