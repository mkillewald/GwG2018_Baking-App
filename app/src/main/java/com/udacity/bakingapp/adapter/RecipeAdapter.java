package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.databinding.RecipeListItemBinding;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;
import java.util.Locale;

public class RecipeAdapter extends
        RecyclerView.Adapter<RecipeAdapter.RecipeAdapterImageViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        final RecipeListItemBinding mBinding;

        RecipeAdapterImageViewHolder(RecipeListItemBinding recipeListItemBinding) {
            super(recipeListItemBinding.getRoot());
            mBinding = recipeListItemBinding;
            mBinding.cvRecipeListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            Recipe recipe = mRecipes.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }

    @Override
    public RecipeAdapterImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecipeListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.recipe_list_item, parent, false);

        RecipeAdapterImageViewHolder viewHolder = new RecipeAdapterImageViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterImageViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        int numberOfSteps = recipe.getSteps().size();
        int numberOfIngredients = recipe.getIngredients().size();

        holder.mBinding.setRecipe(recipe);
        holder.mBinding.tvRecipeName.setText(recipe.getName());
        String stepsWithIngredients = String.format(Locale.getDefault(),
                mContext.getString(R.string.recipe_steps), numberOfSteps, numberOfIngredients);
        holder.mBinding.tvNumberOfSteps.setText(stepsWithIngredients);
        String numberOfServings = String.format(Locale.getDefault(),
                mContext.getString(R.string.recipe_servings), recipe.getServings());
        holder.mBinding.tvRecipeServings.setText(numberOfServings);
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
    }
}
