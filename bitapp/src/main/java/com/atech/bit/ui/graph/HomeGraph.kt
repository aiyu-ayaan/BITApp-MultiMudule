package com.atech.bit.ui.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.atech.bit.ui.screen.home.compose.HomeScreen
import com.atech.utils.animatedCompose
import com.atech.utils.getSimpleName


sealed class HomeScreenRoutes(val route: String) {
    data object HomeScreen : HomeScreenRoutes("home_screen")
}

fun NavGraphBuilder.homeNavigation(
    navController: NavController
) {
    navigation(
        startDestination = HomeScreenRoutes.HomeScreen.route,
        route = getSimpleName(HomeScreenRoutes::class.java)
    ) {
        animatedCompose(
            route = HomeScreenRoutes.HomeScreen.route
        ) {
            HomeScreen()
        }
    }

}





