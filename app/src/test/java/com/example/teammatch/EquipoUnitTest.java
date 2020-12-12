package com.example.teammatch;

import com.example.teammatch.objects.Equipo;
import com.example.teammatch.objects.Evento;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class EquipoUnitTest {

    public static Equipo eq1;
    @Before
    public void initClass (){
        eq1 = new Equipo();
        eq1.setId(1);
        eq1.setNombre("Barcelona");
        eq1.setDescripcion("Equipo de futbol");
        eq1.setMiembros(10);
    }

    @Test
    public void testEquipo(){
        assertNotNull(eq1);
    }

    @Test
    public void testNombreEquipo(){
        assertNotNull(eq1.getNombre());
        assertNotEquals(eq1.getNombre(),"Partido Universidad");
        assertEquals(eq1.getNombre(),"Barcelona");
    }

    @Test
    public void testDescripcion(){
        assertNotNull(eq1.getDescripcion());
        assertNotEquals(eq1.getDescripcion(),"Partido Universidad");
        assertEquals(eq1.getDescripcion(),"Equipo de futbol");
    }

    @Test
    public void testParticipantes(){
        Integer miembrosValido = eq1.getMiembros();
        Integer miembrosNoValido = 5;

        assertNotNull(eq1.getMiembros());
        assertNotEquals(eq1.getMiembros(),miembrosNoValido);
        assertEquals(eq1.getMiembros(),miembrosValido);
    }
}
