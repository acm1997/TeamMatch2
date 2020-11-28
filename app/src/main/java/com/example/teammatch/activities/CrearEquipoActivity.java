package com.example.teammatch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teammatch.R;
import com.example.teammatch.objects.Equipo;

public class CrearEquipoActivity extends AppCompatActivity {

    private static final String TAG = "Lab-UserInterface";


    private EditText mNombre;
    private EditText mMiembros;
    private EditText mDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equipo);

        mNombre = findViewById(R.id.tagNombreEquipo);
        mMiembros = findViewById(R.id.tagNumMiembros);
        mDescripcion = findViewById(R.id.tagDescripcionEquipo);


        final Button cancelButton = findViewById(R.id.cancelButton2);
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        final Button submitButton = findViewById(R.id.buttonCrearEquipo);
        submitButton.setOnClickListener(view -> {

            String n = mNombre.getText().toString();
            String p = mMiembros.getText().toString();
            String desc = mDescripcion.getText().toString();

            if (n != null && n.equals("")) {
                Toast.makeText(CrearEquipoActivity.this, "El nombre del equipo está vacío", Toast.LENGTH_SHORT).show();
            } else {
                if (p != null && p.equals("")) {
                    Toast.makeText(CrearEquipoActivity.this, "El número de miembros está vacío", Toast.LENGTH_SHORT).show();
                } else {
                    if (desc != null && desc.equals("")) {
                        Toast.makeText(CrearEquipoActivity.this, "La descripción está vacía", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent();
                        Equipo.packageIntent(i, n, Integer.parseInt(p), desc);

                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
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