package org.notes.multi.route

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    data object HomeRoute: Route()

    @Serializable
    data class NoteRoute(val uId : Long? = null) : Route()
}