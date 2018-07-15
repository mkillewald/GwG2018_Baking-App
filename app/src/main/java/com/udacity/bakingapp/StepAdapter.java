package com.udacity.bakingapp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.databinding.StepListItemBinding;
import com.udacity.bakingapp.model.Step;

import java.util.List;

public class StepAdapter extends
        RecyclerView.Adapter<StepAdapter.StepAdapterImageViewHolder> {

    private List<Step> mSteps;
    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class StepAdapterImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        final StepListItemBinding mStepItemBinding;

        StepAdapterImageViewHolder(StepListItemBinding stepListItemBinding) {
            super(stepListItemBinding.getRoot());
            mStepItemBinding = stepListItemBinding;
            mStepItemBinding.tvStepName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.onClick(step);
        }
    }

    @Override
    public StepAdapterImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StepListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.step_list_item, parent, false);

        StepAdapterImageViewHolder viewHolder = new StepAdapterImageViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepAdapterImageViewHolder holder, int position) {
        Step step = mSteps.get(position);

        holder.mStepItemBinding.setStep(step);
        holder.mStepItemBinding.tvStepId.setText(String.valueOf(step.getId()));
        holder.mStepItemBinding.tvStepName.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps == null ? 0 : mSteps.size();
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }
}
