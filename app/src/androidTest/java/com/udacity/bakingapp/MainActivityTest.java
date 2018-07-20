package com.udacity.bakingapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.IdlingResource.EspressoTestingIdlingResource;
import com.udacity.bakingapp.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String BROWNIES = "Brownies";
    private static final String BROWNIES_INGREDIENT =
            "350 g Bittersweet chocolate (60-70% cacao)\n" +
            "226 g unsalted butter\n" +
            "300 g granulated sugar\n" +
            "100 g light brown sugar\n" +
            "5 large eggs\n" +
            "1 tbsp vanilla extract\n" +
            "140 g all purpose flour\n" +
            "40 g cocoa powder\n" +
            "1.5 tsp salt\n" +
            "350 g semisweet chocolate chips";
    private static final String BROWNIES_STEP_2 =
            "2. Melt the butter and bittersweet chocolate together in a microwave or a double " +
            "boiler. If microwaving, heat for 30 seconds at a time, removing bowl and stirring " +
            "ingredients in between.";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = EspressoTestingIdlingResource.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkBrowniesExists() {
        // Checks that Brownie recipe exists in list
        onView(withText(BROWNIES)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeCard_OpensRecipeDetailActivity() {
        // Checks that the RecipeDetailActivity opens with the correct ingredients displayed
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.tv_ingredient_list)).check(matches(withText(BROWNIES_INGREDIENT)));
    }

    @Test
    public void clickStep_OpensStepDetailActivity() {
        // Checks that StepDetailActivity opens and correct details are displayed
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_step_list))
                .perform(RecyclerViewActions.scrollToPosition(2));
        onView(withId(R.id.rv_step_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.tv_step_description)).check(matches(withText(BROWNIES_STEP_2)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
