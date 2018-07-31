package com.galamdring.android.cookit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.Step;
import com.squareup.picasso.Picasso;

public class RecipeDetailFragment extends Fragment {
    private static final String KEY_FOR_RECIPE_IN_ARGS = "One key to rule them all";
    ImageView image;
    Context ActivityContext;
    RecyclerView stepRV;
    TextView title;
    TextView servings;
    RecyclerView ingredientRV;
    private Recipe mRecipe;
    private StepAdapter.StepClickHandler mStepClickHandler;
    private StepAdapter mStepAdapter;
    private int ParentId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container !=null) container.removeAllViews();
        View view = inflater.inflate(R.layout.recipe_detail_fragment,container, false);
        ingredientRV = view.findViewById(R.id.ingredientRecyclerView);
        image = view.findViewById(R.id.recipePhoto);
        title = view.findViewById(R.id.recipeTitle);
        servings = view.findViewById(R.id.recipeServings);
        stepRV = view.findViewById(R.id.stepRecyclerView);
        ActivityContext = getActivity();
        if(mRecipe !=null)
            PopulateUI(mRecipe);

        return view;
    }

    public static RecipeDetailFragment instanceOf(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(KEY_FOR_RECIPE_IN_ARGS,recipe);
        RecipeDetailFragment frag = new RecipeDetailFragment();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipe=getArguments()!=null?getArguments().getParcelable(KEY_FOR_RECIPE_IN_ARGS):null;
        if(mStepClickHandler==null){
            try{
                setStepSelectionHandler((StepAdapter.StepClickHandler)getActivity());
            }
            catch(Exception ex){
                Log.e("RecipeDetailFragment","Failed to set StepClickHandler with parent Activity",ex);
            }
        }
    }

    private void PopulateUI(Recipe recipe) {
        if(recipe==null) return;
        if(recipe.getImage()!=null && !TextUtils.isEmpty(recipe.getImage())) {
            Picasso.with(ActivityContext).load(recipe.getImage()).into(image);
        }
        title.setText(String.format("%s",recipe.getName()));
        servings.setText(String.format("%s",recipe.getServings()));
        ingredientRV.setLayoutManager(new GridLayoutManager(ActivityContext,1));
        ingredientRV.setAdapter(new IngredientAdapter(ActivityContext,recipe.getIngredients()));
        stepRV.setLayoutManager(new LinearLayoutManager(ActivityContext));
        mStepAdapter = new StepAdapter(ActivityContext,recipe.getSteps(),mStepClickHandler);
        stepRV.setAdapter(mStepAdapter);
    }

    public void setData(Recipe recipe) {
        mRecipe = recipe;
        try{
            PopulateUI(recipe);
        }
        catch(Exception ex){
            Log.e(this.getClass().getSimpleName(),"Failed to populate UI.",ex);
        }
    }

    public void setStepSelectionHandler(StepAdapter.StepClickHandler handler) {
        mStepClickHandler = handler;
        if(mStepAdapter!=null) mStepAdapter.setClickHandler(handler);
    }

    public int getParentId() {
        return ParentId;
    }
}
