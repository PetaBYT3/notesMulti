package org.notes.multi.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.HomeAction
import org.notes.multi.getImage
import org.notes.multi.route.Route
import org.notes.multi.state.HomeState
import org.notes.multi.utilities.ComposableUnitBottomSheet
import org.notes.multi.utilities.CustomDropDownMenu
import org.notes.multi.utilities.MenuList
import org.notes.multi.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigator: Navigator = LocalNavigator.currentOrThrow
) {
    val state by viewModel.state.collectAsState()
    val onAction = viewModel::onAction

    ScaffoldScreen(
        navigator = navigator,
        state = state,
        onAction = onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    navigator: Navigator,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
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
                    navigator.push(Route.NoteRoute(note = null))
                },
                text = { Text(text = "Create New Note") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }

    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            state = state,
            onAction = onAction
        )
    }

    if (state.showDeleteBottomSheet) {
        ComposableUnitBottomSheet(
            title = "Delete Note",
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(150.dp)
                    ) {
                        Text(
                            text = state.noteToDelete?.title?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        HorizontalDivider(Modifier.padding(vertical = 10.dp))
                        Text(
                            text = state.noteToDelete?.text?: "",
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }
            },
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            onConfirm = {
                onAction(HomeAction.DeleteNote)
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = "Note Deleted !",
                        withDismissAction = true
                    )
                }
            },
            onDismiss = {
                onAction(HomeAction.ShowDeleteBottomSheet(
                    showDeleteBottomSheet = false,
                    noteToDelete = null
                ))
            }
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
                            .padding(5.dp),
                        onClick = {
                            navigator.push(Route.NoteRoute(note = note))
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    if (note.image.isNotBlank()) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                                .data(getImage(note.image))
                                                .crossfade(true)
                                                .build(),
                                            contentScale = ContentScale.Crop,
                                            alignment = Alignment.Center,
                                            contentDescription = "Image",
                                        )
                                    } else {
                                        Icon(
                                            modifier = Modifier
                                                .align(Alignment.Center),
                                            imageVector = Icons.Rounded.Image,
                                            contentDescription = "Image",
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = note.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            HorizontalDivider(Modifier.padding(vertical = 10.dp))
                            Text(
                                modifier = Modifier
                                    .height(100.dp),
                                text = note.text,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            onClick = {
                                onAction(HomeAction.ShowDeleteBottomSheet(
                                    showDeleteBottomSheet = true,
                                    noteToDelete = note
                                ))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Delete Note"
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = state.allNotes.isEmpty(),
            content = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = "Empty Note",
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = "No notes found")
                }
            }
        )
    }
}