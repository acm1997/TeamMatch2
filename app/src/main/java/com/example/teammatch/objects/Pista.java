package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

public class Pista {

    @Ignore
    public final static String NOMBRE = "nombre";
    @Ignore
    public final static String CIUDAD = "ciudad";
    @Ignore
    public final static String CALLE = "calle";
    @Ignore
    public final static String LONGITUD = "longitud";
    @Ignore
    public final static String LATITUD = "latitud";


    @PrimaryKey (autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nombrePista")
    private String nombrePista;

    @ColumnInfo(name = "ciudadPista")
    private String ciudadPista;

    @ColumnInfo(name = "callePista")
    private String callePista;

    private Integer geoLongPista = 0;
    private Integer geoLatPista = 0;

    public Pista(){
        this.nombrePista = "";
        this.ciudadPista = "";
        this.callePista = "";
        this.geoLongPista = 0;
        this.geoLongPista = 0;
    }

    @Ignore
    public Pista(String mNombrePista, String mCiudadPista, String mCallePista, Integer mGeoLongPista, Integer mGeoLatPista) {
        this.nombrePista = mNombrePista;
        this.ciudadPista = mCiudadPista;
        this.callePista = mCallePista;
        this.geoLongPista = mGeoLongPista;
        this.geoLatPista = mGeoLatPista;
    }

    @Ignore
    public Pista(Intent intent){
        nombrePista = intent.getStringExtra(Pista.NOMBRE);
        this.ciudadPista = intent.getStringExtra(Pista.CIUDAD);
        this.callePista = intent.getStringExtra(Pista.CALLE);
        this.geoLongPista = intent.getIntExtra(Pista.LONGITUD, 0);
        this.geoLatPista = intent.getIntExtra(Pista.LATITUD, 0);
    }

    public Pista(long id, String nombrePista, String ciudadPista, String callePista, Integer geoLongPista, Integer geoLatPista) {
        this.id = id;
        this.nombrePista = nombrePista;
        this.ciudadPista = ciudadPista;
        this.callePista = callePista;
        this.geoLongPista = geoLongPista;
        this.geoLatPista = geoLatPista;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombrePista() {
        return nombrePista;
    }

    public void setNombrePista(String nombrePista) {
        this.nombrePista = nombrePista;
    }

    public String getCiudadPista() {
        return ciudadPista;
    }

    public void setCiudadPista(String ciudadPista) {
        this.ciudadPista = ciudadPista;
    }

    public String getCallePista() {
        return callePista;
    }

    public void setCallePista(String callePista) {
        this.callePista = callePista;
    }

    public Integer getGeoLongPista() {
        return geoLongPista;
    }

    public void setGeoLongPista(Integer geoLongPista) {
        this.geoLongPista = geoLongPista;
    }

    public Integer getGeoLatPista() {
        return geoLatPista;
    }

    public void setGeoLatPista(Integer geoLatPista) {
        this.geoLatPista = geoLatPista;
    }

    public static void packageIntent(Intent intent, String nCiudadPista, String mCallePista, Integer mGeoLongPista, Integer mGeoLatPista){
        intent.putExtra(Pista.NOMBRE, nCiudadPista);
        intent.putExtra(Pista.CIUDAD, nCiudadPista);
        intent.putExtra(Pista.CALLE, mCallePista);
        intent.putExtra(Pista.LONGITUD, mGeoLongPista);
        intent.putExtra(Pista.LATITUD, mGeoLatPista);
    }
}