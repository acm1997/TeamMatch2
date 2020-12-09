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
    public final static String IMG_PATH = "imag_path";

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "username")
    private String Username;
    @ColumnInfo(name = "email")
    private String Email;
    @ColumnInfo(name = "password")
    private String Password;

    private String UserPhotoPath;

    @Ignore
    public User() {
        this.Username = "";
        this.Email = "";
        this.Password = "";
        this.UserPhotoPath = "";
    }

    @Ignore
    public User(String Username, String Email ,String Password, String UserPhotoPath){
        this.Username = Username;
        this.Email = Email;
        this.Password = Password;
        this.UserPhotoPath = UserPhotoPath;
    }

    public User(long id, String Username, String Email ,String Password, String UserPhotoPath) {
        this.id = id;
        this.Username = Username;
        this.Email = Email;
        this.Password = Password;
        this.UserPhotoPath = UserPhotoPath;
    }

    public User(Intent intent){
        id = intent.getLongExtra(User.ID,0);
        Username = intent.getStringExtra(User.USERNAME);
        Email = intent.getStringExtra(User.EMAIL);
        Password = intent.getStringExtra(User.PASSWORD);
        UserPhotoPath = intent.getStringExtra(User.IMG_PATH);
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

    public String getUserPhotoPath() {
        return UserPhotoPath;
    }

    public void setUserPhotoPath(String userPhotoPath) {
        UserPhotoPath = userPhotoPath;
    }

    public static void packageIntent(Intent intent, String mUsername, String mEmail , String mPassword, String mUserPhotoPath) {
        intent.putExtra(User.USERNAME, mUsername);
        intent.putExtra(User.EMAIL, mEmail);
        intent.putExtra(User.PASSWORD, mPassword);
        intent.putExtra(User.IMG_PATH, mUserPhotoPath);
    }

    public String toString() {
        return id + ITEM_SEP + Username + ITEM_SEP + Email + ITEM_SEP + Password;
    }
}
