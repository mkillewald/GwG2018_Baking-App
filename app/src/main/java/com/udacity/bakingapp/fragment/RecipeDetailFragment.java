package com.udacity.bakingapp.fragment;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.StepAdapter;
import com.udacity.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;
import java.util.Locale;

public class RecipeDetailFragment extends Fragment implements
        StepAdapter.StepAdapterOnClickHandler {
    
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String WIDGET_DETAILS = "sharedPreferenceWidgetDetails";
    private static final String KEY_RECIPE = "recipeJsonForWidget";

    private Recipe mRecipe;
    private OnStepClickListener mListener;
    
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepClickListener {
        void onStepSelected(int stepIndex);
    }

    public RecipeDetailFragment() {
        // Mandatory empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
            }

            SharedPreferences sharedPreferences = getContext().getSharedPreferences(WIDGET_DETAILS,
                    Context.MODE_MULTI_PROCESS);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String recipeJson = new GsonBuilder().create().toJson(mRecipe, Recipe.class);
            editor.putString(KEY_RECIPE, recipeJson);
            editor.apply();

            Intent widgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            getContext().sendBroadcast(widgetIntent);
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentRecipeDetailBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);

        final View rootView = binding.getRoot();

        binding.tvIngredientList.setText(formatIngredientList());

        String numberOfServings = String.format(Locale.getDefault(),
                getString(R.string.recipe_servings), mRecipe.getServings());
        binding.tvServings.setText(numberOfServings);

        StepAdapter stepAdapter = new StepAdapter(this);
        stepAdapter.setSteps(mRecipe.getSteps());

        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvStepList.setLayoutManager(stepLayoutManager);
        binding.rvStepList.setAdapter(stepAdapter);
        binding.rvStepList.setNestedScrollingEnabled(false);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
    }

    @Override
    public void onClick(int stepIndex) {
        if (mListener != null) {
            mListener.onStepSelected(stepIndex);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.implement_OnStepClickListener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Returns formatted list of ingredients
     * @return the formatted String list of ingredients
     */
    private String formatIngredientList() {
        List<Ingredient> ingredients = mRecipe.getIngredients();
        StringBuilder builder = new StringBuilder();
        int listSize = ingredients.size();

        if (listSize > 0 ) {
            for (int i = 0; i < listSize; i++) {
                builder.append(ingredients.get(i).formatQuantityAndMeasure(getContext()));
                if (i != listSize - 1) {
                    builder.append("\n");
                }
            }
        }

        return builder.toString();
    }

}
