package com.udacity.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnStepClickListener {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        if (findViewById(R.id.recipe_step_detail_layout) != null) {
            mTwoPane = true;
            StepDetailFragment fragment = new StepDetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onStepSelected(int stepIndex) {

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(EXTRA_RECIPE, mRecipe);
            arguments.putInt(EXTRA_STEP_INDEX, stepIndex);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE, mRecipe);
            intent.putExtra(EXTRA_STEP_INDEX, stepIndex);

            startActivity(intent);
        }
    }
}
