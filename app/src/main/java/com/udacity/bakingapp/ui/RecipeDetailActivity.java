package com.udacity.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeDetailFragment;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnStepClickListener,
        StepDetailFragment.TitleStringListener {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";
    private static final String EXTRA_TWO_PANE = "twoPaneBoolean";

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

            String title = buildStepTitle(mRecipe.getName(), 0);
            setTitle(title);

            if (findViewById(R.id.step_detail_container) != null) {
                mTwoPane = true;
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setRecipe(mRecipe);
                fragment.setTwoPane(mTwoPane);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                mTwoPane = false;
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mTwoPane = savedInstanceState.getBoolean(EXTRA_TWO_PANE);

            setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putBoolean(EXTRA_TWO_PANE, mTwoPane);
    }

    @Override
    public void onStepSelected(int stepIndex) {

        if (mTwoPane) {

            Step step = mRecipe.getSteps().get(stepIndex);
            String title = buildStepTitle(mRecipe.getName(), step.getId());
            setTitle(title);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setRecipe(mRecipe);
            fragment.setStepIndex(stepIndex);
            fragment.setTwoPane(mTwoPane);

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

    public String buildStepTitle(String recipeName, int stepNumber) {
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(recipeName);
        titleBuilder.append(getString(R.string.recipe_colon));

        if (stepNumber == 0) {
            titleBuilder.append(getString(R.string.introduction));
        } else {
            titleBuilder.append(getString(R.string.step));
            titleBuilder.append(stepNumber);
        }

        return titleBuilder.toString();
    }
}
