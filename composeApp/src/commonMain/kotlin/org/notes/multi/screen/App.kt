package org.notes.multi.screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.koin.compose.KoinApplication
import org.notes.multi.createBaseDirectory
import org.notes.multi.module.AppModule
import org.notes.multi.route.Route
import org.notes.multi.theme.AppTheme
import org.notes.multi.utilities.slideComposable

@Composable
fun App() {
    createBaseDirectory()
    AppTheme {
        KoinApplication(
            application = {
                modules(AppModule.getAll())
            }
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Route.HomeRoute
            ) {
                composable<Route.HomeRoute> {
                    HomeScreen(
                        navController = navController
                    )
                }
                slideComposable<Route.NoteRoute> {
                    val route: Route.NoteRoute = it.toRoute()
                    NoteScreen(
                        navController = navController,
                        uId = route.uId
                    )
                }
            }
        }
    }
}