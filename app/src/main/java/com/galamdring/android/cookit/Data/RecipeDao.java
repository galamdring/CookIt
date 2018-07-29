package com.galamdring.android.cookit.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RecipeDao {

    public void insertRecipe(List<Recipe> recipes){
        _insertRecipes(recipes);
        for(Recipe recipe:recipes){
            _insertIngredients(recipe.getIngredients());
            _insertSteps(recipe.getSteps());
        }
    }

    @Insert
    public abstract void _insertRecipes(List<Recipe> recipes);

    @Insert
    public abstract void _insertIngredients(List<Ingredient> ingredients);

    @Insert
    public abstract void _insertSteps(List<Step> steps);

    public void deleteAll(){
        deleteIngredients();
        deleteSteps();
        deleteRecipes();
    }

    @Query("Delete from Recipe")
    public abstract void deleteRecipes();
    @Query("Delete from Ingredient")
    public abstract void deleteIngredients();
    @Query("Delete from step")
    public abstract void deleteSteps();


    //This is supposed to get the related objects as well.



    @Query("Select * from Recipe")
    @Transaction
    public abstract LiveData<List<RecipeWithRelations>> _getRecipesWithRelations();

    //This should get the steps and ingredients as well
    @Query("Select * from Recipe where id=:id")
    @Transaction
    public abstract LiveData<RecipeWithRelations> getRecipe(int id);

    //This should get the steps and ingredients as well
    @Query("Select * from Recipe where id=:id")
    @Transaction
    public abstract RecipeWithRelations getRecipeForWidget(int id);


    @Query("select * from Ingredient where recipeId=:id")
    public abstract List<Ingredient> getIngredientsByRecipeIdForWidget(int id);
}
