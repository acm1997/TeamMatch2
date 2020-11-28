package com.example.teammatch.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.adapters.EventAdapter;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "MY_PROFILE_ACTIVITY";

    private TextView tname;
    private Button btn_EditP;
    private ImageView img;

    //Mis eventos
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mlayoutManager;
    private EventAdapter mAdapter;

    //Eventos en los que participo
    private RecyclerView mRecyclerView2;
    private RecyclerView.LayoutManager mlayoutManager2;
    private EventAdapter mAdapter2;

    private SharedPreferences preferences;

    public static final int EDIT_PROFILE_REQUEST = 0;
    public static final int GO_LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        tname = findViewById(R.id.tVUsername);
        img = findViewById(R.id.imageView_imgprofile);

        if (ContextCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MyProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);

        if(usuario_id > 0 && name != null && email != null && password != null){
            tname.setText(name);
        }

        btn_EditP = findViewById(R.id.bEditP);

        btn_EditP.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, EditUserActivity.class);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });

        //Inicio variable bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Pongo seleccion de "Inicio".
        bottomNavigationView.setSelectedItemId(R.id.ic_usuario);
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

        mRecyclerView = findViewById(R.id.my_recycler_view_EventosUser);

        mRecyclerView.setHasFixedSize(true);

        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);

        mAdapter = new EventAdapter(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {
                Intent eventoIntent = new Intent(MyProfileActivity.this, EventDetailsActivity.class);
                Evento.packageIntent(eventoIntent,item.getNombre(),item.getFecha().toString(),item.getParticipantes(),item.getDescripcion(),item.getDeporte(),item.getPista(),item.getUserCreatorId(), item.getLatitud(),item.getLongitud());
                startActivity(eventoIntent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        loadMisParticipaciones();

        mRecyclerView2 = findViewById(R.id.my_recycler_view_EventosParticipacion);

        mRecyclerView2.setHasFixedSize(true);

        mlayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mlayoutManager2);

        mAdapter2 = new EventAdapter(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {
                Intent eventoIntent = new Intent(MyProfileActivity.this, EventDetailsActivity.class);
                Evento.packageIntent(eventoIntent,item.getNombre(),item.getFecha().toString(),item.getParticipantes(),item.getDescripcion(),item.getDeporte(),item.getPista(),item.getUserCreatorId(), item.getLatitud(),item.getLongitud());
                startActivity(eventoIntent);
            }
        });

        mRecyclerView2.setAdapter(mAdapter2);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

   //     if (mAdapter.getItemCount() == 0)
            loadItems();
            loadMisParticipaciones();
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

         if(id == R.id.action_cerrar_sesion){
            preferences.edit().clear().apply();
            Toast.makeText(getApplicationContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
            startActivityForResult(intent, GO_LOGIN_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }


    // Load stored Eventos
    private void loadItems() {
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> eventosUser= TeamMatchDataBase.getInstance(MyProfileActivity.this).getDao().getAllEventosByUserId(usuario_id);
                //log("USUARIO CREADOR" + userWithEventos.getUser().getUsername().toString());
                runOnUiThread(() -> mAdapter.load(eventosUser));
            }
        });
    }

    private void loadMisParticipaciones() {
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> eventosParticipacion= TeamMatchDataBase.getInstance(MyProfileActivity.this).getDao().getAllParticipacionesByUser(usuario_id);
                runOnUiThread(() -> mAdapter2.load(eventosParticipacion));
            }
        });
    }


    private void log (String msg){
        try {
            Thread.sleep(500);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
