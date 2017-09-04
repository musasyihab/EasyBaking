package com.musasyihab.easybaking.network;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easybaking.data.MyProvider;
import com.musasyihab.easybaking.data.RecipeContract;
import com.musasyihab.easybaking.model.IngredientModel;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;

import java.util.List;

/**
 * Created by musasyihab on 9/3/17.
 */

public class TaskGetRecipeDetail extends AsyncTaskLoader<RecipeModel> {

    private Context context;
    private int id;

    public TaskGetRecipeDetail(Context context, int id) {
        super(context);
        this.context = context;
        this.id = id;
    }

    @Override
    public RecipeModel loadInBackground() {
        return MyProvider.getRecipe(context, id);
    }

}
