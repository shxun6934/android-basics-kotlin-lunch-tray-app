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

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.lunchtray.ui.order.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for all navigation flows
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationTests : BaseTest() {

    /**
     * Test navigation from [StartOrderFragment] to [EntreeMenuFragment]
     */
    @Test
    fun `navigate_to_entree_menu_from_start_order`() {
        // Init nav controller
        val navController = initNavController()
        // Launch StartOrderFragment
        val startOrderScenario =
            launchFragmentInContainer<StartOrderFragment>(themeResId = R.style.Theme_LunchTray)
        // Configure nav controller
        startOrderScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView())
        }
        // Click start order
        onView(withId(R.id.start_order_btn)).perform(click())
        // Check destination is correct
        assertEquals(navController.currentDestination?.id, R.id.entree_menu_fragment)
    }

    /**
     * Test navigation from [EntreeMenuFragment] to [StartOrderFragment]
     */
    @Test
    fun `navigate_to_start_order_from_entree_menu`() {
        // Init nav controller
        val navController = initNavController()
        // Launch EntreeMenuFragment
        val entreeMenuScenario =
            launchFragmentInContainer<EntreeMenuFragment>(themeResId = R.style.Theme_LunchTray)
        // Configure nav controller
        entreeMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.entree_menu_fragment)
        }
        // Click the cancel button
        onView(withId(R.id.cancel_button)).perform(click())
        // Check that the destination is correct
        assertEquals(navController.currentDestination?.id, R.id.start_order_fragment)
    }

    /**
     * Test navigation from [EntreeMenuFragment] to [SideMenuFragment]
     */
    @Test
    fun `navigate_to_side_menu_from_entree_menu`() {
        // Init nav controller
        val navController = initNavController()
        // Launch the EntreeMenuFragment
        val entreeMenuScenario =
            launchFragmentInContainer<EntreeMenuFragment>(themeResId = R.style.Theme_LunchTray)
        // Configure nav controller
        entreeMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.entree_menu_fragment)
        }
        // Click the next button
        onView(withId(R.id.next_button)).perform(click())
        // Check that the destination is correct
        assertEquals(navController.currentDestination?.id, R.id.side_menu_fragment)
    }

    /**
     * Test navigation from [SideMenuFragment] to [StartOrderFragment]
     */
    @Test
    fun `navigate_to_start_order_from_side_menu`() {
        val navController = initNavController()
        val sideMenuScenario =
            launchFragmentInContainer<SideMenuFragment>(themeResId = R.style.Theme_LunchTray)
        sideMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.side_menu_fragment)
        }
        onView(withId(R.id.cancel_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.start_order_fragment)
    }

    /**
     * Test navigation from [SideMenuFragment] to [AccompanimentMenuFragment]
     */
    @Test
    fun `navigate_to_accompaniment_menu_from_side_menu`() {
        val navController = initNavController()
        val sideMenuScenario =
            launchFragmentInContainer<SideMenuFragment>(themeResId = R.style.Theme_LunchTray)
        sideMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.side_menu_fragment)
        }
        onView(withId(R.id.next_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.accompaniment_menu_fragment)
    }

    /**
     * Test navigation from [AccompanimentMenuFragment] to [StartOrderFragment]
     */
    @Test
    fun `navigate_to_start_order_from_accompaniment_menu`() {
        val navController = initNavController()
        val accompanimentMenuScenario =
            launchFragmentInContainer<AccompanimentMenuFragment>(
                themeResId = R.style.Theme_LunchTray
            )
        accompanimentMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.accompaniment_menu_fragment)
        }
        onView(withId(R.id.cancel_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.start_order_fragment)
    }

    /**
     * Test navigation from [AccompanimentMenuFragment] to [CheckoutFragment]
     */
    @Test
    fun `navigate_to_checkout_from_accompaniment_menu`() {
        val navController = initNavController()
        val accompanimentMenuScenario =
            launchFragmentInContainer<AccompanimentMenuFragment>(
                themeResId = R.style.Theme_LunchTray
            )
        accompanimentMenuScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.accompaniment_menu_fragment)
        }
        onView(withId(R.id.next_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.checkout_fragment)
    }

    /**
     * Test navigation from [CheckoutFragment] to [StartOrderFragment]
     */
    @Test
    fun `navigate_to_start_order_from_checkout`() {
        val navController = initNavController()
        val checkoutScenario =
            launchFragmentInContainer<CheckoutFragment>(themeResId = R.style.Theme_LunchTray)
        checkoutScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.checkout_fragment)
        }
        onView(withId(R.id.cancel_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.start_order_fragment)
    }

    /**
     * Test Navigation from [CheckoutFragment] to [StartOrderFragment]
     */
    @Test
    fun `navigate_to_start_order_from_checkout_via_submit`() {
        val navController = initNavController()
        val checkoutScenario =
            launchFragmentInContainer<CheckoutFragment>(themeResId = R.style.Theme_LunchTray)
        checkoutScenario.onFragment { fragment ->
            setNavigation(navController, fragment.requireView(), R.id.checkout_fragment)
        }
        onView(withId(R.id.submit_button)).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.start_order_fragment)
    }

    private fun initNavController(): TestNavHostController =
        TestNavHostController(ApplicationProvider.getApplicationContext())

    private fun setNavigation(
        navController: TestNavHostController,
        view: View,
        destId: Int? = null
    ) {
        navController.setGraph(R.navigation.mobile_navigation)
        // Destination defaults to the home fragment, we have to explicitly set the current
        // destination
        if (destId != null) navController.setCurrentDestination(destId)
        Navigation.setViewNavController(view, navController)
    }
}
