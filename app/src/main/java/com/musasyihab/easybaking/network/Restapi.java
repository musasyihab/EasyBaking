package com.musasyihab.easybaking.network;

import com.musasyihab.easybaking.model.RecipeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by musasyihab on 9/1/17.
 */

public interface Restapi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipeModel>> getAllRecipes();

}
