package com.galamdring.android.cookit.Data;

import android.content.Context;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class RecipeApi {

    public static final String BASE_URL = "http://go.udacity.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if(retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface RetrofitInterface{
        @GET("android-baking-app-json")
        Call<List<Recipe>> getRecipes();
    }
    /*
    RecipeDao dao = RecipeDatabase.getInstance(context).recipeDao();

                for(Recipe recipe : recipes){
        int recipeId = recipe.getId();
        for(Ingredient ingredient : recipe.getIngredients()){
            ingredient.setRecipeId(recipeId);
        }
        for(Step step :recipe.getSteps()){
            step.setRecipeId(recipeId);
        }
    }
                dao.insertRecipe(recipes);

                Log.e(RecipeApi.class.getSimpleName(),String.format("Failed to sync Recipes. %s",error.toString()));
                */
    public static void syncRecipes(final Context context){
        RetrofitInterface retrofitInterface =getClient()
                .create(RetrofitInterface.class);
        Call<List<Recipe>> call = retrofitInterface.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response != null) {
                    List<Recipe> recipes = response.body();
                    if(recipes!=null && recipes.size()>0) {
                        for (Recipe recipe : recipes) {
                            int recipeId = recipe.getId();
                            for (Ingredient ingredient : recipe.getIngredients()) {
                                ingredient.setRecipeId(recipeId);
                            }
                            for (Step step : recipe.getSteps()) {
                                step.setRecipeId(recipeId);
                            }
                        }
                        OurExecutors.getINSTANCE().getDbIO().execute(() -> {
                            RecipeDao dao = RecipeDatabase.getInstance(context).recipeDao();
                            dao.deleteAll();
                            dao.insertRecipe(recipes);
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(RecipeApi.class.getSimpleName(),String.format("Failed to sync Recipes. %s",t.toString()));
            }
        });
    }
}
