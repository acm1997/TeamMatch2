package com.example.teammatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.teammatch.activities.MainActivity;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestCU05 {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    SharedPreferences.Editor preferencesEditor;
    Evento e;
    User u;
    TeamMatchDataBase evento_dataBase;
    long id_evento;
    @Before
    public void before() throws Exception {
        //take shared preferences, if necessary
        Context targetContext = getInstrumentation().getTargetContext();
        preferencesEditor = targetContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        evento_dataBase = TeamMatchDataBase.getInstance(targetContext);
        //Borranos todos los eventos que haya en la BD
        evento_dataBase.getDao().deleteAllEventos();

        //nos creamos un evento que será el que veamos en nuestro perfil
        e =  new Evento("EventoTest1",
                new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"), 1,
                "TextoPrueba", Evento.Deporte.TENIS, "PISTA PRUEBA", 1,
                "", "", "");

        //Nos creamos un usuario para poder realizar el test ya que require estar logueado
        u= new User("UserTest","UserTest@user.com","12345678","");
        evento_dataBase.getDao().insertUser(u);

        //Nos deslogueamos
        preferencesEditor.clear().apply();
        // Toast.makeText(getApplicationContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
        //Ahora procedmos a loguearnos
        Login();
        onView(withId(R.id.ic_home)).perform(click());

        //insertamos en BD el evento
        id_evento = evento_dataBase.getDao().insertEvento(e);
        e.setId(id_evento);

        // para iniciar el Test desde HOME
        onView(withId(R.id.ic_home)).perform(click());
    }

    public void Login() {
        String user="UserTest";
        String pass="12345678";
        openContextualActionModeOverflowMenu();
        onView(withText(R.string.action_login)).perform(click());
        onView(withId(R.id.et_email_username)).perform(typeText(user), closeSoftKeyboard());
        onView(withId(R.id.et_password)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
    }
    @Test
    public void VerUsuarioConEventos() throws InterruptedException {

        //Desde home vamos a ver el perfil
        onView(withId(R.id.ic_usuario)).perform(click());
        Thread.sleep(2000);
    }
    @After
    public void deleteElements(){
        //Borramos el evento de prueba
        evento_dataBase.getDao().deleteEvento(e);

        //Nos deslogueamos
        preferencesEditor.clear().apply();
        //Toast.makeText(getApplicationContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();

        //Borramos el usuario de prueba
        evento_dataBase.getDao().deleteUser(u);

    }
}