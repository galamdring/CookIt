package com.galamdring.android.cookit;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.widget.RemoteViews;

import com.galamdring.android.cookit.Data.Ingredient;
import com.galamdring.android.cookit.Data.OurExecutors;
import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.RecipeDao;
import com.galamdring.android.cookit.Data.RecipeDatabase;
import com.galamdring.android.cookit.Data.RecipeViewModel;
import com.galamdring.android.cookit.Data.RecipeWithRelations;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetProviderConfigureActivity RecipeWidgetProviderConfigureActivity}
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        int recipeId = RecipeWidgetProviderConfigureActivity.loadRecipePref(context,appWidgetId);
        OurExecutors.getINSTANCE().getDbIO().execute(new Runnable() {
            @Override
            public void run() {
                RecipeDao dao = RecipeDatabase.getInstance(context).recipeDao();
                final RecipeWithRelations recipe = dao.getRecipeForWidget(recipeId);
                List<Ingredient> ingredients = dao.getIngredientsByRecipeIdForWidget(recipeId);
                if (recipe == null || ingredients == null) return;
                OurExecutors.getINSTANCE().getMainThread().execute(() -> {
                    // Construct the RemoteViews object
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
                    RemoteViews titleView = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_title_layout);
                    titleView.setTextViewText(R.id.recipe_widget_title_textView, recipe.recipe.getName());
                    views.addView(R.id.widget_layout, titleView);
                    for (Ingredient ingredient : ingredients) {
                        RemoteViews vw = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
                        vw.setTextViewText(R.id.ingredientQuantity, String.format("%s", ingredient.getQuantity()));
                        vw.setTextViewText(R.id.ingredientMeasure, ingredient.getMeasure());
                        vw.setTextViewText(R.id.ingredientName, ingredient.getDescription());
                        views.addView(R.id.widget_layout, vw);
                    }

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                });
            }
        });


    }

    private static Recipe getRecipeFromListById(List<Recipe> recipes, int recipeId){
        for(Recipe recipe:recipes){
            if(recipe.getId()==recipeId)
                return recipe;
        }
        return null;
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProviderConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

