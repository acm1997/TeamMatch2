package com.example.teammatch;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.rule.ActivityTestRule;

import com.example.teammatch.activities.MainActivity;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CrearEquipoUITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldInsertTeam(){
        String testingTeamName = "EquipoTest01";
        String testingTeamMembers = "10";
        String testingTeamDescription = "Equipo testing number one";

        onView(withId(R.id.tagNombreEquipo)).perform(typeText(testingTeamName), closeSoftKeyboard());
        onView(withId(R.id.tagNumMiembros)).perform(typeText(testingTeamMembers), closeSoftKeyboard());
        onView(withId(R.id.tagDescripcionEquipo)).perform(typeText(testingTeamDescription), closeSoftKeyboard());

        //Submit
        onView(withId(R.id.buttonCrearEquipo)).perform(click());
    }

    @Before
    public void GoTeams(){
        SharedPreferences preferences = (SharedPreferences) mActivityRule.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Long usuario_id = preferences.getLong("usuario_id", 0);
        String name = preferences.getString("username", null);
        String email = preferences.getString("email", null);
        String password = preferences.getString("password", null);
        if (usuario_id > 0 && name != null && email != null && password != null) {

            onView(withId(R.id.ic_equipos)).perform(click());
            onView(withId(R.id.fabNuevoEquipo)).perform(click());
            onView(withId(R.id.cancelButton2)).perform(click());
            onView(withId(R.id.fabNuevoEquipo)).perform(click());
        }
    }

    @After
    public void DeleteTeams() throws InterruptedException {
        Thread.sleep(1500);

        onView(withId(R.id.imageDeleteTeam)).perform(click());
        onView(withId(R.id.ic_equipos)).perform(click());

    }
}
