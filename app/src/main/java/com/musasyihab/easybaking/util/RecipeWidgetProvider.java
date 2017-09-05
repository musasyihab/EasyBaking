package com.musasyihab.easybaking.util;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.data.MyProvider;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.ui.RecipeListActivity;
import com.musasyihab.easybaking.ui.RecipeStepActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews rv;
        rv = getRecipeSingleRemoteView(context);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i<appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startActionUpdateRecipeWidgets(context);
    }

    private static RemoteViews getRecipeSingleRemoteView(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeId =  preferences.getInt(Constants.SAVED_TO_WIDGET_RECIPE_ID, -1);

        RecipeModel mRecipe = MyProvider.getRecipe(context, recipeId);

        Intent intent;
        if (recipeId == -1) {
            intent = new Intent(context, RecipeStepActivity.class);
        } else {
            Log.d(RecipeWidgetProvider.class.getSimpleName(), "recipeId=" + recipeId);
            intent = new Intent(context, RecipeStepActivity.class);
            intent.putExtra(RecipeStepActivity.RECIPE_ID, recipeId);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_single);

        String ingredientsText = "";
        for (int i=0; i<mRecipe.getIngredients().size(); i++){
            ingredientsText = ingredientsText + "- " + mRecipe.getIngredients().get(i).getIngredient() + "\n";
        }

        views.setTextViewText(R.id.recipe_widget_name, mRecipe.getName());
        views.setTextViewText(R.id.recipe_widget_desc, ingredientsText);


        views.setOnClickPendingIntent(R.id.recipe_widget_layout, pendingIntent);

        return views;
    }

    private static RemoteViews getRecipeGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_grid);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        Intent appIntent = new Intent(context, RecipeListActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

}
