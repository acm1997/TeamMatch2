package com.example.teammatch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.ParticipacionUserEvento;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class EventDetailsActivity extends AppCompatActivity {

    public static final int GO_HOME_DELETE_EVENT_REQUEST = 0;
    private static final String TAG = "DETALLES_ACTIVITY";
    private static boolean Participo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        SharedPreferences preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);

        Evento e = new Evento(getIntent());
        e.setId(getIntent().getLongExtra("ID", 0));

        Toolbar toolbarShowName = (Toolbar) findViewById(R.id.toolbar_nameEvent);
        setSupportActionBar(toolbarShowName);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(e.getNombre());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button btn_deleteEvent = findViewById(R.id.btn_deleteEvent);


        //Solo se muestra el botón editar y borrar si el usuario ha creado el evento
        if(usuario_id > 0 && usuario_id.equals(e.getUserCreatorId())){

            //  BOTÓN EDITAR
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view -> {
                Intent eventIntent = new Intent(EventDetailsActivity.this, EditEventActivity.class);
                Evento.packageIntent(eventIntent,e.getNombre(),e.getFecha().toString(),e.getParticipantes(),e.getDescripcion(),e.getDeporte(),e.getPista(),e.getUserCreatorId(), e.getLatitud(),e.getLongitud());
                eventIntent.putExtra("ID", e.getId());
                startActivity(eventIntent);
            });

            //  BOTÓN BORRAR
            btn_deleteEvent.setVisibility(View.VISIBLE);
            btn_deleteEvent.setOnClickListener(v -> {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(EventDetailsActivity.this);
                myBuild.setMessage("¿Quieres eliminar este evento deportivo?\nNOTA: No podrás recuperar sus datos, y los usuarios que participen dejarán de tener acceso a este evento");
                myBuild.setTitle("Borrar evento deportivo");
                myBuild.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TeamMatchDataBase eventodatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                        TeamMatchDAO eventodao = eventodatabase.getDao();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                log("ID Evento a borrar: "+e.getId());
                                eventodao.deleteEvento(e);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).start();
                    }
                });
                myBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = myBuild.create();
                dialog.show();
            });

        } else {
            fab.setVisibility(View.INVISIBLE);
            btn_deleteEvent.setVisibility(View.INVISIBLE);
        }

        TextView tVMostrarDescripcionEvento = findViewById(R.id.TVDescEvent);
        tVMostrarDescripcionEvento.setText(e.getDescripcion());

        TextView tVMostrarDeporteEvento = findViewById(R.id.TVCatEvent);
        tVMostrarDeporteEvento.setText(e.getDeporte().toString());

        TextView tVMostrarFechaEvento = findViewById(R.id.TVDateEvent);
        tVMostrarFechaEvento.setText(e.FORMAT.format(e.getFecha()));

        TextView tVMostrarPistaEvento = findViewById(R.id.TVPistaEvent);
        tVMostrarPistaEvento.setText(e.getPista());

        TextView tVMostrarParticipantesEvento = findViewById(R.id.TVParticipantsEvent);
        tVMostrarParticipantesEvento.setText(e.getParticipantes().toString());

        final Button map = (Button) findViewById(R.id.btn_map);
        map.setOnClickListener(v -> {
            Uri uri = Uri.parse("geo:"+e.getLatitud()+","+e.getLongitud()+"?z=16&q="+e.getLatitud()+","+e.getLongitud()+"(Caceres)");
            startActivity( new Intent(Intent.ACTION_VIEW, uri));
        });

        //  SWITCH PARTICPAR
        Switch sw = (Switch) findViewById(R.id.switch1);
        if(usuario_id > 0){
            sw.setVisibility(View.VISIBLE);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    TeamMatchDataBase teamMatchDataBase = TeamMatchDataBase.getInstance(EventDetailsActivity.this);
                    ParticipacionUserEvento participacionUserEvento = teamMatchDataBase.getDao().getParticipacion(usuario_id, e.getId());
                    if(Objects.equals(participacionUserEvento, null)){
                        runOnUiThread(()-> sw.setChecked(false));
                        Participo = false;
                    }else{
                        runOnUiThread(()-> sw.setChecked(true));
                        Participo = true;
                    }
                }
            });

            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && !Participo) {
                    ParticipacionUserEvento nuevoparticipante =  new ParticipacionUserEvento(usuario_id, e.getId());
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        TeamMatchDataBase teamMatchDataBase = TeamMatchDataBase.getInstance(EventDetailsActivity.this);
                        long idParticipacion = teamMatchDataBase.getDao().insertParticipacion(nuevoparticipante);
                        nuevoparticipante.setId(idParticipacion);
                        Participo = true;
                    });

                } else if(!isChecked){
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        TeamMatchDataBase teamMatchDataBase = TeamMatchDataBase.getInstance(EventDetailsActivity.this);
                        teamMatchDataBase.getDao().deleteParticipacion(usuario_id,e.getId());
                        Participo = false;
                    });
                }
            });
        }else{
            sw.setVisibility(View.INVISIBLE);
        }

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