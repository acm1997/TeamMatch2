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

import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private ArrayList<Evento> mMyEventsPart = new ArrayList<Evento>();
    private Button btn_login;
    //Guardar credenciales
    private SharedPreferences preferences;

    public static final int GO_TO_REGISTER_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init
        mUsername = findViewById(R.id.et_email_username);
        mPassword = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);

        TeamMatchDataBase.getInstance(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();
                boolean validacion_login = validarCampos(username, password);
                if(validacion_login){
                    //Inicialización BD
                    TeamMatchDataBase userdatabase = TeamMatchDataBase.getInstance(getApplicationContext());
                    TeamMatchDAO userdao = userdatabase.getDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user = userdao.login(username, password);
                            if(user == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Usuario y contraseña no válidos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("usuario_id", user.getId());
                                editor.putString("username", user.getUsername());
                                editor.putString("email", user.getEmail());
                                editor.putString("password", user.getPassword());
                                editor.commit();
                                String username = user.getUsername();
                                startActivity(new Intent(LoginActivity.this, MyProfileActivity.class).putExtra("username", username));
                            }
                        }
                    }).start();
                }
            }
        });

        final ImageView botonIrRegistro = findViewById(R.id.imagebuttonRight);
        botonIrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent2, GO_TO_REGISTER_REQUEST);
            }
        });
    }

    public boolean validarCampos(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Ingresa el nombre de usuario y la contraseña", Toast.LENGTH_LONG).show();
            return false;
        } else if(username.length()<8 || password.length() < 8) {
            Toast.makeText(this, "Ingrese al menos un nombre de usuario y contraseña de 8 carácteres", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
}