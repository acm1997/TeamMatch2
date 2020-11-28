package com.example.teammatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Evento;


public class PistaDetallesActivity extends AppCompatActivity {

    private static final String TAG = "Lab-UserInterface";
    public static final int ADD_PISTA_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pista_detalles);

        final String nombrePista = getIntent().getStringExtra(Binding.NOMBRE);
        String ciudadPista = getIntent().getStringExtra(Binding.CIUDAD);
        String callePista = getIntent().getStringExtra(Binding.CALLE);
      //  if ( nombrePista == null ) nombrePista="";
        if ( ciudadPista == null ) ciudadPista="";
        if ( callePista == null ) callePista="";
        TextView nombrePistaa = findViewById(R.id.nombrePista);
        nombrePistaa.setText(nombrePista);
        TextView ciudadPistaa = findViewById(R.id.nombreCiudad);
        ciudadPistaa.setText( ciudadPista);
        TextView callePistaa = findViewById(R.id.nombreCalle);
        callePistaa.setText( callePista);

        Button anadir = findViewById(R.id.buttomAdd);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pistaIntent = new Intent(PistaDetallesActivity.this, CrearEventoActivity.class);
                pistaIntent.putExtra(Evento.PISTA, nombrePista);
                startActivityForResult(pistaIntent, ADD_PISTA_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PISTA_REQUEST && resultCode == RESULT_OK){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                   finish();
                }
            });
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
