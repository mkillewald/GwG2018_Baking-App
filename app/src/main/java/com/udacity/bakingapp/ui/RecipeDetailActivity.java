package com.udacity.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeDetailFragment;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnStepClickListener,
        StepDetailFragment.ParentActivityCallback {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";
    private static final String EXTRA_TWO_PANE = "twoPaneBoolean";

    private Recipe mRecipe;
    private int mStepIndex;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

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
            mStepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            mTwoPane = savedInstanceState.getBoolean(EXTRA_TWO_PANE);
        }

        if (mTwoPane) {
            setTitle(titleWithStep(mRecipe, mStepIndex));
        } else {
            setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_STEP_INDEX, mStepIndex);
        outState.putBoolean(EXTRA_TWO_PANE, mTwoPane);
    }

    @Override
    public void onStepSelected(int stepIndex) {
        mStepIndex = stepIndex;

        if (mTwoPane) {
            setTitle(titleWithStep(mRecipe, mStepIndex));

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setRecipe(mRecipe);
            fragment.setStepIndex(mStepIndex);
            fragment.setTwoPane(mTwoPane);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE, mRecipe);
            intent.putExtra(EXTRA_STEP_INDEX, mStepIndex);

            startActivity(intent);
        }
    }

    public String titleWithStep(Recipe recipe, int index) {
        Step step = recipe.getSteps().get(index);

        if (index == 0) {
            return String.format(Locale.getDefault(),
                    getString(R.string.title_step_0), recipe.getName(),
                    step.getShortDescription());
        } else {
            return String.format(Locale.getDefault(),
                    getString(R.string.title_with_step), recipe.getName(), step.getId());
        }
    }

    @Override
    public void setStepIndex(int index) {
        mStepIndex = index;
    }
}
