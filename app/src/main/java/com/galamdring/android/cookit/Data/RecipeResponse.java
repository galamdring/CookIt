package com.galamdring.android.cookit.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse {
    @SerializedName("")
    @Expose
    public List<Recipe> recipes;
}
