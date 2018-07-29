package com.galamdring.android.cookit;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;


import com.galamdring.android.cookit.Data.Recipe;

import org.hamcrest.core.AllOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void testStartUp(){

    }
    @Test
    public void testRecyclerViewPopulatedWithRecipeList(){
        List<Recipe> recipes = TestingDataUtils.GetRecipes();
        RecyclerViewInteraction.<Recipe>onRecyclerView(withId(R.id.recipeItemRecyclerView))
                .withItems(recipes)
                .check(new RecyclerViewInteraction.ItemViewAssertion<Recipe>() {
                    @Override
                    public void check(Recipe item, View view, NoMatchingViewException e) {
                        matches(hasDescendant(withText(item.getName()))).check(view,e);
                    }
                });
    }
    @Test
    public void testRecyclerViewClickLaunchesDetailActivity(){
        onView(withId(R.id.recipeItemRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Assert.assertEquals(TestingDataUtils.getActivityInstance().getClass(), recipeDetailActivity.class);

    }
}
