package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.udacity.bakingapp.activity.RecipeDetailActivity;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utility.RecipeJson;

import java.util.Locale;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String EXTRA_RECIPE = "com.udacity.bakingapp.model.Recipe";
    private static final String WIDGET_DETAILS = "sharedPreferenceWidgetDetails";
    private static final String KEY_RECIPE = "recipeJsonForWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WIDGET_DETAILS,
                Context.MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String recipeJson = sharedPreferences.getString(KEY_RECIPE, "");
        Recipe recipe = RecipeJson.parse(recipeJson);

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());
        views.setTextViewText(R.id.tv_widget_ingredients,
                recipe.getFormattedIngredientList(context));

        String servings = String.format(Locale.getDefault(),
                context.getString(R.string.recipe_servings),
                recipe.getServings());
        views.setTextViewText(R.id.tv_widget_servings, servings);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName widget = new ComponentName(context.getPackageName(),
                RecipeWidgetProvider.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(widget);
        onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);

        super.onReceive(context, intent);
    }
}

