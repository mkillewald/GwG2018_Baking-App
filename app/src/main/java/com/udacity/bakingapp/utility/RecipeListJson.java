package com.udacity.bakingapp.utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.bakingapp.model.Recipe;

import java.lang.reflect.Type;
import java.util.List;

public class RecipeListJson {

    List<Recipe> mRecipes;

    public RecipeListJson(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    public static List<Recipe> parse(String json) {
        Type collectionType = new TypeToken<List<Recipe>>(){}.getType();
        Gson gson = new Gson();

        return gson.fromJson(json, collectionType);
    }
}
