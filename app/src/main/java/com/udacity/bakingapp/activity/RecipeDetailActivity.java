package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeDetailFragment;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnStepClickListener {
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

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mTwoPane = getResources().getBoolean(R.bool.isTablet);

            if (mTwoPane) {
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setRecipe(mRecipe);
                fragment.setTwoPane(mTwoPane);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, fragment)
                        .commit();
            }
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            mStepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            mTwoPane = savedInstanceState.getBoolean(EXTRA_TWO_PANE);
        }

        if (!mTwoPane) { setTitle(mRecipe.getName()); }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
