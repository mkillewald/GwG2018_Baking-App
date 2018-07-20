
package com.udacity.bakingapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    private Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {

        @Override
        public Recipe createFromParcel(Parcel in) { return new Recipe(in); }

        @Override
        public Recipe[] newArray(int size) { return (new Recipe[size]); }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    /**
     * Returns formatted list of ingredients
     * @return the formatted String list of ingredients
     */
    public String getformattedIngredientList(Context context) {
        List<Ingredient> ingredients = getIngredients();
        StringBuilder builder = new StringBuilder();
        int listSize = ingredients.size();

        if (listSize > 0 ) {
            for (int i = 0; i < listSize; i++) {
                builder.append(ingredients.get(i).formatQuantityAndMeasure(context));
                if (i != listSize - 1) {
                    builder.append("\n");
                }
            }
        }

        return builder.toString();
    }

}
