package com.galamdring.android.cookit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final RecipeAdapterOnClickHandler itemClickHandler;
    Context ContextHolder;
    List<Recipe> Recipes;

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler handler){
        ContextHolder=context;
        itemClickHandler=handler;
    }

    public void setData(List<Recipe> recipes){
        Recipes=recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ContextHolder).inflate(R.layout.recipe_item, parent, false);
        view.setFocusable(true);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = Recipes.get(position);
        holder.recipeTitle.setText(recipe.getName());
        String pictureURL = recipe.getImage();
        if(!TextUtils.isEmpty(pictureURL)) {
            Picasso.with(ContextHolder).load(recipe.getImage()).into(holder.recipePhoto);
        }

    }

    @Override
    public int getItemCount() {
        if(Recipes==null) return 0;
        return Recipes.size();
    }

    public interface RecipeAdapterOnClickHandler{
        void onClick(Recipe recipe);
    }
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView recipePhoto;
        private TextView recipeTitle;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipePhoto=itemView.findViewById(R.id.recipeItemPicture);
            recipeTitle=itemView.findViewById(R.id.recipeItemTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = Recipes.get(position);
            itemClickHandler.onClick(recipe);
        }
    }
}
