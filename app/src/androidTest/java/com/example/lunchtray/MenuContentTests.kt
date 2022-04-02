/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.lunchtray.constants.ItemType
import com.example.lunchtray.model.MenuItem
import com.example.lunchtray.ui.order.AccompanimentMenuFragment
import com.example.lunchtray.ui.order.EntreeMenuFragment
import com.example.lunchtray.ui.order.SideMenuFragment
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MenuContentTests : BaseTest() {

    /**
     * Test the menu content of the entire [EntreeMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file.
     */
    @Test
    fun `entree_menu_item_content`() {
        // launch the entree menu fragment
        launchFragmentInContainer<EntreeMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val entreeMenuItems = fetchMenusByType(ItemType.ENTREE)

        // Check the cauliflower item
        val cauliflower = entreeMenuItems["cauliflower"]!!
        checkDisplayMenu(
            cauliflower,
            R.id.cauliflower,
            R.id.cauliflower_description,
            R.id.cauliflower_price
        )

        // Check the chili item
        val chili = entreeMenuItems["chili"]!!
        checkDisplayMenu(chili, R.id.chili, R.id.chili_description, R.id.chili_price)

        // Check the pasta item
        val pasta = entreeMenuItems["pasta"]!!
        checkDisplayMenu(pasta, R.id.pasta, R.id.pasta_description, R.id.pasta_price)

        // Check the skillet item
        val skillet = entreeMenuItems["skillet"]!!
        checkDisplayMenu(skillet, R.id.skillet, R.id.skillet_description, R.id.skillet_price)
    }

    /**
     * Test the menu content of the entire [SideMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file by reducing the number of
     * functions that would otherwise be necessary to test each item separately.
     */
    @Test
    fun `side_menu_item_content`() {
        // launch the side menu fragment
        launchFragmentInContainer<SideMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val sideMenuItems = fetchMenusByType(ItemType.SIDE_DISH)

        // Check the salad item
        val salad = sideMenuItems["salad"]!!
        checkDisplayMenu(salad, R.id.salad, R.id.salad_description, R.id.salad_price)

        // Check the soup item
        val soup = sideMenuItems["soup"]!!
        checkDisplayMenu(soup, R.id.soup, R.id.soup_description, R.id.soup_price)

        // Check the potato item
        val potatoes = sideMenuItems["potatoes"]!!
        checkDisplayMenu(potatoes, R.id.potatoes, R.id.potato_description, R.id.potato_price)

        // Check the rice item
        val rice = sideMenuItems["rice"]!!
        checkDisplayMenu(rice, R.id.rice, R.id.rice_description, R.id.rice_price)
    }

    /**
     * Test the menu content of the entire [AccompanimentMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file by reducing the number of
     * functions that would otherwise be necessary to test each item separately.
     */
    @Test
    fun `accompaniment_menu_item_content`() {
        // launch the accompaniment menu fragment
        launchFragmentInContainer<AccompanimentMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val accompanimentMenuItems = fetchMenusByType(ItemType.ACCOMPANIMENT)

        // Check the bread item
        val bread = accompanimentMenuItems["bread"]!!
        checkDisplayMenu(bread, R.id.bread, R.id.bread_description, R.id.bread_price)

        // Check the berries item
        val berries = accompanimentMenuItems["berries"]!!
        checkDisplayMenu(berries, R.id.berries, R.id.berries_description, R.id.berries_price)

        // Check the pickle item
        val pickles = accompanimentMenuItems["pickles"]!!
        checkDisplayMenu(pickles, R.id.pickles, R.id.pickles_description, R.id.pickles_price)
    }

    private fun checkDisplayMenu(menu: MenuItem, titleId: Int, descriptionId: Int, priceId: Int) {
        onView(withId(titleId)).check(matches(withText(containsString(menu.name))))
        onView(withId(descriptionId)).check(matches(withText(containsString(menu.description))))
        onView(withId(priceId)).check(matches(withText(containsString(menu.getFormattedPrice()))))
    }
}
