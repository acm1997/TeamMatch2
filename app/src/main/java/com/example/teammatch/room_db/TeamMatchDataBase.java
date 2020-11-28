package com.example.teammatch.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.teammatch.objects.Equipo;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.ParticipacionUserEvento;
import com.example.teammatch.objects.User;


@Database(entities = {Evento.class, User.class, Equipo.class, ParticipacionUserEvento.class}, version = 1)
public abstract class TeamMatchDataBase extends RoomDatabase {

    public static TeamMatchDataBase instance;

    public static TeamMatchDataBase getInstance(Context context){
        if(instance == null)
            instance = Room.databaseBuilder(context, TeamMatchDataBase.class, "TeamMatch.db").build();
        return instance;
    }

    public abstract TeamMatchDAO getDao();

}