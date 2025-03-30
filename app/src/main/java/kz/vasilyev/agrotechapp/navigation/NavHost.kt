package kz.vasilyev.agrotechapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kz.vasilyev.agrotechapp.feature.add_garden.AddGardenScreen
import kz.vasilyev.agrotechapp.feature.add_note.AddNoteScreen
import kz.vasilyev.agrotechapp.feature.ai_chat.ChatScreen
import kz.vasilyev.agrotechapp.feature.analytics.AnalyticsScreen
import kz.vasilyev.agrotechapp.feature.home.HomeScreen
import kz.vasilyev.agrotechapp.feature.journal_garden.FullScreenImageScreen
import kz.vasilyev.agrotechapp.feature.journal_garden.JournalGarden
import kz.vasilyev.agrotechapp.feature.library.LibraryScreen
import kz.vasilyev.agrotechapp.feature.note_details.NoteDetailsScreen
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
            LibraryScreen(innerPadding)
        }

        composable(route = Screen.BottomBar.Chat.route) {
            ChatScreen(innerPadding)
        }

        composable(route = Screen.BottomBar.Tips.route) {
            TipsScreen(innerPadding)
        }

        composable(route = Screen.AddGarden.route) {
            AddGardenScreen(innerPadding, navController)
        }

        composable(route = Screen.JournalGarden.route) { backStackEntry ->
            val gardenId = backStackEntry.arguments?.getString("gardenId")
            gardenId?.toLongOrNull()?.let {
                JournalGarden(innerPadding, navController, it)
            }
        }

        composable(route = Screen.Analytics.route) { backStackEntry ->
            val gardenId = backStackEntry.arguments?.getString("gardenId")
            gardenId?.toLongOrNull()?.let { it
                AnalyticsScreen(innerPadding, navController, it)
            }
        }

        composable(route = Screen.AddNote.route) { backStackEntry ->
            val gardenId = backStackEntry.arguments?.getString("gardenId")
            gardenId?.toLongOrNull()?.let {
                AddNoteScreen(innerPadding, navController, it)
            }
        }

        composable(route = Screen.DetailsNote.route) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")
            noteId?.toLongOrNull()?.let {
                NoteDetailsScreen(innerPadding, navController, it)
            }
        }

        composable(
            route = Screen.FullScreenImage.route
        ) { backStackEntry ->
            val imageBase64 = backStackEntry.arguments?.getString("imageBase64")
                ?: return@composable
            Log.d("ASDASDA", imageBase64.toString())
            FullScreenImageScreen(
                navController = navController,
                imageBase64 = imageBase64
            )
        }
    }
}