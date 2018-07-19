package com.udacity.bakingapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.databinding.StepListItemBinding;
import com.udacity.bakingapp.model.Step;

import java.util.List;

public class StepAdapter extends
        RecyclerView.Adapter<StepAdapter.StepAdapterImageViewHolder> {
    private static final String TAG = StepAdapter.class.getSimpleName();

    private List<Step> mSteps;
    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(int stepIndex);
    }

    public StepAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class StepAdapterImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        final StepListItemBinding mBinding;

        StepAdapterImageViewHolder(StepListItemBinding stepListItemBinding) {
            super(stepListItemBinding.getRoot());
            mBinding = stepListItemBinding;
            mBinding.cvStepListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.onClick(adapterPosition);
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

        holder.mBinding.setStep(step);

        if (!step.getThumbnailURL().isEmpty()) {
            Picasso.with(holder.itemView.getContext())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.ic_spoon)
                    .into(holder.mBinding.ivStepThumbnail);
        }

        String description = null;
        if (step.getId() == 0) {
            description = step.getShortDescription();
        } else {
            description = step.getId() + ". " + step.getShortDescription();
        }
        holder.mBinding.tvStepName.setText(description);
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
