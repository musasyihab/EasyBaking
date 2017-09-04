package com.musasyihab.easybaking.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.ui.RecipeStepActivity;
import com.musasyihab.easybaking.data.MyProvider;
import com.musasyihab.easybaking.model.RecipeModel;

import java.util.List;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    RecipeModel mRecipe;
    List<RecipeModel> mRecipeList;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;

    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        mRecipeList = MyProvider.getAllRecipesWidget(mContext);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (mRecipeList == null) return 0;
        return mRecipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipeList == null || mRecipeList.size() == 0) return null;
        mRecipe = mRecipeList.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);

        String ingredientsText = "";
        for (int i=0; i<mRecipe.getIngredients().size(); i++){
            ingredientsText = ingredientsText + "- " + mRecipe.getIngredients().get(i).getIngredient() + "\n";
        }

        views.setTextViewText(R.id.recipe_widget_name, mRecipe.getName());
        views.setTextViewText(R.id.recipe_widget_desc, ingredientsText);

        Bundle extras = new Bundle();
        extras.putInt(RecipeStepActivity.RECIPE_ID, mRecipe.getId());
        Intent recipeStepIntent = new Intent();
        recipeStepIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.recipe_widget_layout, recipeStepIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

