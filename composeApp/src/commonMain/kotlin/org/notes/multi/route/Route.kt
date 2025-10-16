package org.notes.multi.route

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.screen.HomeScreen
import org.notes.multi.screen.NoteScreen

@Serializable
sealed class Route {

    @Serializable
    data object HomeRoute: Route()

    @Serializable
    data class NoteRoute(val uId : Int? = null) : Route()
}