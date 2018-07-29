package com.galamdring.android.cookit;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.galamdring.android.cookit.Data.Recipe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class detailActivityTest {
    //Create the activityRule, but don't start the activity, since we need to add the intent
    // which we will do in the before.
    @Rule
    public ActivityTestRule<recipeDetailActivity> detailActivityActivityTestRule
            = new ActivityTestRule<>(recipeDetailActivity.class,false,false);

    @Before
    public void setupIntentForDetailActivity(){
        Recipe recipe = TestingDataUtils.GetRecipes().get(0);
        Intent intent = new Intent();
        intent.putExtra("Recipe",recipe);
        detailActivityActivityTestRule.launchActivity(intent);
    }
    @Test
    public void testClickStepLaunchesStepDetailActivity(){
        onView(withId(R.id.stepRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Assert.assertEquals(TestingDataUtils.getActivityInstance().getClass(), stepDetailActivity.class);
    }
}
