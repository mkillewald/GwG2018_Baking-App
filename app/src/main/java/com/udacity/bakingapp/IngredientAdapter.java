package com.udacity.bakingapp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.databinding.IngredientListItemBinding;
import com.udacity.bakingapp.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends
        RecyclerView.Adapter<IngredientAdapter.IngredientAdapterImageViewHolder> {

//    private final RecipeListActivity mParentActivity;
    private List<Ingredient> mIngredients;
    private final IngredientAdapterOnClickHandler mClickHandler;

    public interface IngredientAdapterOnClickHandler {
        void onClick(Ingredient ingredient);
    }

    public IngredientAdapter(IngredientAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class IngredientAdapterImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        final IngredientListItemBinding mIngredientItemBinding;

        IngredientAdapterImageViewHolder(IngredientListItemBinding ingredientListItemBinding) {
            super(ingredientListItemBinding.getRoot());
            mIngredientItemBinding = ingredientListItemBinding;
            mIngredientItemBinding.tvIngredientName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            Ingredient ingredient = mIngredients.get(adapterPosition);
            mClickHandler.onClick(ingredient);
        }
    }

    @Override
    public IngredientAdapterImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IngredientListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.ingredient_list_item, parent, false);

        IngredientAdapterImageViewHolder viewHolder = new IngredientAdapterImageViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientAdapterImageViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        holder.mIngredientItemBinding.setIngredient(ingredient);
        holder.mIngredientItemBinding.tvIngredientName.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    public void setIngreients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }
}
