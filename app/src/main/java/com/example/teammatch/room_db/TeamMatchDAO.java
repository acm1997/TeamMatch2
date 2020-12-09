package com.example.teammatch.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.teammatch.objects.Binding;
import com.example.teammatch.objects.Equipo;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.ParticipacionUserEvento;
import com.example.teammatch.objects.Pista;
import com.example.teammatch.objects.Pistas;
import com.example.teammatch.objects.User;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TeamMatchDAO {

    //  EVENTOS
    @Query("SELECT * FROM evento")
    public List<Evento> getAllEventos();

    @Query("SELECT * FROM evento")
    LiveData<List<Evento>> getLiveDataAllEventos();

    @Insert
    public long insertEvento(Evento evento);

    @Update
    public int updateEvento(Evento evento);

    @Delete
    public void deleteEvento(Evento evento);

    @Query("DELETE FROM evento")
    public void deleteAllEventos();

    @Query("SELECT * FROM evento WHERE userCreatorId = :userCreatorId")
    public List<Evento> getAllEventosByUserId(long userCreatorId);

    @Query("SELECT * FROM evento where nombre= :nombre")
    public List<Evento> SearchByName(String nombre);

    @Query("SELECT * FROM evento where deporte LIKE :deporte")
    public List<Evento> SearchByCatFutbol(String deporte);

    //  EQUIPOS
    @Query("SELECT * FROM equipo")
    public List<Equipo> getAllEquipos();

    @Insert
    public long insertEquipo(Equipo equipo);

    @Update
    public int updateEquipo(Equipo equipo);

    @Delete
    public void deleteEquipo(Equipo equipo);

    @Query("DELETE FROM equipo")
    public void deleteAllEquipos();


    //  USUARIOS
    @Query("SELECT * FROM usuarios")
    public List<User> getAllUsers();

    @Insert
    public long insertUser(User usuario);

    @Update
    public int update(User usuario);

    @Query("DELETE FROM usuarios")
    public void deleteAllUsers();

    @Delete
    public void deleteUser(User usuario);

    @Query("SELECT * FROM usuarios where username=(:username) and password=(:password)")
    User login(String username, String password);



    // PARTICIPACION DE USUARIOS EN EVENTOS

    @Insert
    public long insertParticipacion(ParticipacionUserEvento participacionUserEvento);

    @Query("SELECT * FROM participacionUserEvento WHERE idUser = :idUser AND idEvento = :idEvento")
    public ParticipacionUserEvento getParticipacion(long idUser, long idEvento);

    @Query("SELECT * FROM evento " +
            "JOIN participacionuserevento ON evento.id = participacionuserevento.idEvento " +
            "WHERE participacionuserevento.idUser = :idUser")
    public List<Evento> getAllParticipacionesByUser(long idUser);

    @Query("SELECT * FROM usuarios " +
            "JOIN participacionuserevento ON usuarios.id = participacionuserevento.idUser " +
            "WHERE participacionuserevento.idEvento = :idEvento")
    public List<User> getAllParticipacionesByEvento(long idEvento);

    @Update
    public int updateParticipacion(ParticipacionUserEvento participacionUserEvento);

    @Delete
    public void deleteParticipacion(ParticipacionUserEvento participacionUserEvento);

    @Query("DELETE FROM participacionuserevento WHERE idUser = :idUser AND idEvento = :idEvento")
    public void deleteParticipacion(long idUser, long idEvento);

    @Query("DELETE FROM ParticipacionUserEvento WHERE idUser = :idUser")
    public void deleteParticipacionByUser(long idUser);

    @Query("DELETE FROM ParticipacionUserEvento WHERE idEvento = :idEvento")
    public void deleteParticipacionByEvento(long idEvento);

    //PISTA

    @Insert(onConflict = REPLACE)
   public long insertPista(Pista pista);

    @Insert(onConflict = REPLACE)
    void bulkInsert(List<Pista> pistas);

    @Query("SELECT * FROM pista where id=(:idPista)")
    Pista getPistaById(long idPista);


    @Query("DELETE FROM pista")
    public void deleteAllPistas();

    //Migrate List<Repo> to LiveData<List<Repo>>
    //Ahora me devuelve un observable y tendre los datos de forma reactiva en mi interfaz siempre.
    @Query("SELECT * FROM pista")
    LiveData<List<Pista>> getLiveDataPistas();

    @Query("SELECT count(*) FROM pista")
    int getNumberPistas();


}