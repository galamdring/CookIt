package com.galamdring.android.cookit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.View;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.RecipeApi;
import com.galamdring.android.cookit.Data.RecipeViewModel;
import com.galamdring.android.cookit.Data.Step;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler,StepAdapter.StepClickHandler{

    private static final String RECIPE_FRAGMENT_BUNDLE_KEY = "RecipeListFragment";
    private static final String RECIPE_FRAGMENT_TAG = "RECIPE_FRAGMENT_TAG";
    private static final String DETAIL_FRAGMENT_TAG = "DETAIL_FRAGMENT_TAG";
    private RecipeViewModel recipeViewModel;
    private LiveData<List<Recipe>> RecipesContent;
    private RecipeAdapter Adapter;
    private String KEY_FOR_LAYOUT_STATE = "This is the key for the layout state.";
    private Parcelable layoutState;
    RecyclerView.LayoutManager manager;
    private RecipeListFragment mRecipeFragment;
    private RecipeDetailFragment mDetailFragment;
    private String RECIPE_DETAIL_BUNDLE_KEY = "This is the key for the detail fragment in the bundle.";
    private boolean mDualPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.recipe_detail_fragment);
        mDualPane = (view != null && view.getVisibility()==View.VISIBLE);
        if(savedInstanceState!=null){
            mRecipeFragment = (RecipeListFragment) getSupportFragmentManager().getFragment(savedInstanceState,RECIPE_FRAGMENT_BUNDLE_KEY);

            if(mDualPane){
                RecipeDetailFragment detailFragment = (RecipeDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState,RECIPE_DETAIL_BUNDLE_KEY);
                if(detailFragment!=null){
                    mDetailFragment = detailFragment;
                }
            }
        }
        else {
            mRecipeFragment = new RecipeListFragment();

            RecipeListFragment existingFragment = (RecipeListFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_list_fragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(existingFragment!=null){
                transaction.replace(existingFragment.getId(), mRecipeFragment,RECIPE_FRAGMENT_TAG).commit();
            }
            else{
                transaction.replace(R.id.recipe_list_fragment,mRecipeFragment,RECIPE_FRAGMENT_TAG).commit();
            }

        }
        mRecipeFragment.setRecipeSelectionHandler(this);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,RECIPE_FRAGMENT_BUNDLE_KEY,mRecipeFragment);
        if(mDualPane)getSupportFragmentManager().putFragment(outState,RECIPE_DETAIL_BUNDLE_KEY,mDetailFragment);
    }

    @Override
    public void onClick(Recipe recipe) {
        if(mDualPane){
            mDetailFragment = new RecipeDetailFragment();
            mDetailFragment.setData(recipe);
            mDetailFragment.setStepSelectionHandler(this);
            RecipeDetailFragment existingFragment = (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_detail_fragment);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(existingFragment!=null){
                transaction.replace(existingFragment.getId(), mDetailFragment,DETAIL_FRAGMENT_TAG).commit();
            }
            else{
                transaction.replace(R.id.recipe_detail_fragment,mDetailFragment,DETAIL_FRAGMENT_TAG).commit();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_fragment,mDetailFragment).commit();
        }
        else{
        Intent recipeDetailIntent = new Intent(this, recipeDetailActivity.class);
        recipeDetailIntent.putExtra("Recipe",recipe);
        startActivity(recipeDetailIntent);
        }
    }

    @Override
    public void onClick(Step step) {
        Intent stepDetailIntent = new Intent(this,stepDetailActivity.class);
        stepDetailIntent.putExtra("Step",step);
        startActivity(stepDetailIntent);
    }
}
