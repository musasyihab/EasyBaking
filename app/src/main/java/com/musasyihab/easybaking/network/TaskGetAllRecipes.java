package com.musasyihab.easybaking.network;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easybaking.data.MyProvider;
import com.musasyihab.easybaking.data.RecipeContract;
import com.musasyihab.easybaking.model.IngredientModel;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by musasyihab on 9/1/17.
 */

public class TaskGetAllRecipes extends AsyncTaskLoader<List<RecipeModel>> {

    private List<RecipeModel> response;
    private Context context;

    public TaskGetAllRecipes(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public List<RecipeModel> loadInBackground() {
        try {
            response = NetworkUtils.setupRetrofit().getAllRecipes().execute().body();
            for (int i=0; i<response.size(); i++){
                RecipeModel recipe = response.get(i);
                RecipeModel localData = MyProvider.getRecipe(context, recipe.getId());
                if(localData!=null && localData.isWidgetAdded()){
                    recipe.setWidgetAdded(true);
                } else {
                    recipe.setWidgetAdded(false);
                }

                MyProvider.insertRecipe(context, recipe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MyProvider.getAllRecipes(context);
    }

}
