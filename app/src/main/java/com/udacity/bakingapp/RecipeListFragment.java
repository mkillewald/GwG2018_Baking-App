package com.udacity.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.util.RecipeListJson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeListFragment extends Fragment implements
        RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = RecipeListFragment.class.getSimpleName();

    private static final String EXTRA_RECIPE_LIST = "recipeList";

    private static final String RECIPES_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipes;

    // Define a new interface that triggers a callback in the host activity
    OnRecipeCardClickListener mCallback;

    // OnRecipeCardClickListener interface, calls a method in the host activity
    // named onRecipeCardSelected
    public interface OnRecipeCardClickListener {
        void onRecipeCardSelected(Recipe recipe);
    }

    // Mandatory empty constructor
    public RecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnRecipeCardClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.implement_OnRecipeCardClickListener));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_recipe_list);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numOfColumns = (int) (dpWidth / 300);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numOfColumns);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (savedInstanceState == null) {
            try {
                fetchRecipeList(new URL(RECIPES_URL));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mRecipes = savedInstanceState.getParcelableArrayList(EXTRA_RECIPE_LIST);
            mRecipeAdapter.setRecipes(mRecipes);
        }

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_RECIPE_LIST, mRecipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Recipe recipe) {
        mCallback.onRecipeCardSelected(recipe);
    }

    void fetchRecipeList(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String results = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (results != null && !results.equals("")) {

                            mRecipes = (ArrayList<Recipe>) RecipeListJson.parse(results);

                            mRecipeAdapter.setRecipes(mRecipes);
                            mRecipeAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });
    }
}
