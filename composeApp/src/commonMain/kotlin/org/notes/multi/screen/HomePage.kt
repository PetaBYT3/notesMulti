package org.notes.multi.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.HomeAction
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.route.Route
import org.notes.multi.state.HomeState
import org.notes.multi.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: Navigator = LocalNavigator.currentOrThrow
) {
    val state by viewModel.state.collectAsState()

    ScaffoldScreen(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    navigator: Navigator,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigator.push(Route.NoteRoute(
                        noteUid = null
                    ))
                },
                text = { Text(text = "Create New Note") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            )
        }

    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            state = state,
            onAction = onAction
        )
    }

}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth(),
            columns = GridCells.Adaptive(150.dp),
            content = {
                items(state.allNotes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Text(text = note.title)
                            Text(text = note.text)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        )
    }
}