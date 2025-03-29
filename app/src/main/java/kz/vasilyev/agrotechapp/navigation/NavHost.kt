package kz.vasilyev.agrotechapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kz.vasilyev.agrotechapp.feature.add_garden.AddGardenScreen
import kz.vasilyev.agrotechapp.feature.home.HomeScreen
import kz.vasilyev.agrotechapp.feature.tips.TipsScreen

@Composable
fun NavHost(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.BottomBar.Home.route,
    ) {
        composable(route = Screen.BottomBar.Home.route) {
            HomeScreen(innerPadding, navController)
        }

        composable(route = Screen.BottomBar.Library.route) {
            TipsScreen(innerPadding)
        }

        composable(route = Screen.BottomBar.Chat.route) {

        }

        composable(route = Screen.BottomBar.Tips.route) {

        }

        composable(route = Screen.AddGarden.route) {
            AddGardenScreen(innerPadding, navController)
        }
    }
}