
package com.udacity.bakingapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.bakingapp.R;

public class Ingredient implements Parcelable {

    private static final String JSON_UNIT = "UNIT";
    private static final String JSON_TBLSP = "TBLSP";
    private static final String JSON_K = "K";


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
    public String formatQuantityAndMeasure(Context context) {

        StringBuilder builder = new StringBuilder();

        if (getQuantity() % 1 == 0 ) {
            builder.append((int) getQuantity());
        } else {
            builder.append(getQuantity());
        }

        builder.append(" ");

        switch(getMeasure()) {
            case JSON_UNIT:
                // no unit of measure to display, so do nothing
                break;
            case JSON_TBLSP:
                builder.append(context.getString(R.string.tbsp_unit));
                builder.append(" ");
                break;
            case JSON_K:
                builder.append(context.getString(R.string.kg_unit));
                builder.append(" ");
                break;
            default:
                builder.append(getMeasure().toLowerCase());
                builder.append(" ");
        }

        builder.append(getIngredient());

        return builder.toString();
    }

}
