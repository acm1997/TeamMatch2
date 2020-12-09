package com.example.teammatch.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.network.PistasActivity;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.Evento.Deporte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.teammatch.activities.MainActivity.SELECCIONAR_PISTA_EVENTO;

public class CrearEventoActivity extends AppCompatActivity {

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;

    private static final String TAG = "Lab-UserInterface";

    private static String horaString;
    private static String fechaString;
    private static TextView fechaView;
    private static TextView horaView;

    private EditText mNombre;
    private Date mFecha;
    private EditText mParticipantes;
    private EditText mDescripcion;
    private RadioGroup mDeportes;
    private RadioButton mDefaultDeporte;
    private TextView mPista;
    private ImageView mImagenView;
    private Button mImagen;

    String image_path;
    String latitud;
    String longitud;


    private SharedPreferences preferences;

    Intent i = new Intent();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_evento);
        mNombre = (EditText) findViewById(R.id.nombreEvento);
        mParticipantes =(EditText) findViewById(R.id.numParticipantes);
        mDescripcion = (EditText) findViewById(R.id.descEvento);
        mDeportes = (RadioGroup) findViewById(R.id.radioGroup);
        mDefaultDeporte = (RadioButton) findViewById(R.id.radioFutbol);
        mPista = (TextView) findViewById(R.id.idPistaSeleccionada);
        fechaView = (TextView) findViewById(R.id.fecha);
        horaView = (TextView) findViewById(R.id.hora);
        mImagenView = (ImageView) findViewById(R.id.iV_evento);


        //Usuario de la sesion
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);


        // Set the default date and time

        setDefaultDateTime();


        // OnClickListener for the Date button, calls showDatePickerDialog() to show
        // the Date dialog

        final Button datePickerButton = (Button) findViewById(R.id.botonfecha);
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());

        // OnClickListener for the Time button, calls showTimePickerDialog() to show
        // the Time Dialog

        final Button timePickerButton = (Button) findViewById(R.id.botonhora);
        timePickerButton.setOnClickListener(v -> showTimePickerDialog());

        //OnClickListener for the Pista button
        final Button selPista = (Button) findViewById(R.id.idSelecPista);
        selPista.setOnClickListener(v -> {
            Intent intent = new Intent(CrearEventoActivity.this, PistasActivity.class);
            startActivityForResult(intent, SELECCIONAR_PISTA_EVENTO);
        });

        final Button mImagen = (Button) findViewById(R.id.btn_add_image_event);
        mImagen.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(CrearEventoActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CrearEventoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        final  Button submitButton = findViewById(R.id.buttonCrear);
        submitButton.setOnClickListener(view -> {

            String n = mNombre.getText().toString();
            String d = fechaString + " " + horaString;
            String p = mParticipantes.getText().toString();
            String desc = mDescripcion.getText().toString();
            String pist = mPista.getText().toString();
            Deporte dep = getDeporte();
            String img = image_path;


            if(n != null && n.equals("")){
                Toast.makeText(CrearEventoActivity.this, "El nombre del evento está vacío", Toast.LENGTH_SHORT).show();
            }else{
                if(p != null && p.equals("")){
                    Toast.makeText(CrearEventoActivity.this, "El número de participantes está vacío", Toast.LENGTH_SHORT).show();
                }else{
                    if(desc != null && desc.equals("")){
                        Toast.makeText(CrearEventoActivity.this, "La descripción está vacía", Toast.LENGTH_SHORT).show();
                    }else {
                        if(pist != null && pist.equals("")){
                            Toast.makeText(CrearEventoActivity.this, "No tiene asignada ninguna pista", Toast.LENGTH_SHORT).show();
                        }else {
                            log("LATITUD Y LONGITUD que se pasan por el INTENT: " + latitud + " " + longitud);
                            //log(image_path);
                            Evento.packageIntent(i, n, d, Integer.parseInt(p), desc, dep, pist, usuario_id, latitud,longitud, image_path);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                }
            }
        });
    }

    //Actividad Añadir Pista
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Actividad Añadir Pista
        if (requestCode == SELECCIONAR_PISTA_EVENTO && resultCode == RESULT_OK) {
            TextView nombrePistaa = findViewById(R.id.idPistaSeleccionada);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final String nombrePista;
                    nombrePista = data.getStringExtra(Evento.PISTA);
                    latitud = data.getStringExtra(Evento.LATITUD);
                    longitud = data.getStringExtra(Evento.LONGITUD);
                    runOnUiThread(() -> {
                        nombrePistaa.setText(nombrePista);
                    });
                }
            });
        }
        //Añadir Imagen
        if(requestCode == 10 && resultCode == RESULT_OK){
            Bitmap imagesBitmap = null;
            try {
                imagesBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_path = createDirectoryAndSaveFile(imagesBitmap);
            mImagenView.setImageBitmap(imagesBitmap);
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
        String photoname = "IMGEvento_" + timeStamp + ".png";
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

    //Bloque añadir fecha y hora
    private void setDefaultDateTime() {

        // Default is current time + 7 days
        mFecha = new Date();
        mFecha = new Date(mFecha.getTime() + SEVEN_DAYS);

        Calendar c = Calendar.getInstance();
        c.setTime(mFecha);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        fechaView.setText(fechaString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        horaView.setText(horaString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        fechaString = year + "-" + mon + "-" + day;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        horaString = hour + ":" + min + ":00";
    }

    private void showDatePickerDialog() {
        DatePickerFragment dpf = new DatePickerFragment();
        dpf.show(getFragmentManager(),"dpf");

    }

    private void showTimePickerDialog() {
        TimePickerFragment tpf = new TimePickerFragment();
        tpf.show(getFragmentManager(),"tpf");


    }

    // DialogFragment used to pick a ToDoItem deadline date

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);

            fechaView.setText(fechaString);
        }

    }

    // DialogFragment used to pick a ToDoItem deadline time

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            horaView.setText(horaString);
        }
    }

    private Deporte getDeporte() {

        switch (mDeportes.getCheckedRadioButtonId()) {
            case R.id.radioFutbol: {
                return Deporte.FUTBOL;
            }
            case R.id.radioBaloncesto: {
                return Deporte.BALONCESTO;
            }

            case R.id.radioVoleibol: {
                return Deporte.VOLEIBOL;
            }
            default: {
                return Deporte.TENIS;
            }
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
