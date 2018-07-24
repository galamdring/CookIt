package com.galamdring.android.cookit.Data;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RecipeSpinnerAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> Recipes;
    private final Context CreatorContext;

    public RecipeSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> objects) {
        super(context, resource, objects);
        this.CreatorContext = context;
        this.Recipes = objects;
    }

    public void setRecipes(List<Recipe> recipes){
        Recipes=recipes;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(Recipes!=null) return Recipes.size();
        return 0;
    }

    @Nullable
    @Override
    public Recipe getItem(int position) {
        if(Recipes!=null) return Recipes.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView)super.getDropDownView(position,convertView,parent);
        label.setTextColor(Color.BLACK);
        label.setText(Recipes.get(position).getName());
        return label;
    }


}
