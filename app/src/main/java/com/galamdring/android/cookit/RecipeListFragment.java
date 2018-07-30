package com.galamdring.android.cookit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.RecipeApi;
import com.galamdring.android.cookit.Data.RecipeViewModel;

import java.util.List;

public class RecipeListFragment extends Fragment{

    private static final String KEY_FOR_LAYOUT_STATE = "LAYOUT STATE KEY";
    Context ActivityContext;
    private RecipeViewModel recipeViewModel;
    private LiveData<List<Recipe>> RecipesContent;
    private Parcelable layoutState;
    private LinearLayoutManager manager;
    private RecipeAdapter Adapter;
    private RecyclerView rView;
    private RecipeAdapter.RecipeAdapterOnClickHandler RecipeSelectionHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_fragment_layout, container, false);
        ActivityContext = getActivity();
        RecipeApi.syncRecipes(ActivityContext);
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
        RecipesContent = recipeViewModel.getRecipeList();
        if(savedInstanceState!=null){
            layoutState = savedInstanceState.getParcelable(KEY_FOR_LAYOUT_STATE);
        }

        manager = new LinearLayoutManager(ActivityContext);
        Adapter = new RecipeAdapter(ActivityContext,RecipeSelectionHandler);
        RecipesContent.observe(this, recipes -> {
            Adapter.setData(recipes);
            if(layoutState!=null){
                manager.onRestoreInstanceState(layoutState);
            }
        });
        rView = view.findViewById(R.id.recipeItemRecyclerView);
        rView.setLayoutManager(manager);

        rView.setAdapter(Adapter);


        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable layoutState = manager.onSaveInstanceState();
        outState.putParcelable(KEY_FOR_LAYOUT_STATE, layoutState);
    }

    public void setRecipeSelectionHandler(RecipeAdapter.RecipeAdapterOnClickHandler recipeSelectionHandler) {
        RecipeSelectionHandler = recipeSelectionHandler;
        if(Adapter!=null) Adapter.setOnClickHandler(recipeSelectionHandler);
    }
}
