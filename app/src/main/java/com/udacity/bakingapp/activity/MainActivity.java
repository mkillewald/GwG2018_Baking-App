package com.udacity.bakingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.fragment.RecipeListFragment;
import com.udacity.bakingapp.model.Recipe;


// TODO add espresso UI tests
// TODO add comments and javadocs
// TODO run Lint

public class MainActivity extends AppCompatActivity implements
        RecipeListFragment.OnRecipeCardClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRecipeCardSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
