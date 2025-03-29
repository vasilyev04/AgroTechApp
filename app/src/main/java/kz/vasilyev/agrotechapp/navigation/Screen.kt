package kz.vasilyev.agrotechapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Analytics : Screen("analytics")
    object Chat : Screen("chat")
    object AddGarden : Screen("add_garden")
    object JournalGarden : Screen("journal_garden/{gardenId}") {
        fun createRoute(gardenId: Long) = "journal_garden/$gardenId"
    }
    object AddNote : Screen("add_note/{gardenId}") {
        fun createRoute(gardenId: Long) = "add_note/$gardenId"
    }
    object NoteDetails : Screen("note_details/{noteId}") {
        fun createRoute(noteId: Long) = "note_details/$noteId"
    }

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
}
