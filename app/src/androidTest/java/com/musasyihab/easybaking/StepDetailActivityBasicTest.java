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


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.musasyihab.easybaking.ui.RecipeStepActivity;
import com.musasyihab.easybaking.ui.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class StepDetailActivityBasicTest {

    private String RECIPE_DESC_0 = "Recipe Introduction";
    private String RECIPE_DESC_1 = "1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.";

    @Rule
    public ActivityTestRule<RecipeStepActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeStepActivity.class);

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