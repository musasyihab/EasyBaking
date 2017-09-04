package com.musasyihab.easybaking.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easybaking.model.IngredientModel;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musasyihab on 9/1/17.
 */

@ContentProvider(
        authority = MyProvider.AUTHORITY,
        database = MyDatabase.class)
public final class MyProvider {

    public static final String AUTHORITY = "android.example.com.squawker.provider.provider";


    @TableEndpoint(table = MyDatabase.RECIPE_TABLE)
    public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeContract.COLUMN_ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/recipes");

    }

    public static List<RecipeModel> getAllRecipes(Context context){
        List<RecipeModel> recipes = new ArrayList<>();

        Uri uri = Recipes.CONTENT_URI;

        Cursor result = context.getContentResolver().query(uri, RecipeContract.RECIPES_PROJECTION, null, null, null);

        int colId = result.getColumnIndex(RecipeContract.COLUMN_ID);
        int colName = result.getColumnIndex(RecipeContract.COLUMN_NAME);
        int colImage = result.getColumnIndex(RecipeContract.COLUMN_IMAGE);
        int colServings = result.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        int colIngredients = result.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS);
        int colSteps = result.getColumnIndex(RecipeContract.COLUMN_STEPS);
        int colWidget = result.getColumnIndex(RecipeContract.COLUMN_WIDGET_ADDED);

        // convert data from cursor to MovieModel
        while (result.moveToNext()) {
            RecipeModel recipe = new RecipeModel();
            recipe = new RecipeModel();
            recipe.setId(result.getInt(colId));
            recipe.setName(result.getString(colName));
            recipe.setImage(result.getString(colImage));
            recipe.setServings(result.getInt(colServings));
            List<IngredientModel> ingredients = new Gson().fromJson(result.getString(colIngredients),
                    new TypeToken<List<IngredientModel>>() {
                    }.getType());
            recipe.setIngredients(ingredients);
            List<StepModel> steps = new Gson().fromJson(result.getString(colSteps),
                    new TypeToken<List<StepModel>>() {
                    }.getType());
            recipe.setSteps(steps);
            boolean isWidgetAdded = result.getInt(colWidget) == 1;
            recipe.setWidgetAdded(isWidgetAdded);

            recipes.add(recipe);
        }

        result.close();

        return recipes;
    }

    public static List<RecipeModel> getAllRecipesWidget(Context context){
        List<RecipeModel> recipes = new ArrayList<>();

        Uri uri = Recipes.CONTENT_URI;

        String whereClause = RecipeContract.COLUMN_WIDGET_ADDED+"=?";
        String [] whereArgs = {"1"};

        Cursor result = context.getContentResolver().query(uri, RecipeContract.RECIPES_PROJECTION, whereClause, whereArgs, null);

        int colId = result.getColumnIndex(RecipeContract.COLUMN_ID);
        int colName = result.getColumnIndex(RecipeContract.COLUMN_NAME);
        int colImage = result.getColumnIndex(RecipeContract.COLUMN_IMAGE);
        int colServings = result.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        int colIngredients = result.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS);
        int colSteps = result.getColumnIndex(RecipeContract.COLUMN_STEPS);
        int colWidget = result.getColumnIndex(RecipeContract.COLUMN_WIDGET_ADDED);

        // convert data from cursor to MovieModel
        while (result.moveToNext()) {
            RecipeModel recipe = new RecipeModel();
            recipe = new RecipeModel();
            recipe.setId(result.getInt(colId));
            recipe.setName(result.getString(colName));
            recipe.setImage(result.getString(colImage));
            recipe.setServings(result.getInt(colServings));
            List<IngredientModel> ingredients = new Gson().fromJson(result.getString(colIngredients),
                    new TypeToken<List<IngredientModel>>() {
                    }.getType());
            recipe.setIngredients(ingredients);
            List<StepModel> steps = new Gson().fromJson(result.getString(colSteps),
                    new TypeToken<List<StepModel>>() {
                    }.getType());
            recipe.setSteps(steps);
            boolean isWidgetAdded = result.getInt(colWidget) == 1;
            recipe.setWidgetAdded(isWidgetAdded);

            recipes.add(recipe);
        }

        result.close();

        return recipes;
    }

    public static RecipeModel getRecipe(Context context, int id){
        RecipeModel recipe = null;

        Uri uri = Recipes.CONTENT_URI;

        String whereClause = RecipeContract.COLUMN_ID+"=?";
        String [] whereArgs = {id+""};

        Cursor result = context.getContentResolver().query(uri, RecipeContract.RECIPES_PROJECTION, whereClause, whereArgs, null);

        int colId = result.getColumnIndex(RecipeContract.COLUMN_ID);
        int colName = result.getColumnIndex(RecipeContract.COLUMN_NAME);
        int colImage = result.getColumnIndex(RecipeContract.COLUMN_IMAGE);
        int colServings = result.getColumnIndex(RecipeContract.COLUMN_SERVINGS);
        int colIngredients = result.getColumnIndex(RecipeContract.COLUMN_INGREDIENTS);
        int colSteps = result.getColumnIndex(RecipeContract.COLUMN_STEPS);
        int colWidget = result.getColumnIndex(RecipeContract.COLUMN_WIDGET_ADDED);

        // convert data from cursor to MovieModel
        while (result.moveToNext()) {
            recipe = new RecipeModel();
            recipe.setId(result.getInt(colId));
            recipe.setName(result.getString(colName));
            recipe.setImage(result.getString(colImage));
            recipe.setServings(result.getInt(colServings));
            List<IngredientModel> ingredients = new Gson().fromJson(result.getString(colIngredients),
                    new TypeToken<List<IngredientModel>>() {
                    }.getType());
            recipe.setIngredients(ingredients);
            List<StepModel> steps = new Gson().fromJson(result.getString(colSteps),
                    new TypeToken<List<StepModel>>() {
                    }.getType());
            recipe.setSteps(steps);
            boolean isWidgetAdded = result.getInt(colWidget) == 1;
            recipe.setWidgetAdded(isWidgetAdded);
        }

        result.close();

        return recipe;
    }

    public static void insertRecipe(Context context, RecipeModel recipe){

        String ingredients = new Gson().toJson(recipe.getIngredients());
        String steps = new Gson().toJson(recipe.getSteps());

        ContentValues newRecipe = new ContentValues();
        newRecipe.put(RecipeContract.COLUMN_ID, recipe.getId());
        newRecipe.put(RecipeContract.COLUMN_NAME, recipe.getName());
        newRecipe.put(RecipeContract.COLUMN_IMAGE, recipe.getImage());
        newRecipe.put(RecipeContract.COLUMN_SERVINGS, recipe.getServings());
        newRecipe.put(RecipeContract.COLUMN_INGREDIENTS, ingredients);
        newRecipe.put(RecipeContract.COLUMN_STEPS, steps);
        if(recipe.isWidgetAdded()) {
            newRecipe.put(RecipeContract.COLUMN_WIDGET_ADDED, 1);
        } else {
            newRecipe.put(RecipeContract.COLUMN_WIDGET_ADDED, 0);
        }
        context.getContentResolver().insert(MyProvider.Recipes.CONTENT_URI, newRecipe);
    }
}