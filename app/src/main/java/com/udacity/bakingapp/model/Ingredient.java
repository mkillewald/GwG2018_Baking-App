
package com.udacity.bakingapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.bakingapp.R;

import java.util.Locale;

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

        String outQuantity;
        if (getQuantity() % 1 == 0 ) {
            // display whole number only if no fraction exists
            outQuantity = String.valueOf((int) getQuantity());
        } else {
            outQuantity = String.valueOf(getQuantity());
        }

        String outMeasure;
        switch(getMeasure()) {
            case JSON_UNIT:
                // no unit of measure to display
                outMeasure = "";
                break;
            case JSON_TBLSP:
                outMeasure = context.getString(R.string.tbsp_unit);
                break;
            case JSON_K:
                outMeasure = context.getString(R.string.kg_unit);
                break;
            default:
                outMeasure = getMeasure().toLowerCase() + " ";
        }

        String format = context.getString(R.string.quantity_measure_ingredient);
        return String.format(Locale.getDefault(), format, outQuantity, outMeasure, getIngredient());
    }

}
