package com.example.teammatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.teammatch.activities.MainActivity;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)

public class DeleteEventUITest {

        @Rule
        public ActivityTestRule<MainActivity> mActivityRule =
                new ActivityTestRule<>(MainActivity.class);
        SharedPreferences.Editor preferencesEditor;
        Evento e;
        TeamMatchDataBase evento_dataBase;
        long id_evento;

        @Before
        public void before() throws Exception {
            //take shared preferences, if necessary
            Context targetContext = getInstrumentation().getTargetContext();
            preferencesEditor = targetContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();

            TeamMatchDataBase evento_dataBase;
            evento_dataBase = TeamMatchDataBase.getInstance(targetContext);
            evento_dataBase.getDao().deleteAllEventos();

            e =  new Evento("EventoTest1",
                    new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"), 1,
                    "TextoPrueba", Evento.Deporte.TENIS, "PISTA PRUEBA", 1,
                    "39.461263", "-6.378087", "");

            evento_dataBase = TeamMatchDataBase.getInstance(targetContext);
            id_evento = evento_dataBase.getDao().insertEvento(e);
            e.setId(id_evento);
        }

        @Test
        public void ShouldDeleteEventFromDatabase() throws InterruptedException {
            preferencesEditor.putLong("id", 1);
            preferencesEditor.putString("username", "UserTest");
            preferencesEditor.putString("email", "UserTest@user.com");
            preferencesEditor.putString("password", "12345678");
            preferencesEditor.commit();

                onView(withId(R.id.my_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            Thread.sleep(2000);
            onView(withId(R.id.btn_deleteEvent)).perform(click());
            Thread.sleep(2000);
            onView(withText("Aceptar")).perform(click());
            Thread.sleep(2000);
        }
}
