package com.example.teammatch;

import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class UserUnitTest {
    public static User user1;


    @Before
    public void iniUser(){
        user1 = new User();
        user1.setId(0);
        user1.setUsername("userPrueba");
        user1.setEmail("prueba@prueba");
        user1.setPassword("testPrueba");
    }

    @Test
    public void testEmail(){
        assertNotNull(user1.getEmail());
        assertNotEquals(user1.getEmail(),"test@test");
        assertEquals(user1.getEmail(),"prueba@prueba");
    }

    @Test
    public void testPassword(){
        assertNotNull(user1.getPassword());
        assertNotEquals(user1.getPassword(),"pruebaError");
        assertEquals(user1.getPassword(),"testPrueba");
    }

    @Test
    public void testUserName(){
        assertNotNull(user1.getUsername());
        assertNotEquals(user1.getUsername(),"pruebaError");
        assertEquals(user1.getUsername(),"userPrueba");
    }
}
