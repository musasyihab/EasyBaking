package com.musasyihab.easybaking.data;

import android.content.SharedPreferences;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by musasyihab on 9/1/17.
 */

public class RecipeContract {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_NAME = "name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_INGREDIENTS = "ingredients";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_STEPS = "steps";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String COLUMN_IMAGE = "image";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    public static final String COLUMN_WIDGET_ADDED = "widgetAdded";

    public static final String[] RECIPES_PROJECTION = {
            RecipeContract.COLUMN_ID,
            RecipeContract.COLUMN_NAME,
            RecipeContract.COLUMN_IMAGE,
            RecipeContract.COLUMN_SERVINGS,
            RecipeContract.COLUMN_INGREDIENTS,
            RecipeContract.COLUMN_STEPS,
            RecipeContract.COLUMN_WIDGET_ADDED
    };

}
