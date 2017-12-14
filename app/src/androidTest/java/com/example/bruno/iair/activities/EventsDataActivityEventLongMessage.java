package com.example.bruno.iair.activities;


import android.support.test.espresso.DataInteraction;
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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EventsDataActivityEventLongMessage {

    @Rule
    public ActivityTestRule<DashBoardActivity> mActivityTestRule = new ActivityTestRule<>(DashBoardActivity.class);

    @Test
    public void eventsDataActivityEventLongMessage() {
        DataInteraction linearLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.cityList),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                1)))
                .atPosition(1);
        linearLayout.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.sendEventsBtn), withText("Send Events"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.layoutInfoo),
                                        7),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.editMessage),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        editText.check(matches(withText("")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.editMessage),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        editText2.check(matches(withText("")));

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.radioButtonEarthQuake), withText("Earthquake"),
                        childAtPosition(
                                allOf(withId(R.id.layoutEarthquake),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.editMessage), withText("There is a Earthquake in Lisboa"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        editText3.check(matches(withText("There is a Earthquake in Lisboa")));

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
