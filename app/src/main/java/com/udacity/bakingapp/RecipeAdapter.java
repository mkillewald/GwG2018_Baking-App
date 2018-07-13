package com.udacity.bakingapp;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.databinding.RecipeListItemBinding;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends
        RecyclerView.Adapter<RecipeAdapter.RecipeAdapterImageViewHolder> {

//    private final RecipeListActivity mParentActivity;
    private List<Recipe> mRecipes;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

//    private final boolean mTwoPane;
//    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Recipe recipe = (Recipe) view.getTag();
//            if (mTwoPane) {
//                Bundle arguments = new Bundle();
//                arguments.putParcelable(RecipeDetailFragment.EXTRA_RECIPE, recipe);
//                RecipeDetailFragment fragment = new RecipeDetailFragment();
//                fragment.setArguments(arguments);
//                mParentActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.recipe_detail_container, fragment)
//                        .commit();
//            } else {
//                Context context = view.getContext();
//                Intent intent = new Intent(context, RecipeDetailActivity.class);
//                intent.putExtra(RecipeDetailFragment.EXTRA_RECIPE, recipe);
//
//                context.startActivity(intent);
//            }
//        }
//    };

//    RecipeAdapter (RecipeListActivity parent, List<Recipe> recipes, boolean twoPane) {
//        mRecipes = recipes;
//        mParentActivity = parent;
//        mTwoPane = twoPane;
//    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        final RecipeListItemBinding mRecipeItemBinding;

        RecipeAdapterImageViewHolder(RecipeListItemBinding recipeListItemBinding) {
            super(recipeListItemBinding.getRoot());
            mRecipeItemBinding = recipeListItemBinding;
            mRecipeItemBinding.idText.setOnClickListener(this);
            mRecipeItemBinding.content.setOnClickListener(this);
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

        holder.mRecipeItemBinding.setRecipe(recipe);
        holder.mRecipeItemBinding.idText.setText(String.valueOf(recipe.getId()));
        holder.mRecipeItemBinding.content.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
    }
}
