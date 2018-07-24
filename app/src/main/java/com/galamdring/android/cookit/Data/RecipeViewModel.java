package com.galamdring.android.cookit.Data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private LiveData<List<RecipeWithRelations>> recipeLiveList;
    private RecipeDatabase DATABASE;
    private LiveData<List<Recipe>> recipeList;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        DATABASE=RecipeDatabase.getInstance(application.getApplicationContext());
        recipeLiveList = DATABASE.recipeDao()._getRecipesWithRelations();
        recipeList = Transformations.map(recipeLiveList, (Function<List<RecipeWithRelations>, List<Recipe>>) Recipe::transformRecipeList);
    }



    public LiveData<List<Recipe>> getRecipeList() {
        return recipeList;
    }

    public LiveData<Recipe> getRecipeById(int id){
        LiveData<RecipeWithRelations> recipes = DATABASE.recipeDao().getRecipe(id);
        return Transformations.map(recipes, this::transformRecipe);
    }

    private Recipe transformRecipe(RecipeWithRelations recipeWithRelations) {
        Recipe recipe = recipeWithRelations.recipe;
        recipe.setIngredients(recipeWithRelations.getIngredients());
        recipe.setSteps(recipeWithRelations.getSteps());
        return recipe;
    }


}
