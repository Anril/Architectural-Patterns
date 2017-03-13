package com.example.architecturalpatterns.controllers;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import com.example.architecturalpatterns.R;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class CrudOperationsTest {

    private final String uniqueTitle = UUID.randomUUID().toString();
    private final String uniqueDesc = UUID.randomUUID().toString();

    @Rule
    public ActivityTestRule<TasksListActivity> tasksListActivityTestRule =
            new ActivityTestRule<>(TasksListActivity.class);

    @Test
    public void addTaskTest() {
        addTask(uniqueTitle, uniqueDesc);

        onView(allOf(withId(R.id.tv_task_title), withText(uniqueTitle)))
                .check(matches(withText(uniqueTitle)));
        onView(allOf(withId(R.id.tv_task_desc), withText(uniqueDesc)))
                .check(matches(withText(uniqueDesc)));

        deleteTask(uniqueTitle, uniqueDesc);
    }

    @Test
    public void editTaskTest() {
        addTask(uniqueTitle, uniqueDesc);

        onView(withText(uniqueTitle)).perform(click());

        String changedTitle = UUID.randomUUID().toString();
        String changedDesc = UUID.randomUUID().toString();

        ViewInteraction titleEditText = onView(withId(R.id.et_task_title));
        titleEditText.perform(replaceText(changedTitle));

        ViewInteraction descEditText = onView(withId(R.id.et_task_desc));
        descEditText.perform(replaceText(changedDesc));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.fab_done)).perform(click());
        onView(withText(changedTitle)).check(matches(isDisplayed()));

        deleteTask(changedTitle, changedDesc);
    }

    @Test
    public void deleteTaskTest() {
        addTask(uniqueTitle, uniqueDesc);

        deleteTask(uniqueTitle, uniqueDesc);

        onView(withText(uniqueTitle)).check(doesNotExist());
    }

    private void addTask(String title, String desc) {
        ViewInteraction addTaskFab = onView(withId(R.id.fab_add_task));
        addTaskFab.perform(click());

        ViewInteraction titleEditText = onView(withId(R.id.et_task_title));
        titleEditText.perform(replaceText(title));

        ViewInteraction descEditText = onView(withId(R.id.et_task_desc));
        descEditText.perform(replaceText(desc));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.fab_done)).perform(click());
    }

    private void deleteTask(String title, String desc) {
        onView(withText(title)).perform(click());
        onView(withId(R.id.menu_delete)).perform(click());
    }
}
