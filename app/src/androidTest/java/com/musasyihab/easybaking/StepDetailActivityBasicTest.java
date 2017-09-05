/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.musasyihab.easybaking;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;
import com.musasyihab.easybaking.ui.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class StepDetailActivityBasicTest {

    private String RECIPE_DESC_0 = "Recipe Introduction";
    private String RECIPE_DESC_1 = "1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.";

    private RecipeModel generateDummyRecipe(){
        RecipeModel recipe = new RecipeModel();
        List<StepModel> steps = new ArrayList<>();
        StepModel step0 = new StepModel();
        step0.setDescription(RECIPE_DESC_0);
        steps.add(step0);
        StepModel step1 = new StepModel();
        step1.setDescription(RECIPE_DESC_1);
        steps.add(step1);
        recipe.setSteps(steps);
        return recipe;
    }

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityTestRule =
            new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, StepDetailActivity.class);
                    RecipeModel dummyRecipe = generateDummyRecipe();
                    result.putExtra(StepDetailActivity.RECIPE, new Gson().toJson(dummyRecipe));
                    result.putExtra(StepDetailActivity.CURRENT_POS, 0);
                    return result;
                }
            };

    @Test
    public void containPrevAndNextButton() {
        onView(withId(R.id.step_detail_prev)).check(matches(isDisplayed()));
        onView(withId(R.id.step_detail_next)).check(matches(isDisplayed()));
    }

    @Test
    public void containCorrectDescription() {
        onView(withId(R.id.step_detail_desc)).check(matches(withText(RECIPE_DESC_0)));
    }

    @Test
    public void changeDescriptionWhenClickNext() {
        onView(withId(R.id.step_detail_next))
                .perform(click());
        onView(withId(R.id.step_detail_desc)).check(matches(withText(RECIPE_DESC_1)));

    }
}