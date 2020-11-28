package com.example.teammatch.objects;

import android.content.Intent;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "participacionUserEvento" )
public class ParticipacionUserEvento {

    @Ignore
    public final static String USER = "user";
    @Ignore
    public final static String EVENTO = "evento";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long idUser;

    private long idEvento;

    @Ignore
    public ParticipacionUserEvento(){
        idUser = 0;
        idEvento = 0;
    }

    @Ignore
    public ParticipacionUserEvento(long idUser, long idEvento) {
        this.idUser = idUser;
        this.idEvento = idEvento;
    }

    @Ignore
    public ParticipacionUserEvento(Intent intent){
        idUser = intent.getLongExtra(ParticipacionUserEvento.USER, 0);
        idEvento = intent.getLongExtra(ParticipacionUserEvento.EVENTO, 0);
    }

    public ParticipacionUserEvento(long id, long idUser, long idEvento) {
        this.id = id;
        this.idUser = idUser;
        this.idEvento = idEvento;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public static void packageIntent(Intent intent, long idUser, long idEvento){
        intent.putExtra(ParticipacionUserEvento.USER,idUser);
        intent.putExtra(ParticipacionUserEvento.EVENTO, idEvento);
    }
}
