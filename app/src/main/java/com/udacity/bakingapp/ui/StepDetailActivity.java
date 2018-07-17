package com.udacity.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.util.Locale;

public class StepDetailActivity extends AppCompatActivity implements
        StepDetailFragment.ParentActivityCallback {
    private static final String TAG = StepDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";

    private Recipe mRecipe;
    private int mStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mRecipe = intent.getParcelableExtra(EXTRA_RECIPE);
            mStepIndex = intent.getIntExtra(EXTRA_STEP_INDEX, 0);

            StepDetailFragment stepFragment = new StepDetailFragment();
            stepFragment.setRecipe(mRecipe);
            stepFragment.setStepIndex(mStepIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepFragment)
                    .commit();
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mStepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
        }

        setTitle(titleWithStep(mRecipe, mStepIndex));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_STEP_INDEX, mStepIndex);
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

    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

}
