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

    void insertRecipe(List<Recipe> recipes){
        _insertRecipes(recipes);
        for(Recipe recipe:recipes){
            _insertIngredients(recipe.getIngredients());
            _insertSteps(recipe.getSteps());
        }
    }

    @Insert
    abstract void _insertRecipes(List<Recipe> recipes);

    @Insert
    abstract void _insertIngredients(List<Ingredient> ingredients);

    @Insert
    abstract void _insertSteps(List<Step> steps);

    void deleteAll(){
        deleteIngredients();
        deleteSteps();
        deleteRecipes();
    }

    @Query("Delete from Recipe")
    abstract void deleteRecipes();
    @Query("Delete from Ingredient")
    abstract void deleteIngredients();
    @Query("Delete from step")
    abstract void deleteSteps();


    //This is supposed to get the related objects as well.



    @Query("Select * from Recipe")
    @Transaction
    public abstract LiveData<List<RecipeWithRelations>> _getRecipesWithRelations();

    //This should get the steps and ingredients as well
    @Query("Select * from Recipe where id=:id")
    @Transaction
    abstract LiveData<RecipeWithRelations> getRecipe(int id);


}
