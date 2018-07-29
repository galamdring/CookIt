package com.galamdring.android.cookit;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.galamdring.android.cookit.Data.Ingredient;
import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.RecipeDatabase;
import com.galamdring.android.cookit.Data.RecipeSpinnerAdapter;
import com.galamdring.android.cookit.Data.RecipeViewModel;


import java.util.List;


/**
 * The configuration screen for the {@link RecipeWidgetProvider RecipeWidgetProvider} AppWidget.
 */
public class RecipeWidgetProviderConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.galamdring.android.cookit.RecipeWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private Spinner RecipeSpinner;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = RecipeWidgetProviderConfigureActivity.this;

            Recipe recipe = mSpinnerAdapter.getItem(RecipeSpinner.getSelectedItemPosition());
            saveRecipePref(context,mAppWidgetId,recipe.getId());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    private List<Recipe> Recipes;
    private RecipeSpinnerAdapter mSpinnerAdapter;
    private static RecipeViewModel recipeViewModel;

    public RecipeWidgetProviderConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, int Recipeid) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId+"_recipeId", Recipeid );
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId+"_recipeId", 0);
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId+"_recipeId");
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_provider_configure);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        RecipeSpinner = findViewById(R.id.recipeSelectSpinner);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        recipeViewModel.getRecipeList()
            .observe(this, this::onChanged);

        mSpinnerAdapter = new RecipeSpinnerAdapter(this,android.R.layout.simple_spinner_item, Recipes);
        RecipeSpinner.setAdapter(mSpinnerAdapter);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }


    private void onChanged(List<Recipe> recipes) {
        Recipes = recipes;
        mSpinnerAdapter.setRecipes(recipes);
    }

    public static LiveData<Recipe> loadRecipe(Context context, int appWidgetId) {
        int id = loadRecipePref(context,appWidgetId);
        return recipeViewModel.getRecipeById(id);
    }

    public static List<Ingredient> loadIngredients(Context context, int appWidgetId){
        int id = loadRecipePref(context,appWidgetId);
        return recipeViewModel.getIngredientsByRecipeIdForWidget(id);
    }
}

