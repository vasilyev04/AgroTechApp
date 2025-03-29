package kz.vasilyev.agrotechapp.navigation

sealed class Screen(val route: String) {
    sealed class BottomBar(route: String): Screen(route) {
        data object Home : BottomBar("/home")
        data object Library : BottomBar("/library")
        data object Chat : BottomBar("/chat")
        data object Tips : BottomBar("/tips")

        companion object {
            private val bottomBarRoutes = listOf(
                Home.route,
                Library.route,
                Chat.route,
                Tips.route
            )

            fun isBottomBarScreen(route: String?): Boolean = route in bottomBarRoutes
        }
    }

    data object AddGarden : Screen("/addgarden")
    data object JournalGarden : Screen("/journalgarden/{gardenId}")
    data object NoteDetails : Screen("/notedetails/{gardenId}")
    data object AddNote : Screen("/addnote")
}
