package com.galamdring.android.cookit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.galamdring.android.cookit.Data.Step;
import com.squareup.picasso.Picasso;

public class recipeDetailActivity extends AppCompatActivity implements StepAdapter.StepClickHandler {

    private RecipeDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Recipe recipe = getIntent().getParcelableExtra("Recipe");
        mDetailFragment = new RecipeDetailFragment();
        mDetailFragment.setData(recipe);
        mDetailFragment.setStepSelectionHandler(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_fragment_solo,mDetailFragment).commit();
    }


    @Override
    public void onClick(Step step) {
        Intent stepDetailIntent = new Intent(this,stepDetailActivity.class);
        stepDetailIntent.putExtra("Step",step);
        startActivity(stepDetailIntent);
    }
}
