package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "usuarios")
public class User {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    @Ignore
    public final static String ID = "ID";
    @Ignore
    public final static String USERNAME = "username";
    @Ignore
    public final static String EMAIL = "email";
    @Ignore
    public final static String PASSWORD = "password";
    @Ignore
    public final static ArrayList<Evento> myEventsPart = new ArrayList<Evento>(); //Lista de eventos en los que participa el user

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "username")
    private String Username;
    @ColumnInfo(name = "email")
    private String Email;
    @ColumnInfo(name = "password")
    private String Password;
    @Ignore
    private ArrayList<Evento> MyEventsPart;

    @Ignore
    public User() {
        this.Username = "";
        this.Email = "";
        this.Password = "";
        this.MyEventsPart = null;
    }

    @Ignore
    public User(String username, String email, String password, ArrayList<Evento> myEventsPart) {
        Username = username;
        Email = email;
        Password = password;
  //      MyEvents = myEvents;
        MyEventsPart = myEventsPart;
    }

    @Ignore
    public User(String Username, String Email ,String Password){
        this.Username = Username;
        this.Email = Email;
        this.Password = Password;
//        this.MyEvents = new ArrayList<Evento>();
        this.MyEventsPart = new ArrayList<Evento>();
    }

    public User(long id, String Username, String Email ,String Password) {
        this.id = id;
        this.Username = Username;
        this.Email = Email;
        this.Password = Password;
    }

    public User(Intent intent){
        id = intent.getLongExtra(User.ID,0);
        Username = intent.getStringExtra(User.USERNAME);
        Email = intent.getStringExtra(User.EMAIL);
        Password = intent.getStringExtra(User.PASSWORD);
        MyEventsPart = (ArrayList<Evento>) intent.getSerializableExtra("myEventsPart");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public static void packageIntent(Intent intent, String mUsername, String mEmail , String mPassword, ArrayList<Evento> mMyEventsPart) {
        intent.putExtra(User.USERNAME, mUsername);
        intent.putExtra(User.EMAIL, mEmail);
        intent.putExtra(User.PASSWORD, mPassword);
        intent.putExtra("MyEventsPart", mMyEventsPart);
    }

    public String toString() {
        return id + ITEM_SEP + Username + ITEM_SEP + Email + ITEM_SEP + Password;
    }
}
