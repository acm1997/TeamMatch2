package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.teammatch.room_db.DeporteConverter;
import com.example.teammatch.room_db.FechaConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity (tableName = "evento" )
public class Evento {

    public enum Deporte {
        FUTBOL,BALONCESTO,TENIS,VOLEIBOL
    }

    @Ignore
    public final static String NOMBRE = "nombre";
    @Ignore
    public final static String FECHA = "fecha";
    @Ignore
    public final static String PARTICIPANTES = "participantes";
    @Ignore
    public final static String DESCRIPCION = "descripcion";
    @Ignore
    public final static String DEPORTE = "deporte";
    @Ignore
    public final static String PISTA = "pista";
    @Ignore
    public final static String USER = "user";
    @Ignore
    public final static String LATITUD = "latitud";
    @Ignore
    public final static String LONGITUD = "longitud";

    @Ignore
    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    @PrimaryKey (autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @TypeConverters(FechaConverter.class)
    private Date fecha;

    private Integer participantes;

    private String descripcion;

    private String pista;

    @TypeConverters(DeporteConverter.class)
    private Deporte deporte;

    private long userCreatorId;

    private String latitud;
    private String longitud;

    @Ignore
    public Evento() {
        this.nombre = "";
        this.fecha = null;
        this.participantes = 0;
        this.descripcion = "";
        this.deporte = null;
        this.pista = "";
        this.userCreatorId = 0;
        this.latitud = "";
        this.longitud = "";
    }

    @Ignore
    public Evento(String mNombre, Date mFecha, Integer mParticipantes, String mDescripcion, Deporte mDeporte,String mPista,long userCreatorId, String mLatitud, String mLongitud) {
        this.nombre = mNombre;
        this.fecha = mFecha;
        this.participantes = mParticipantes;
        this.descripcion = mDescripcion;
        this.deporte = mDeporte;
        this.userCreatorId = userCreatorId;
        this.pista = mPista;
        this.latitud = mLatitud;
        this.longitud = mLongitud;
    }

    @Ignore
    public Evento(Intent intent){
        nombre = intent.getStringExtra(Evento.NOMBRE);
        participantes = intent.getIntExtra(Evento.PARTICIPANTES, 0);
        descripcion = intent.getStringExtra(Evento.DESCRIPCION);
        deporte = Deporte.valueOf(intent.getStringExtra(Evento.DEPORTE));
        pista = intent.getStringExtra(Evento.PISTA);
        latitud = intent.getStringExtra(Evento.LATITUD);
        longitud = intent.getStringExtra(Evento.LONGITUD);

        try {
            fecha = Evento.FORMAT.parse(intent.getStringExtra(Evento.FECHA));
        } catch (ParseException e) {
            fecha = new Date();
        }

        userCreatorId = intent.getLongExtra(Evento.USER, 0);
    }

    public Evento(long id, String nombre, Date fecha, Integer participantes, String descripcion, Deporte deporte, String pista, long userCreatorId,  String latitud, String longitud) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
        this.descripcion = descripcion;
        this.deporte = deporte;
        this.pista = pista;
        this.userCreatorId = userCreatorId;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public Integer getParticipantes() {
        return participantes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setParticipantes(Integer participantes) {
        this.participantes = participantes;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public long getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(long userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public static void packageIntent(Intent intent, String mNombre, String mFecha, Integer mParticipantes, String mDescripcion, Deporte mDeporte, String mPista, long id_user, String mLatitud, String mLongitud) {
        intent.putExtra(Evento.NOMBRE, mNombre);
        intent.putExtra(Evento.DESCRIPCION, mDescripcion);
        intent.putExtra(Evento.FECHA, mFecha);
        intent.putExtra(Evento.PARTICIPANTES, mParticipantes);
        intent.putExtra(Evento.DEPORTE, mDeporte.toString());
        intent.putExtra(Evento.PISTA, mPista);
        intent.putExtra(Evento.USER, id_user);
        intent.putExtra(Evento.LATITUD, mLatitud);
        intent.putExtra(Evento.LONGITUD, mLongitud);
    }

}
