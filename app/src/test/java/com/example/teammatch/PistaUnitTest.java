package com.example.teammatch;

import com.example.teammatch.objects.Equipo;
import com.example.teammatch.objects.Pista;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class PistaUnitTest {
    public static Pista pista1;
    @Before
    public void initClass (){
        pista1 = new Pista();
        pista1.setId(1);
        pista1.setNombrePista("Espacio deportivo instalación: Circuito Automodelismo");
        pista1.setCallePista("Calle Carlos Callejo");
        pista1.setCiudadPista("Caceres");
        pista1.setGeoLatPista("39.469366377359485");
        pista1.setGeoLongPista("-6.39591783285141");
    }

    @Test
    public void testEquipo(){
        assertNotNull(pista1);
    }

    @Test
    public void testNombrePista(){
        assertNotNull(pista1.getNombrePista());
        assertNotEquals(pista1.getNombrePista(),"Moctezuma");
        assertEquals(pista1.getNombrePista(),"Espacio deportivo instalación: Circuito Automodelismo");
    }

    @Test
    public void testCallePista(){
        assertNotNull(pista1.getCallePista());
        assertNotEquals(pista1.getCallePista(),"Canovas");
        assertEquals(pista1.getCallePista(),"Calle Carlos Callejo");
    }

    @Test
    public void testCiudadPista(){
        assertNotNull(pista1.getCiudadPista());
        assertNotEquals(pista1.getCiudadPista(),"Badajoz");
        assertEquals(pista1.getCiudadPista(),"Caceres");
    }

    @Test
    public void testGeoLatPista(){
        assertNotNull(pista1.getGeoLatPista());
        assertNotEquals(pista1.getGeoLatPista(),"38.564456165");
        assertEquals(pista1.getGeoLatPista(),"39.469366377359485");
    }

    @Test
    public void testGeoLongPista(){
        assertNotNull(pista1.getGeoLongPista());
        assertNotEquals(pista1.getGeoLongPista(),"-4.51651");
        assertEquals(pista1.getGeoLongPista(),"-6.39591783285141");
    }
}
