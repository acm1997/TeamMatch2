package com.example.teammatch.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.teammatch.R;
import com.example.teammatch.fragments.EventosCreadosFragment;
import com.example.teammatch.fragments.EventosParticipadosFragment;
import com.example.teammatch.objects.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "MY_PROFILE_ACTIVITY";

    private TextView tname;
    private Button btn_EditP;
    private ImageView img;
    String image_path;

    private SharedPreferences preferences;

    public static final int EDIT_PROFILE_REQUEST = 0;
    public static final int GO_LOGIN_REQUEST = 1;


    // EVENTOS
    public static ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private String[] titles = new String[]{"Eventos creados", "Eventos en los que participas"};

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
        User u = new User(getIntent());

        if(usuario_id > 0 && name != null && email != null && password != null){
            tname.setText(name);
            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Imagenes/"+ u.getUserPhotoPath());

            img.setImageBitmap(myBitmap);
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

        viewPager = findViewById(R.id.viewPager_Eventos);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();

    }

    @Override
    public void onResume() {
        super.onResume();

        //TODO - NOTIFICAR LOS CAMBIOS DEL FRAGMENT (?)
        // loadItems();

        //TODO - NOTIFICAR LOS CAMBIOS DEL FRAGMENT (?)
        // loadMisParticipaciones();
    }

 /*   static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto(View view) {
        if (ActivityCompat.checkSelfPermission(MyProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }*/

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


    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return new EventosCreadosFragment();
                }
                case 1: {
                    return new EventosParticipadosFragment();
                }
                default:
                    return new EventosCreadosFragment();
            }
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
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
