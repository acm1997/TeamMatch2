package com.example.teammatch.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.teammatch.R;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditUserActivity extends AppCompatActivity {

    private EditText eUsername;
    private EditText eEmail;
    private EditText ePassword;
    private EditText eRePassword;
    private Button btn_save;
    private Button btn_deleteuser;
    private ImageView imgProfile;
    private SharedPreferences preferences;
    public static final int GO_HOME_DELETE_USER_REQUEST = 0;

    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eUsername = findViewById(R.id.editTextTextPersonName);
        eEmail = findViewById(R.id.editTextTextEmailAddress);
        ePassword = findViewById(R.id.editTextTextPassword);
        eRePassword = findViewById(R.id.editTextTextPassword2);
        btn_save = findViewById(R.id.btn_save);
        btn_deleteuser = findViewById(R.id.btn_deleteuser);
        imgProfile = findViewById(R.id.img_profile);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);
        String imagePath = preferences.getString("path", null);

        //Mostrar datos actuales en los campos del usuario
        showDataUser(usuario_id, name, email, password, imagePath);

        TeamMatchDataBase.getInstance(this);

        //Modificar los datos de un usuario
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String editusername = eUsername.getText().toString();
                final String editemail = eEmail.getText().toString();
                final String editpassword = ePassword.getText().toString();
                final String editRepassword = eRePassword.getText().toString();

                if(editpassword.equals(editRepassword)){
                    boolean validacion_editar = validarNuevosCampos(editusername, editemail, editpassword);
                    if(validacion_editar){
                        TeamMatchDataBase userdatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                        TeamMatchDAO userdao = userdatabase.getDao();
                        User userupdate = new User(usuario_id, editusername, editemail, editpassword, imagePath);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("usuario_id", userupdate.getId());
                                editor.putString("username", userupdate.getUsername());
                                editor.putString("email", userupdate.getEmail());
                                editor.putString("password", userupdate.getPassword());
                                editor.putString("path", imgPath);
                                editor.commit();
                                userdao.update(userupdate);
                                String username = userupdate.getUsername();
                                startActivity(new Intent(EditUserActivity.this, MyProfileActivity.class).putExtra("username", username));
                            }
                        }).start();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Borrar un usuario
        btn_deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuild = new AlertDialog.Builder(EditUserActivity.this);
                myBuild.setMessage("¿Deseas borrar tu perfil?\nNOTA: Todos tus datos se perderan");
                myBuild.setTitle("Borrar perfil");
                myBuild.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(usuario_id > 0 && name != null && email != null && password != null){
                            TeamMatchDataBase userdatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                            TeamMatchDAO userdao = userdatabase.getDao();
                            User userdelete = new User(usuario_id, name, email, password, imagePath);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    preferences.edit().clear().apply();
                                    userdao.deleteUser(userdelete);
                                    Intent intentdelete = new Intent(EditUserActivity.this, MainActivity.class);
                                    startActivityForResult(intentdelete, GO_HOME_DELETE_USER_REQUEST);
                                }
                            }).start();
                        }
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
            }
        });
    }
    public void showDataUser(Long usuario_id, String username, String email, String password, String imageUserPath){
        if(usuario_id > 0 && username != null && email != null && password != null){
            eUsername.setText(username);
            eEmail.setText(email);
            ePassword.setText(password);
            Bitmap myBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Imagenes/"+ imageUserPath);

            imgProfile.setImageBitmap(myBitmap);
        }
    }

    public boolean validarNuevosCampos(String username, String email, String password){
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Rellena los campos vacíos", Toast.LENGTH_LONG).show();
            return false;
        } else if(username.length()<8 || password.length() < 8) {
            Toast.makeText(this, "Ingrese al menos un nombre de usuario y contraseña de 8 carácteres", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void OpenGalery(View view) {
        if (ActivityCompat.checkSelfPermission(EditUserActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditUserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
            return;
        }
        cargarImagen();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la Aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imagesBitmap = null;
        try {
            imagesBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgPath = createDirectoryAndSaveFile(imagesBitmap);
        imgProfile.setImageBitmap(imagesBitmap);
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
}
