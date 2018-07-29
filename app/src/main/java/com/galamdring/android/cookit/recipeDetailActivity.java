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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Recipe recipe = getIntent().getParcelableExtra("Recipe");
        PopulateUI(recipe);
    }

    private void PopulateUI(Recipe recipe) {
        if(recipe==null) return;
        if(recipe.getImage()!=null && !TextUtils.isEmpty(recipe.getImage())) {
            ImageView image = findViewById(R.id.recipePhoto);
            Picasso.with(this).load(recipe.getImage()).into(image);
        }

        setTitle(recipe.getName());

        TextView title = findViewById(R.id.recipeTitle);
        TextView servings = findViewById(R.id.recipeServings);
        title.setText(String.format("%s",recipe.getName()));
        servings.setText(String.format("%s",recipe.getServings()));

        RecyclerView ingredientRV = findViewById(R.id.ingredientRecyclerView);
        ingredientRV.setLayoutManager(new GridLayoutManager(this,1));
        ingredientRV.setAdapter(new IngredientAdapter(this,recipe.getIngredients()));

        RecyclerView stepRV = findViewById(R.id.stepRecyclerView);
        stepRV.setLayoutManager(new LinearLayoutManager(this));
        stepRV.setAdapter(new StepAdapter(this,recipe.getSteps(),this));
    }

    @Override
    public void onClick(Step step) {
        Intent stepDetailIntent = new Intent(this,stepDetailActivity.class);
        stepDetailIntent.putExtra("Step",step);
        startActivity(stepDetailIntent);
    }
}
