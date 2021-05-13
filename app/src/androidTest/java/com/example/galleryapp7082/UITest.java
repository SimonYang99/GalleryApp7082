package com.example.galleryapp7082;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchLocation() {
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.LongitudeEditText)).perform(typeText("45.43"));
        onView(withId(R.id.LatitudeEditText)).perform(typeText("-122.6"));
        onView(withId(R.id.searchLocation)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
        onView(withId(R.id.leftButton)).perform(click());
    }

    @Test
    public void openSearch(){
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.startDate)).perform(typeText("1"));
        onView(withId(R.id.endDate)).perform(typeText("2"));
        onView(withId(R.id.SearchMenuButton)).perform(click());
        assertEquals(4, 2 + 3);

//        onView(withId(R.id.btnSearch)).perform(click());
//        onView(withId(R.id.etFromDateTime)).perform(typeText(""), closeSoftKeyboard());
//        onView(withId(R.id.etToDateTime)).perform(typeText(""), closeSoftKeyboard());
//        onView(withId(R.id.etKeywords)).perform(typeText("caption"), closeSoftKeyboard());
//        onView(withId(R.id.go)).perform(click());
//        onView(withId(R.id.etCaption)).check(matches(withText("caption")));
//        onView(withId(R.id.btnRight)).perform(click());
//        onView(withId(R.id.btnLeft)).perform(click());
    }

    @Test
    public void testSearch3(){
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.startDate)).perform(click());
        onView(withId(R.id.startDate)).perform(typeText("23:10:00"));
        onView(withId(R.id.endDate)).perform(click());
        onView(withId(R.id.endDate)).perform(typeText("24:00:00"));
        onView(withId(R.id.SearchMenuButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
        onView(withId(R.id.rightButton)).perform(click());
//        assertEquals(2, 2);

    }
}
