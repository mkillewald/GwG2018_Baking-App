
package com.udacity.bakingapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private static final String NO_UNIT = "UNIT";

    private double quantity;
    private String measure;
    private String ingredient;

    private Ingredient(Parcel in) {
        this.quantity = in.readDouble();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {

        public Ingredient createFromParcel(Parcel in) { return new Ingredient(in); }

        public Ingredient[] newArray(int size) { return (new Ingredient[size]); }
    };

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    /**
     * Returns quantity, measure and ingredient
     * @return formatted String with quantity, measure and ingredient
     */
    public String formatQuantityAndMeasure() {
        StringBuilder builder = new StringBuilder();

        if (getQuantity() % 1 == 0 ) {
            builder.append((int) getQuantity());
        } else {
            builder.append(getQuantity());
        }

        builder.append(" ");

        if (!getMeasure().equals(NO_UNIT)) {
            builder.append(getMeasure().toLowerCase());
            builder.append(" ");
        }

        builder.append(getIngredient());

        return builder.toString();
    }

}
