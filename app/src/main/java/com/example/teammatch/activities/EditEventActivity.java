package com.example.teammatch.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.network.PistasActivity;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.teammatch.activities.MainActivity.SELECCIONAR_PISTA_EDITAR_EVENTO;


public class EditEventActivity extends AppCompatActivity {

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;
    private static final String TAG = "EDIT_EVENT";

    private static String horaString;
    private static String fechaString;
    private static TextView fechaView;
    private static TextView horaView;

    private EditText mNombre;
    private Date mFecha;
    private EditText mParticipantes;
    private EditText mDescripcion;
    private RadioGroup mDeportes;
    private RadioButton mDeporteFutbol;
    private RadioButton mDeporteBaloncesto;
    private RadioButton mDeporteTenis;
    private RadioButton mDeporteVoleibol;

    private TextView mPista;

    private Button btn_save;
    private Button btn_editImg;

    String latitud;
    String longitud;
    String image_path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_evento);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Evento e = new Evento(getIntent());
        e.setId(getIntent().getLongExtra("ID", 0));


        mNombre = (EditText) findViewById(R.id.Editname);
        mParticipantes =(EditText) findViewById(R.id.Editparticipantes);
        mDescripcion = (EditText) findViewById(R.id.Editdesc);
        mDeportes = (RadioGroup) findViewById(R.id.radioEGroup);
        mPista = (TextView) findViewById(R.id.editPista);

        mDeporteBaloncesto = (RadioButton) findViewById(R.id.radioEBaloncesto);
        mDeporteTenis = (RadioButton) findViewById(R.id.radioETenis);
        mDeporteVoleibol = (RadioButton) findViewById(R.id.radioEVoleibol);
        mDeporteFutbol = (RadioButton) findViewById(R.id.radioEFutbol);
       // mPista = (TextView) findViewById(R.id.editCat);
        fechaView = (TextView) findViewById(R.id.editfecha);
        horaView = (TextView) findViewById(R.id.edithora);
        btn_save = findViewById(R.id.btn_saveEventEdit);


        //Mostrar datos actuales en los campos del evento
        showDataEvent( e.getNombre(), e.getParticipantes().toString(), e.getDescripcion(), e.getPista());
        if(e.getDeporte().name().equals("FUTBOL")) mDeporteFutbol.setChecked(true);
        if(e.getDeporte().name().equals("BALONCESTO")) mDeporteBaloncesto.setChecked(true);
        if(e.getDeporte().name().equals("TENIS")) mDeporteTenis.setChecked(true);
        if(e.getDeporte().name().equals("VOLEIBOL")) mDeporteVoleibol.setChecked(true);

        setDefaultDateTime();

        //Coordenadas actuales de la pista
        latitud = e.getLatitud();
        longitud = e.getLongitud();

        // OnClickListener for the Date button, calls showDatePickerDialog() to show
        // the Date dialog

        final Button btn_fechaedit = (Button) findViewById(R.id.btn_fechaedit);
        btn_fechaedit.setOnClickListener(v -> showDatePickerDialog());

        // OnClickListener for the Time button, calls showTimePickerDialog() to show
        // the Time Dialog

        final Button btn_horaedit = (Button) findViewById(R.id.btn_horaedit);
        btn_horaedit.setOnClickListener(v -> showTimePickerDialog());

        //OnClickListener for the Pista button
        final Button btn_pistaedit = (Button) findViewById(R.id.btn_pistaedit);
        btn_pistaedit.setOnClickListener(v -> {
            Intent intent = new Intent(EditEventActivity.this, PistasActivity.class);
            startActivityForResult(intent, SELECCIONAR_PISTA_EDITAR_EVENTO);
        });

        //Botón cambiar imagen
        final Button btn_editImg = (Button) findViewById(R.id.btn_imgedit);
        btn_editImg.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EditEventActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        });


        TeamMatchDataBase.getInstance(this);

        //Modificar los datos de un evento

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String editNombreEvento = mNombre.getText().toString();
                final String editDescripcionEvento = mDescripcion.getText().toString();
                final String editParticipantes = mParticipantes.getText().toString();
                final String editPista = mPista.getText().toString();
                final String editFechaHora =  fechaString +" " + horaString;

                //TODO - Poner la fecha y hora editada en el nuevo evento editado.

                TeamMatchDataBase eventodatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                 TeamMatchDAO eventoDAO = eventodatabase.getDao();

                    Intent i = new Intent();
                    Evento.packageIntent(i,editNombreEvento,editFechaHora,Integer.parseInt(editParticipantes),editDescripcionEvento,getDeporte(),editPista,e.getUserCreatorId(),latitud, longitud, image_path);

                    Evento eventoupdate = new Evento(i);
                    eventoupdate.setId(e.getId());


                new Thread(new Runnable() {
                        @Override
                        public void run() {
                            eventoDAO.updateEvento(eventoupdate);
                            startActivity(new Intent(EditEventActivity.this, MainActivity.class));
                        }
                    }).start();
            }
        });

    }

    private Evento.Deporte getDeporte() {

        switch (mDeportes.getCheckedRadioButtonId()) {
            case R.id.radioEFutbol: {
                return Evento.Deporte.FUTBOL;
            }
            case R.id.radioEBaloncesto: {
                return Evento.Deporte.BALONCESTO;
            }

            case R.id.radioETenis: {
                return Evento.Deporte.TENIS;
            }
            default: {
                return Evento.Deporte.VOLEIBOL;
            }
        }
    }

    public void showDataEvent(String _mNombre, String _mParticipantes, String _mDescripcion, String _mPista/*, String deporte*/){
        mNombre.setText(_mNombre);
        mParticipantes.setText(_mParticipantes);
        mDescripcion.setText(_mDescripcion);
        mPista.setText(_mPista);
    }

    public void mostrarHoraEvento(String fechaMasHora){
        List<Character> listaHora = null;
        String horaMostrar;
        //Cojo los caracteres que contienen la hora.
        log("fecha + hora que intento mostrar: "+ fechaMasHora);
        for (char i=fechaMasHora.charAt(9);i<=fechaMasHora.charAt(19);i++){
            log("Caracter a mostrar: " + i );
            listaHora.add(i);
        }

        horaView.setText(listaHora.toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECCIONAR_PISTA_EDITAR_EVENTO && resultCode == RESULT_OK) {
            mPista = findViewById(R.id.editPista);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final String nombrePista;
                    nombrePista = data.getStringExtra(Evento.PISTA);
                    runOnUiThread(() -> {
                        mPista.setText(nombrePista);
                    });
                }
            });
        }
        //Modificar la imagen
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

    //Guardar la nueva imagen en el directorio
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

    private void showDatePickerDialog() {
        EditEventActivity.DatePickerFragment dpf = new EditEventActivity.DatePickerFragment();
        dpf.show(getFragmentManager(),"dpf");
    }

    private void showTimePickerDialog() {
        EditEventActivity.TimePickerFragment tpf = new EditEventActivity.TimePickerFragment();
        tpf.show(getFragmentManager(),"tpf");


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