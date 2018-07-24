package com.galamdring.android.cookit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.RecipeApi;
import com.galamdring.android.cookit.Data.RecipeViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecipeViewModel recipeViewModel;
    private LiveData<List<Recipe>> RecipesContent;
    private RecipeAdapter Adapter;
    private String KEY_FOR_LAYOUT_STATE = "This is the key for the layout state.";
    private Parcelable layoutState;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecipeApi.syncRecipes(this);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        RecipesContent = recipeViewModel.getRecipeList();
        if(savedInstanceState!=null){
            layoutState = savedInstanceState.getParcelable(KEY_FOR_LAYOUT_STATE);
        }

        manager = new LinearLayoutManager(this);
        Adapter = new RecipeAdapter(this,this);
        RecipesContent.observe(this, recipes -> {
            Adapter.setData(recipes);
            if(layoutState!=null){
                manager.onRestoreInstanceState(layoutState);
            }
        });
        RecyclerView rView = findViewById(R.id.recipeItemRecyclerView);

        rView.setLayoutManager(manager);

        rView.setAdapter(Adapter);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable layoutState = manager.onSaveInstanceState();
        outState.putParcelable(KEY_FOR_LAYOUT_STATE, layoutState);
    }
    @Override
    public void onClick(Recipe recipe) {
        Intent recipeDetailIntent = new Intent(this, recipeDetailActivity.class);
        recipeDetailIntent.putExtra("Recipe",recipe);
        startActivity(recipeDetailIntent);
    }
}
