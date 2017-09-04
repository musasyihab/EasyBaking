package com.musasyihab.easybaking.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.adapter.RecipeAdapter;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.network.TaskGetAllRecipes;
import com.musasyihab.easybaking.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeAdapterClickListener,
        LoaderManager.LoaderCallbacks<List<RecipeModel>> {

    private RecyclerView mRecipeList;
    private RecipeAdapter mAdapter;
    private Loader<List<RecipeModel>> mLoaderList;
    private List<RecipeModel> recipeList;
    private LoaderManager mLoaderManager;

    private final int loaderListId = Constants.GET_ALL_RECIPES_LOADER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeList = (RecyclerView) findViewById(R.id.main_recipe_list);

        int column = 1;

        if(isTablet()){
            column = 3;
        }

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, column);

        mRecipeList.setLayoutManager(layoutManager);
        mAdapter = new RecipeAdapter(this, Collections.EMPTY_LIST, this);

        getRecipes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoaderManager != null){
            if(mLoaderManager.getLoader(loaderListId)!=null)
                mLoaderManager.destroyLoader(loaderListId);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRecipeClick(RecipeModel recipeClicked) {
        Context context = this;
        Class destinationClass = RecipeStepActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(RecipeStepActivity.RECIPE_ID, recipeClicked.getId());
        startActivity(intentToStartDetailActivity);
    }

    private void loadRecipesToView(){
        if(recipeList==null || recipeList.isEmpty()){
            return;
        }
        mAdapter = new RecipeAdapter(this, recipeList, this);
        mRecipeList.setAdapter(mAdapter);

        if(mLoaderManager != null){
            if(mLoaderManager.getLoader(loaderListId)!=null)
                mLoaderManager.destroyLoader(loaderListId);
        }
    }

    private void getRecipes(){
        recipeList = new ArrayList<>();
        if (mLoaderList == null) {
            mLoaderManager = getSupportLoaderManager();
            mLoaderList = mLoaderManager.getLoader(loaderListId);
            mLoaderManager.initLoader(loaderListId, null, this).forceLoad();
        } else {
            mLoaderManager.restartLoader(loaderListId, null, this).forceLoad();
        }
    }

    private boolean isTablet(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth >= 600) {
            return true;
        } else {
            return false;
        }
    }

    //---------<LOADER CALLBACKS>---------

    @Override
    public Loader<List<RecipeModel>> onCreateLoader(int id, Bundle args) {
        switch (id){
            case loaderListId:
                return new TaskGetAllRecipes(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<RecipeModel>> loader, List<RecipeModel> data) {
        recipeList = data;
        loadRecipesToView();
    }

    @Override
    public void onLoaderReset(Loader<List<RecipeModel>> loader) {

    }

    //--------</LOADER CALLBACKS>---------
}
