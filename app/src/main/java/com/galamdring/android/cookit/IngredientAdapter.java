package com.galamdring.android.cookit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Ingredient;

import java.text.DecimalFormat;
import java.util.List;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> Ingredients;
    private Context CreatorContext;

    public IngredientAdapter(Context context, List<Ingredient> ingredients){
        CreatorContext = context;
        Ingredients=ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(CreatorContext).inflate(R.layout.ingedient_item_nonwidget,parent,false);
        view.setFocusable(true);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = Ingredients.get(position);
        holder.measure.setText(ingredient.getMeasure());
        holder.quantity.setText(new DecimalFormat("#.##").format(ingredient.getQuantity()));
        holder.name.setText(ingredient.getDescription());

    }

    @Override
    public int getItemCount() {
        if(Ingredients==null || Ingredients.size()==0){
            return 0;
        }
        return Ingredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        Ingredients=ingredients;
        notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView quantity;
        TextView measure;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredientName);
            quantity=itemView.findViewById(R.id.ingredientQuantity);
            measure=itemView.findViewById(R.id.ingredientMeasure);

        }

    }
}
