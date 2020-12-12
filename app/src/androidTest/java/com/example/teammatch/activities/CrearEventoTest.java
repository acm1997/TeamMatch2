package com.example.teammatch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CrearEventoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    SharedPreferences.Editor preferencesEditor;
    Intent intent;
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

        //nos creamos un evento que será el que editemos
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
    public void shouldTestInterfaceCrearEvento(){

        String testingString = "Test Text";
        Integer testingParticipantes = 10;
        String testingDescripcion="Prueba descripcion";
        String testFecha = "31/12/2021";
        String testingHora = "18:00:00";


          // click a boton añadir evento
          onView(ViewMatchers.withId(R.id.fabNuevoEvento)).check(matches(isEnabled()));
          onView(withId(R.id.fabNuevoEvento)).perform(click());
          // Perform typeText() and closeSoftKeyboard() actions on R.id.title
          onView(withId(R.id.nombreEvento)).perform(typeText(testingString), closeSoftKeyboard());

          onView(withId(R.id.fecha)).perform(clearText()).perform(typeText(testFecha), closeSoftKeyboard());
          onView(withId(R.id.hora)).perform(clearText()).perform(typeText(testingHora), closeSoftKeyboard());

          //Chequeo radio buton.
          onView(withId(R.id.radioTenis))
                  .perform(click());

          onView(withId(R.id.radioFutbol))
                  .check(matches(not(isChecked())));

          onView(withId(R.id.radioBaloncesto))
                  .check(matches(not(isChecked())));

          onView(withId(R.id.radioVoleibol))
                  .check(matches(not(isChecked())));

          onView(withId(R.id.idSelecPista)).perform(click());
          onView(withId(R.id.idRecyclerPista)).check(matches(isDisplayed()));//se comprueba que se muestre recycler view pistas.
          onView(withId(R.id.idRecyclerPista)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

          onView(withId(R.id.numParticipantes)).perform(typeText(testingParticipantes.toString()), closeSoftKeyboard());
          onView(withId(R.id.descEvento)).perform(typeText(testingDescripcion), closeSoftKeyboard());
          onView(withId(R.id.buttonCrear)).perform(click());


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
