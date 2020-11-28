package com.example.teammatch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teammatch.AppExecutors;
import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRePassword;
    private ArrayList<Evento> MyEventsPart = new ArrayList<Evento>();
    private Button btn_register;
    private SharedPreferences preferences;

    public static final int GO_TO_LOGIN_REQUEST = 98;
    public static final int GO_TO_PROFILE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = findViewById(R.id.et_username_register);
        mEmail = findViewById(R.id.et_email_username);
        mPassword = findViewById(R.id.et_password);
        mRePassword = findViewById(R.id.et_repassword_register);
        btn_register = findViewById(R.id.btn_register);

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
                                editor.commit();
                                
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
}