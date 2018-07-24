package com.galamdring.android.cookit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final List<Step> Steps;
    private final Context CreatorContext;
    private StepClickHandler stepClickHandler;

    public StepAdapter(Context context, List<Step> steps, StepClickHandler handler){
        CreatorContext = context;
        Steps = steps;
        stepClickHandler=handler;
    }
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(CreatorContext).inflate(R.layout.step_item,parent,false);
        view.setFocusable(true);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = Steps.get(position);
        holder.id.setText(Integer.toString(step.getId()));
        holder.description.setText(step.getShortDescription());
        if(position%2==1){
            holder.containerLayout.setBackgroundColor(0x80E0EEEE);
        }
        else{
            holder.containerLayout.setBackgroundColor(0XFFFFFFFF);
        }

    }

    @Override
    public int getItemCount() {
        if(Steps==null || Steps.size()<1){
            return 0;
        }
        return Steps.size();
    }

    interface StepClickHandler{
        void onClick(Step step);
    }
    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id;
        TextView description;
        View containerLayout;

        public StepViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.stepItemId);
            description = itemView.findViewById(R.id.stepItemDescription);
            containerLayout = itemView.findViewById(R.id.stepItemContainer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step step = Steps.get(getAdapterPosition());
            stepClickHandler.onClick(step);
        }
    }
}
