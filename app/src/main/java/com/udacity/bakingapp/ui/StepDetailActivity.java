package com.udacity.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

public class StepDetailActivity extends AppCompatActivity implements
        StepDetailFragment.TitleStringListener{
    private static final String TAG = StepDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
            int stepIndex = intent.getIntExtra(EXTRA_STEP_INDEX, 0);
            Step step = recipe.getSteps().get(stepIndex);

            setTitle(buildStepTitle(recipe.getName(), step.getId()));

            StepDetailFragment stepFragment = new StepDetailFragment();
            stepFragment.setRecipe(recipe);
            stepFragment.setStepIndex(stepIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepFragment)
                    .commit();
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
