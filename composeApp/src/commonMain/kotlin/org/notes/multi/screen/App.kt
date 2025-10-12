package org.notes.multi.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.koin.compose.KoinApplication
import org.notes.multi.module.AppModule
import org.notes.multi.route.Route

@Composable
fun App() {
    MaterialTheme {
        KoinApplication(
            application = {
                modules(AppModule.getAll())
            }
        ) {
            Navigator(screen = Route.HomeRoute) {
                SlideTransition(navigator = it)
            }
        }
    }
}