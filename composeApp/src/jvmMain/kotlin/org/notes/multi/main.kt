package org.notes.multi

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.notes.multi.screen.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NotesMulti",
    ) {
        App()
    }
}