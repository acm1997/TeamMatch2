package com.example.teammatch;

import com.example.teammatch.objects.Evento;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class EventoUnitTest {

    public static Evento ev1;
    @Before
    public void initClass (){
        ev1 = new Evento();
        ev1.setNombre("Partido Moctezuma");
        ev1.setDescripcion("a");
        ev1.setDeporte(Evento.Deporte.FUTBOL);
        ev1.setParticipantes(10);
    }

    @Test
    public void testEvento(){
        assertNotNull(ev1);
    }

    @Test
    public void testNombreEvento(){
        assertNotNull(ev1.getNombre());
        assertNotEquals(ev1.getNombre(),"Partido Universidad");
        assertEquals(ev1.getNombre(),"Partido Moctezuma");
    }

    @Test
    public void testDeporteEvento(){
        assertNotNull(ev1.getDeporte());
        assertNotEquals(ev1.getDeporte(), Evento.Deporte.BALONCESTO);
        assertNotEquals(ev1.getDeporte(), Evento.Deporte.VOLEIBOL);
        assertNotEquals(ev1.getDeporte(), Evento.Deporte.TENIS);
        assertEquals(ev1.getDeporte(),Evento.Deporte.FUTBOL);
    }

    @Test
    public void testParticipantesEvento(){
        Integer iValido = ev1.getParticipantes();
        Integer iNoValido = 0;
        assertNotNull(ev1.getParticipantes());
        assertNotEquals(ev1.getParticipantes(),iNoValido);
        assertEquals(ev1.getParticipantes(),iValido);
    }


    @Test
    public void testDescripcionEvento(){
        assertNotNull(ev1.getDescripcion());
        assertNotEquals(ev1.getDescripcion(),"Pachanga");
        assertEquals(ev1.getDescripcion(),"a");
    }

}
