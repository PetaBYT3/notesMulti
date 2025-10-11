package org.notes.multi.route

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.serialization.Serializable
import org.notes.multi.screen.HomeScreen
import org.notes.multi.screen.NoteScreen

@Serializable
sealed interface Route: Screen {

    @Serializable
    data object HomeRoute: Route {
        @Composable
        override fun Content() {
            HomeScreen()
        }
    }

    @Serializable
    data class NoteRoute(
        val noteUid: Int? = null
    ) : Route {
        @Composable
        override fun Content() {
            NoteScreen(noteUid = noteUid)
        }
    }

}