package com.musasyihab.easybaking.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.adapter.StepAdapter;
import com.musasyihab.easybaking.data.MyProvider;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;
import com.musasyihab.easybaking.network.TaskGetRecipeDetail;
import com.musasyihab.easybaking.util.Constants;
import com.musasyihab.easybaking.util.RecipeWidgetService;

/**
 * Created by musasyihab on 9/3/17.
 */

public class RecipeStepActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, StepAdapter.StepAdapterClickListener  {
    public static final String RECIPE_ID = "RECIPE_ID";

    private int recipeId = -1;
    private RecipeModel recipe;
    private LoaderManager mLoaderManager;
    private Loader<RecipeModel> mLoaderDetail;

    private NestedScrollView mLayout;
    private RecyclerView mStepList;
    private TextView mStepIngredients;
    private FrameLayout mDetailFragment;
    private ActionBar mActionBar;
    private MenuItem addWidgetMenu;

    private StepAdapter mAdapter;
    private boolean isTablet;
    private boolean isWidgetAdded;
    private FragmentManager fragmentManager;

    private final int loaderGetDetailId = Constants.GET_RECIPE_DETAIL_LOADER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        mLayout = (NestedScrollView) findViewById(R.id.recipe_step_layout);
        mStepList = (RecyclerView) findViewById(R.id.recipe_step_list);
        mStepList = (RecyclerView) findViewById(R.id.recipe_step_list);
        mStepIngredients = (TextView) findViewById(R.id.recipe_step_ingredients);

        if(findViewById(R.id.recipe_step_detail_frame) != null){
            mDetailFragment = (FrameLayout) findViewById(R.id.recipe_step_detail_frame);
            isTablet = true;
        } else {
            isTablet = false;
        }

        fragmentManager = getSupportFragmentManager();

        mActionBar = getSupportActionBar();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(RECIPE_ID)) {
                recipeId = intentThatStartedThisActivity.getIntExtra(RECIPE_ID, -1);
            }
        }
        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey(RECIPE_ID)) {
                recipeId = savedInstanceState.getInt(RECIPE_ID, -1);
            }
        }

        if(recipeId!=-1) {
            getRecipeDetail();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, recipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoaderManager != null){
            if(mLoaderManager.getLoader(loaderGetDetailId)!=null)
                mLoaderManager.destroyLoader(loaderGetDetailId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_step_activity_menu, menu);
        addWidgetMenu = menu.findItem(R.id.action_add_widget);
        updateAddWidgetMenuText();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_widget:
                updateAddWidgetStatus();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAddWidgetStatus(){
        AsyncTask<Void, Void, Void> insertRecipesTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                isWidgetAdded = !isWidgetAdded;
                recipe.setWidgetAdded(isWidgetAdded);
                MyProvider.insertRecipe(RecipeStepActivity.this, recipe);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateAddWidgetMenuText();
            }
        };

        insertRecipesTask.execute();
    }

    private void getRecipeDetail(){
        mLoaderManager = getSupportLoaderManager();
        if (mLoaderDetail == null) {
            mLoaderDetail = mLoaderManager.getLoader(loaderGetDetailId);
            mLoaderManager.initLoader(loaderGetDetailId, null, this).forceLoad();
        } else {
            mLoaderManager.restartLoader(loaderGetDetailId, null, this).forceLoad();
        }
    }

    private void loadRecipeToView(){
        String ingredientsText = "";
        for (int i=0; i<recipe.getIngredients().size(); i++){
            ingredientsText = ingredientsText + "- " + recipe.getIngredients().get(i).getIngredient() + "\n";
        }
        mStepIngredients.setText(ingredientsText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mStepList.setLayoutManager(layoutManager);

        mAdapter = new StepAdapter(this, recipe.getSteps(), this);
        mStepList.setAdapter(mAdapter);
        mStepList.setNestedScrollingEnabled(false);

        mLayout.scrollTo(0, 0);

        if(mActionBar!=null){
            mActionBar.setTitle(recipe.getName());
        }

        isWidgetAdded = recipe.isWidgetAdded();
        updateAddWidgetMenuText();
    }

    private void updateAddWidgetMenuText(){
        if(addWidgetMenu!=null){
            if(isWidgetAdded) {
                addWidgetMenu.setTitle(getString(R.string.remove_widget));
            }
            else {
                addWidgetMenu.setTitle(getString(R.string.add_widget));
            }
        }
        RecipeWidgetService.startActionUpdateRecipeWidgets(this);
    }

    @Override
    public void onStepClick(StepModel stepClicked, int clickedPosition) {
        if(isTablet){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setRecipe(recipe);
            stepDetailFragment.setCurrentPos(clickedPosition);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_frame, stepDetailFragment)
                    .commit();
        } else {
            String sRecipe = new Gson().toJson(recipe);
            Class destinationClass = StepDetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(this, destinationClass);
            intentToStartDetailActivity.putExtra(StepDetailActivity.RECIPE, sRecipe);
            intentToStartDetailActivity.putExtra(StepDetailActivity.CURRENT_POS, clickedPosition);
            startActivity(intentToStartDetailActivity);
        }
    }

    //---------<LOADER CALLBACKS>---------

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id){
            case loaderGetDetailId:
                return new TaskGetRecipeDetail(this, recipeId);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()){
            case loaderGetDetailId:
                recipe = (RecipeModel) data;
                if(recipe!=null)
                    loadRecipeToView();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    //--------</LOADER CALLBACKS>---------

}
