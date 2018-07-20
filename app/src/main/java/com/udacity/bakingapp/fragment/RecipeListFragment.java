package com.udacity.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.IdlingResource.EspressoTestingIdlingResource;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.RecipeAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utility.NetworkUtils;
import com.udacity.bakingapp.utility.RecipeListJson;

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

    OnRecipeCardClickListener mListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeCardClickListener {
        void onRecipeCardSelected(Recipe recipe);
    }

    public RecipeListFragment() {
        // Mandatory empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container,
                false);

        mRecyclerView = rootView.findViewById(R.id.rv_recipe_list);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numOfColumns = (int) (dpWidth / 300);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numOfColumns);

        mRecipeAdapter = new RecipeAdapter(getContext(), this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (savedInstanceState == null) {
            if (NetworkUtils.isNetworkOnline(getContext())) {
                try {
                    fetchRecipeList(NetworkUtils.getUrlfromString(RECIPES_URL));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        if (mListener != null) {
            mListener.onRecipeCardSelected(recipe);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mListener = (OnRecipeCardClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.implement_OnRecipeCardClickListener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void fetchRecipeList(URL url) throws IOException {
        EspressoTestingIdlingResource.increment();

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
                        NetworkUtils.displayNetworkNotRespondingAlert(getContext());
                    }
                });

                EspressoTestingIdlingResource.decrement();
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

                EspressoTestingIdlingResource.decrement();

            }
        });
    }
}
