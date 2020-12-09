package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipo")
public class Equipo {

    @Ignore
    public final static String NOMBRE = "nombre";
    @Ignore
    public final static String MIEMBROS = "miembros";
    @Ignore
    public final static String DESCRIPCION = "descripcion";
    @Ignore
    public final static String IMG_PATH = "imag_path";

    @PrimaryKey (autoGenerate = true)
    private long id;
    @ColumnInfo(name = "nombre")
    private String nombre;
    private Integer miembros;
    private String descripcion;
    private String EquipoPhotoPath;

    @Ignore
    public Equipo() {
        this.nombre = "";
        this.miembros = 0;
        this.descripcion = "";
        this.EquipoPhotoPath = "";
    }

    @Ignore
    public Equipo(String nombre, Integer miembros, String descripcion, String mEquipoPhotoPath) {
        this.nombre = nombre;
        this.miembros = miembros;
        this.descripcion = descripcion;
        this.EquipoPhotoPath = mEquipoPhotoPath;
    }

    @Ignore
    public Equipo (Intent intent){
        nombre = intent.getStringExtra(Equipo.NOMBRE);
        miembros = intent.getIntExtra(Equipo.MIEMBROS, 0);
        descripcion = intent.getStringExtra(Equipo.DESCRIPCION);
        EquipoPhotoPath = intent.getStringExtra(Equipo.IMG_PATH);
    }

    public Equipo(long id, String nombre, Integer miembros, String descripcion, String EquipoPhotoPath) {
        this.id = id;
        this.nombre = nombre;
        this.miembros = miembros;
        this.descripcion = descripcion;
        this.EquipoPhotoPath = EquipoPhotoPath;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMiembros() {
        return miembros;
    }

    public void setMiembros(Integer miembros) {
        this.miembros = miembros;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEquipoPhotoPath() {
        return EquipoPhotoPath;
    }

    public void setEquipoPhotoPath(String equipoPhotoPath) {
        EquipoPhotoPath = equipoPhotoPath;
    }

    public static void packageIntent(Intent intent, String mNombre, Integer mMiembros, String mDescripcion, String mEquipoPhotoPath){
        intent.putExtra(Equipo.NOMBRE, mNombre);
        intent.putExtra(Equipo.DESCRIPCION, mDescripcion);
        intent.putExtra(Equipo.MIEMBROS, mMiembros);
        intent.putExtra(Equipo.IMG_PATH, mEquipoPhotoPath);
    }
}
