package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;

public class StepDetailActivity extends AppCompatActivity {

    private static final String TAG = StepDetailActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String EXTRA_STEP_INDEX = "stepIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
            int stepIndex = intent.getIntExtra(EXTRA_STEP_INDEX, 0);

            StepDetailFragment stepFragment = new StepDetailFragment();
            stepFragment.setRecipe(recipe);
            stepFragment.setStepIndex(stepIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, stepFragment)
                    .commit();
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
