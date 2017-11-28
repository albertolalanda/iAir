package com.example.bruno.iair.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.bruno.iair.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ChangeCurrentLocationWithSwipeDown {

    @Rule
    public ActivityTestRule<DashBoardActivity> mActivityTestRule = new ActivityTestRule<>(DashBoardActivity.class);

    @Test
    public void changeCurrentLocationWithSwipeDown() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btnCurrentLocation), withText("Current Location"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewCityName), withText("Leiria"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutInfoo),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Leiria")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textViewCityName), withText("Leiria"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutInfoo),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Leiria")));

        //DoneByMockingGPS Coordinates
        /*ViewInteraction textView3 = onView(
                allOf(withText("Fake GPS"),
                        childAtPosition(
                                allOf(withId(com.lexa.fakegps.R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("Fake GPS")));*/

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textViewCityName), withText("Lisbon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutInfoo),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Lisbon")));

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
}
