package pl.edu.pwr.lab1.i236764;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppEspressoTest {

    private String massInput = "50";
    private String heightInput = "160";
    private String result = "Your BMI is ";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    //TO-DO
    @Ignore
    @Test
    public void changeText_sameActivity() {
        // when
        onView(withId(R.id.massInput))
                .perform(typeText(massInput), closeSoftKeyboard());
        onView(withId(R.id.heightInput))
                .perform(typeText(heightInput), closeSoftKeyboard());
        onView(withId(R.id.button_first)).perform(click());

        // then
        onView(withId(R.id.result))
                .check(matches(withText(result)));
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("pl.edu.pwr.lab1.i236764", appContext.getPackageName());
    }
}