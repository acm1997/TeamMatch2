package com.example.teammatch.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_ACTIVITY";
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRePassword;
    private ArrayList<Evento> MyEventsPart = new ArrayList<Evento>();
    private Button btn_register;
    private SharedPreferences preferences;
    private ImageView mIVUser;

    public static final int GO_TO_LOGIN_REQUEST = 98;
    public static final int GO_TO_PROFILE = 99;

    String image_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.et_username_register);
        mEmail = findViewById(R.id.et_email_username);
        mPassword = findViewById(R.id.et_password);
        mRePassword = findViewById(R.id.et_repassword_register);
        btn_register = findViewById(R.id.btn_register);
        mIVUser = findViewById(R.id.ivUser);

        final Button mImgUser = (Button) findViewById(R.id.btn_imageUser);
        mImgUser.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassword.getText().toString();
                String repassword = mRePassword.getText().toString();
                preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

                //Si las contraseña y confirmación de contraseña coinciden se crea el usuario
                if(password.equals(repassword)){
                    //Crear nuevo usuario
                    User user = new User();
                    user.setUsername(mUsername.getText().toString());
                    user.setEmail(mEmail.getText().toString());
                    user.setPassword(mPassword.getText().toString());
                    user.setUserPhotoPath(image_path);
                    log("Imagen "+ user.getUserPhotoPath());
                    boolean validacion_register = validarCampos(user);

                    if(validacion_register){
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                TeamMatchDataBase userdatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                                long id_user = userdatabase.getDao().insertUser(user);
                                user.setId(id_user);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("usuario_id", user.getId());
                                editor.putString("username", user.getUsername());
                                editor.putString("email", user.getEmail());
                                editor.putString("password", user.getPassword());
                                editor.putString("path", image_path);
                                editor.commit();
                                log("Imagen "+ user.getUserPhotoPath());
                                Intent intentP = new Intent(RegisterActivity.this, MyProfileActivity.class);
                                startActivityForResult(intentP, GO_TO_PROFILE);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final ImageView botonIrLogin = findViewById(R.id.imagebuttonLeft);
        botonIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivityForResult(intent2, GO_TO_LOGIN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Añadir Imagen
        if(requestCode == 10 && resultCode == RESULT_OK){
            Bitmap imagesBitmap = null;
            try {
                imagesBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            image_path = createDirectoryAndSaveFile(imagesBitmap);
            mIVUser.setImageBitmap(imagesBitmap);
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
        String photoname = "IMGUser_" + timeStamp + ".png";
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

    private Boolean validarCampos(User user){
        if(user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            Toast.makeText(this, "Ingresa un nombre de usuario, email y contraseña", Toast.LENGTH_LONG).show();
            return false;
        } else if(user.getUsername().length()<8 || user.getPassword().length()<8){
            Toast.makeText(this, "Ingrese al menos un nombre de usuario y contraseña de 8 carácteres", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
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