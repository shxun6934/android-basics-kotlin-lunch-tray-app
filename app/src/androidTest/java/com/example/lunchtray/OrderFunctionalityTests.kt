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
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.lunchtray.constants.ItemType
import com.example.lunchtray.model.MenuItem
import com.example.lunchtray.ui.order.AccompanimentMenuFragment
import com.example.lunchtray.ui.order.CheckoutFragment
import com.example.lunchtray.ui.order.EntreeMenuFragment
import com.example.lunchtray.ui.order.SideMenuFragment
import org.hamcrest.core.StringContains.containsString
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat

@RunWith(AndroidJUnit4::class)
@LargeTest
class OrderFunctionalityTests : BaseTest() {

    /**
     * Test subtotal in [EntreeMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file.
     */
    @Test
    fun `radio_buttons_update_entree_menu_subtotal`() {
        // Launch the entree menu fragment
        launchFragmentInContainer<EntreeMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val entreeMenuItems = fetchMenusByType(ItemType.ENTREE)

        // Select the cauliflower item
        val cauliflower = entreeMenuItems["cauliflower"]!!
        checkUpdateSubtotal(cauliflower, R.id.cauliflower)

        // Select the chili item
        val chili = entreeMenuItems["chili"]!!
        checkUpdateSubtotal(chili, R.id.chili)

        // Select the pasta item
        val pasta = entreeMenuItems["pasta"]!!
        checkUpdateSubtotal(pasta, R.id.pasta)

        // Select the skillet item
        val skillet = entreeMenuItems["skillet"]!!
        checkUpdateSubtotal(skillet, R.id.skillet)
    }

    /**
     * Test subtotal in [SideMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file.
     */
    @Test
    fun `radio_buttons_update_side_menu_subtotal`() {
        // Launch the side menu fragment
        launchFragmentInContainer<SideMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val sideMenuItems = fetchMenusByType(ItemType.SIDE_DISH)

        // Select the salad item
        val salad = sideMenuItems["salad"]!!
        checkUpdateSubtotal(salad, R.id.salad)

        // Select the soup item
        val soup = sideMenuItems["soup"]!!
        checkUpdateSubtotal(soup, R.id.soup)

        // Select the potato item
        val potatoes = sideMenuItems["potatoes"]!!
        checkUpdateSubtotal(potatoes, R.id.potatoes)

        // Select the rice item
        val rice = sideMenuItems["rice"]!!
        checkUpdateSubtotal(rice, R.id.rice)
    }

    /**
     * Test subtotal in [AccompanimentMenuFragment]
     *
     * It isn't necessarily best practice to make all these assertions in a single test,
     * however, it is done here for improved readability of the file.
     */
    @Test
    fun `radio_buttons_update_accompaniment_menu_subtotal`() {
        // Launch the side menu fragment
        launchFragmentInContainer<AccompanimentMenuFragment>(themeResId = R.style.Theme_LunchTray)

        val accompanimentMenuItems = fetchMenusByType(ItemType.ACCOMPANIMENT)

        // Select the salad item
        val bread = accompanimentMenuItems["bread"]!!
        checkUpdateSubtotal(bread, R.id.bread)

        // Select the soup item
        val berries = accompanimentMenuItems["berries"]!!
        checkUpdateSubtotal(berries, R.id.berries)

        // Select the potato item
        val pickles = accompanimentMenuItems["pickles"]!!
        checkUpdateSubtotal(pickles, R.id.pickles)
    }

    /**
     * Test subtotals in full order flow
     */
    @Test
    fun `subtotal_updates_in_full_order_flow`() {
        val cauliflowerPrice = fetchMenusByType(ItemType.ENTREE)["cauliflower"]!!.price
        val saladPrice = fetchMenusByType(ItemType.SIDE_DISH)["salad"]!!.price
        val breadPrice = fetchMenusByType(ItemType.ACCOMPANIMENT)["bread"]!!.price

        var subtotal = 0.0

        // Launch the main activity
        launchActivity<MainActivity>()
        // Start order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Select entree item
        onView(withId(R.id.cauliflower)).perform(click())
        // We already have a test for a single menu item selection, so we don't need to check the
        // subtotal here.
        // Move to next fragment
        onView(withId(R.id.next_button)).perform(click())
        // Select side item
        onView(withId(R.id.salad)).perform(click())
        // Check that subtotal has updated
        subtotal += cauliflowerPrice + saladPrice
        onView(withId(R.id.subtotal))
            .check(matches(withText(containsString("Subtotal: ${formattedPrice(subtotal)}"))))
        // Move to next fragment
        onView(withId(R.id.next_button)).perform(click())
        // Select accompaniment item
        onView(withId(R.id.bread)).perform(click())
        // Check that subtotal has updated
        subtotal += breadPrice
        onView(withId(R.id.subtotal))
            .check(matches(withText(containsString("Subtotal: ${formattedPrice(subtotal)}"))))
        // Move to next fragment
        onView(withId(R.id.next_button)).perform(click())
        // Check that subtotal in checkout is correct
        onView(withId(R.id.subtotal))
            .check(matches(withText(containsString("Subtotal: ${formattedPrice(subtotal)}"))))
    }

    /**
     * Test subtotal, tax, and total in [CheckoutFragment]
     */
    @Test
    fun `subtotal_tax_total_in_checkout`() {
        val cauliflowerPrice = fetchMenusByType(ItemType.ENTREE)["cauliflower"]!!.price
        val saladPrice = fetchMenusByType(ItemType.SIDE_DISH)["salad"]!!.price
        val breadPrice = fetchMenusByType(ItemType.ACCOMPANIMENT)["bread"]!!.price
        val subtotal = cauliflowerPrice + saladPrice + breadPrice

        // Select items and move to checkout
        fullOrderFlow()
        // Check subtotal. Note that this is already done in a separate test, but the other values
        // depend on it so this assertion is a sanity check
        onView(withId(R.id.subtotal))
            .check(matches(withText(containsString("Subtotal: ${formattedPrice(subtotal)}"))))
        // Check tax
        val tax = subtotal * 0.08
        onView(withId(R.id.tax))
            .check(matches(withText(containsString("Tax: ${formattedPrice(tax)}"))))
        // Check total
        val total = subtotal + tax
        onView(withId(R.id.total))
            .check(matches(withText(containsString("Total: ${formattedPrice(total)}"))))
    }

    /**
     * Test that the order is reset after canceling in [EntreeMenuFragment]
     */
    @Test
    fun `order_reset_after_cancel_from_entree_menu`() {
        // Launch the app
        launchActivity<MainActivity>()
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Select an item
        onView(withId(R.id.cauliflower)).perform(click())
        // Cancel order
        onView(withId(R.id.cancel_button)).perform(click())
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Make sure subtotal is zero
        onView(withText("Subtotal: ￥0")).check(matches(isDisplayed()))
    }

    /**
     * Test that the order is reset after canceling in [SideMenuFragment]
     */
    @Test
    fun `order_reset_after_cancel_from_side_menu`() {
        // Launch the app
        launchActivity<MainActivity>()
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Select an item
        onView(withId(R.id.cauliflower)).perform(click())
        // Move to side menu
        onView(withId(R.id.next_button)).perform(click())
        // Select an item
        onView(withId(R.id.soup)).perform(click())
        // Cancel the order
        onView(withId(R.id.cancel_button)).perform(click())
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Make sure subtotal is zero
        onView(withText("Subtotal: ￥0")).check(matches(isDisplayed()))
    }

    /**
     * Test that the order is reset after canceling in [AccompanimentMenuFragment]
     */
    @Test
    fun `order_reset_after_cancel_from_accompaniment_menu`() {
        // Launch the app
        launchActivity<MainActivity>()
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Select an item
        onView(withId(R.id.cauliflower)).perform(click())
        // Move to side menu
        onView(withId(R.id.next_button)).perform(click())
        // Select an item
        onView(withId(R.id.soup)).perform(click())
        // Move to accompaniment menu
        onView(withId(R.id.next_button)).perform(click())
        // Select item
        onView(withId(R.id.bread)).perform(click())
        // Cancel the order
        onView(withId(R.id.cancel_button)).perform(click())
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Make sure subtotal is zero
        onView(withText("Subtotal: ￥0")).check(matches(isDisplayed()))
    }

    /**
     * Test that the order is reset after canceling in [CheckoutFragment]
     */
    @Test
    fun `order_reset_after_cancel_from_checkout`() {
        // Select items and move to checkout
        fullOrderFlow()
        // Cancel the order
        onView(withId(R.id.cancel_button)).perform(click())
        // Start the order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Make sure subtotal is zero
        onView(withText("Subtotal: ￥0")).check(matches(isDisplayed()))
    }

    /**
     * Test that the correct snackbar is displayed when order is submitted
     */
    @Test
    fun `order_snackbar`() {
        // Select items and move to checkout
        fullOrderFlow()
        // Click submit
        onView(withId(R.id.submit_button)).perform(click())
        // Check for snackbar text
        onView(withText(containsString("Order Submitted!")))
            .check(matches(isDisplayed()))
    }

    private fun checkUpdateSubtotal(menu: MenuItem, radioId: Int) {
        onView(withId(radioId)).perform(click())
        onView(withId(R.id.subtotal)).check(matches(withText(containsString("Subtotal: ${menu.getFormattedPrice()}"))))
    }

    private fun formattedPrice(price: Double): String =
        NumberFormat.getCurrencyInstance().format(price)
}
