package com.musasyihab.easybaking.model;

import java.util.List;

/**
 * Created by musasyihab on 9/1/17.
 */

public class RecipeModel {
    private int id;
    private String name;
    private List<IngredientModel> ingredients;
    private List<StepModel> steps;
    private int servings;
    private String image;
    private boolean widgetAdded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<StepModel> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isWidgetAdded() {
        return widgetAdded;
    }

    public void setWidgetAdded(boolean widgetAdded) {
        this.widgetAdded = widgetAdded;
    }
}
