package com.example.teammatch.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teammatch.R;
import com.example.teammatch.objects.Equipo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearEquipoActivity extends AppCompatActivity {

    private static final String TAG = "Lab-UserInterface";


    private EditText mNombre;
    private EditText mMiembros;
    private EditText mDescripcion;
    private Button mImagenEquipo;

    String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_equipo);

        mNombre = findViewById(R.id.tagNombreEquipo);
        mMiembros = findViewById(R.id.tagNumMiembros);
        mDescripcion = findViewById(R.id.tagDescripcionEquipo);

        final Button mImagenButton = (Button) findViewById(R.id.btn_add_image_team);
        mImagenButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(CrearEquipoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CrearEquipoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        });

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
            String img_team = image_path;

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
                        Equipo.packageIntent(i, n, Integer.parseInt(p), desc, image_path);

                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }
        });
    }

    //Añadir imagen de equipo

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK){
            Bitmap imagesBitmap = null;
            try {
                imagesBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_path = createDirectoryAndSaveFile(imagesBitmap);
        }
    }

    //Guardar imagen en el directorio
    private String createDirectoryAndSaveFile(Bitmap imageToSave) {

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Imagenes/";

        File direct = new File(file_path);

        if (!direct.exists()) {
            direct.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String photoname = "IMGEquipo_" + timeStamp + ".png";
        File file = new File(direct, photoname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 85, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoname;
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