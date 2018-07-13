package com.udacity.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.udacity.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailFragment.OnFragmentInteractionListener {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();

    public static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);

        if (findViewById(R.id.recipe_step_detail_layout) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
