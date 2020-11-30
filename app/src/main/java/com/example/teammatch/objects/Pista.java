package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pista")
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

    @ColumnInfo(name = "geoLong")
    private String geoLongPista;
    @ColumnInfo(name = "geoLat")
    private String geoLatPista;

    public Pista(){
        this.nombrePista = "";
        this.ciudadPista = "";
        this.callePista = "";
        this.geoLongPista = "";
        this.geoLongPista = "";
    }

    @Ignore
    public Pista(String mNombrePista, String mCiudadPista, String mCallePista, String mGeoLongPista, String mGeoLatPista) {
        this.nombrePista = mNombrePista;
        this.ciudadPista = mCiudadPista;
        if(mCallePista==null) this.callePista="";
        this.callePista = mCallePista;
        this.geoLongPista = mGeoLongPista;
        this.geoLatPista = mGeoLatPista;
    }

    @Ignore
    public Pista(Intent intent){
        nombrePista = intent.getStringExtra(Pista.NOMBRE);
        this.ciudadPista = intent.getStringExtra(Pista.CIUDAD);
        this.callePista = intent.getStringExtra(Pista.CALLE);
        this.geoLongPista = intent.getStringExtra(Pista.LONGITUD);
        this.geoLatPista = intent.getStringExtra(Pista.LATITUD);
    }

    public Pista(long id, String nombrePista, String ciudadPista, String callePista, String geoLongPista, String geoLatPista) {
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

    public String getGeoLongPista() {
        return geoLongPista;
    }

    public void setGeoLongPista(String geoLongPista) {
        this.geoLongPista = geoLongPista;
    }

    public String getGeoLatPista() {
        return geoLatPista;
    }

    public void setGeoLatPista(String geoLatPista) {
        this.geoLatPista = geoLatPista;
    }

    public static void packageIntent(Intent intent, String nCiudadPista, String mCallePista, String mGeoLongPista, String mGeoLatPista){
        intent.putExtra(Pista.NOMBRE, nCiudadPista);
        intent.putExtra(Pista.CIUDAD, nCiudadPista);
        intent.putExtra(Pista.CALLE, mCallePista);
        intent.putExtra(Pista.LONGITUD, mGeoLongPista);
        intent.putExtra(Pista.LATITUD, mGeoLatPista);
    }
}