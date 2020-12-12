package com.example.teammatch;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Checkable;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.teammatch.activities.EventDetailsActivity;
import com.example.teammatch.activities.LoginActivity;
import com.example.teammatch.activities.MainActivity;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDAO;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.isA;

import androidx.test.espresso.contrib.RecyclerViewActions;


@LargeTest
public class CrearParticipacionUITest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    SharedPreferences.Editor preferencesEditor;
    TeamMatchDataBase evento_dataBase;
    Evento e;

    @Before
    public void before() throws Exception {
        //take shared preferences, if necessary
        Context targetContext = getInstrumentation().getTargetContext();
        preferencesEditor = targetContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();

        e =  new Evento("EventoTest",
                new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"), 1,
                "TextoPrueba", Evento.Deporte.valueOf("TENIS"), "PISTA PRUEBA",
                1, "", "", "");

        evento_dataBase = TeamMatchDataBase.getInstance(targetContext);
        evento_dataBase.getDao().deleteAllEventos();
        long id_evento = evento_dataBase.getDao().insertEvento(e);
        e.setId(id_evento);
    }

    @Test
    public void crearParticipacion() throws InterruptedException{
        preferencesEditor.putLong("id", 1);
        preferencesEditor.putString("username", "UserPrueba");
        preferencesEditor.commit();

        // Click item at position 3
        onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Thread.sleep(700);

        onView(withId(R.id.switch1)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(1000);

    }

    @After
    public void deleteElements(){
        evento_dataBase.getDao().deleteEvento(e);
    }

}
