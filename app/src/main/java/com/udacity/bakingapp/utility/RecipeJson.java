package com.udacity.bakingapp.utility;

import com.google.gson.Gson;
import com.udacity.bakingapp.model.Recipe;

public class RecipeJson {
    Recipe mRecipe;

    public RecipeJson(Recipe recipe) {
        mRecipe = recipe;
    }

    public static Recipe parse(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, Recipe.class);
    }
}
