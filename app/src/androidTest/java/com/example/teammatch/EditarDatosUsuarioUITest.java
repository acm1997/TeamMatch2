package com.example.teammatch.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.teammatch.R;
import com.example.teammatch.objects.Evento;
import com.example.teammatch.objects.User;
import com.example.teammatch.room_db.TeamMatchDataBase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditarDatosUsuarioUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    SharedPreferences.Editor preferencesEditor;
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


        //Nos creamos un usuario para poder realizar el test ya que require estar logueado
        u= new User("UserTest","UserTest@user.com","12345678","");
        evento_dataBase.getDao().insertUser(u);

        //Nos deslogueamos
        preferencesEditor.clear().apply();
        // Toast.makeText(getApplicationContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();
        //Ahora procedmos a loguearnos
        Login();
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
    public void shouldEditUser() throws InterruptedException {

        String testingUsername = "TestUsernameEdit";
        String testingEmail = "test@test";
        String testPassword = "pruebapassword";
        String testConfPassword = "pruebapassword";



        // click a boton añadir evento
        onView(withId(R.id.bEditP)).perform(click());
        // Perform typeText() and closeSoftKeyboard() actions on R.id.title
        onView(withId(R.id.editTextTextPersonName)).perform(clearText()).perform(typeText(testingUsername), closeSoftKeyboard());
        // Perform typeText() and closeSoftKeyboard() actions on R.id.title
        onView(withId(R.id.editTextTextEmailAddress)).perform(clearText()).perform(typeText(testingEmail), closeSoftKeyboard());
        // Perform typeText() and closeSoftKeyboard() actions on R.id.title
        onView(withId(R.id.editTextTextPassword)).perform(clearText()).perform(typeText(testPassword), closeSoftKeyboard());
        // Perform typeText() and closeSoftKeyboard() actions on R.id.title
        onView(withId(R.id.editTextTextPassword2)).perform(clearText()).perform(typeText(testConfPassword), closeSoftKeyboard());

        onView(withId(R.id.btn_save)).perform(click());




        Thread.sleep(5000);
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    public void deleteElements(){
        //Nos deslogueamos
        preferencesEditor.clear().apply();
        //Toast.makeText(getApplicationContext(), "Se ha cerrado la sesión", Toast.LENGTH_SHORT).show();

        //Borramos el usuario de prueba
        evento_dataBase.getDao().deleteUser(u);

    }



}




