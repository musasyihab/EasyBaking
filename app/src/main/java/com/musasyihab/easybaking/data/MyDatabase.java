package com.musasyihab.easybaking.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by musasyihab on 9/1/17.
 */

@Database(version = MyDatabase.VERSION)
public class MyDatabase {

    public static final int VERSION = 1;

    @Table(RecipeContract.class)
    public static final String RECIPE_TABLE = "recipe_table";

}