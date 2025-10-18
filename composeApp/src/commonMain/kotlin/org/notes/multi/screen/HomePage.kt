package org.notes.multi.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.HomeAction
import org.notes.multi.getFile
import org.notes.multi.route.Route
import org.notes.multi.state.HomeState
import org.notes.multi.utilities.ComposableUnitBottomSheet
import org.notes.multi.utilities.CustomDropDownMenu
import org.notes.multi.utilities.DropDownList
import org.notes.multi.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val onAction = viewModel::onAction

    ScaffoldScreen(
        navController = navController,
        state = state,
        onAction = onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    navController: NavController,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Route.NoteRoute(uId = null)) },
                content = {
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
            navController = navController,
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
                    ) {
                        Box(
                            modifier = Modifier
                                .height(75.dp)
                                .clip(RoundedCornerShape(15.dp))
                        ) {
                            if (state.noteToDelete?.image?.imagePath != null) {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(getFile(state.noteToDelete.image.imagePath))
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
                        Text(
                            text = state.noteToDelete?.noteEntity?.title ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        HorizontalDivider(Modifier.padding(vertical = 10.dp))
                        Text(
                            text = state.noteToDelete?.noteEntity?.text?: "",
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 5
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
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
                            .clip(RoundedCornerShape(20.dp)),
                        onClick = {
                            navController.navigate(Route.NoteRoute(note.noteEntity.uId))
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .height(75.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(15.dp))
                                ) {

                                    if (note.image?.imagePath != null) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                                .data(getFile(note.image.imagePath))
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
                                Box(
                                    modifier = Modifier
                                ) {
                                    var isExpanded by rememberSaveable { mutableStateOf(false) }
                                    IconButton(
                                        onClick = { isExpanded = !isExpanded }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.MoreVert,
                                            contentDescription = null
                                        )
                                    }
                                    val dropDownList = listOf(
                                        DropDownList(
                                            icon = Icons.Rounded.Delete,
                                            title = "Delete Note",
                                            onClick = {
                                                onAction(HomeAction.ShowDeleteBottomSheet(true, note))
                                            }
                                        )
                                    )
                                    CustomDropDownMenu(
                                        dropDownList = dropDownList,
                                        isExpanded = isExpanded,
                                        onDismiss = { isExpanded = false }
                                    )
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                modifier = Modifier
                                    .weight(1f),
                                text = note.noteEntity.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            HorizontalDivider(Modifier.padding(vertical = 10.dp))
                            Text(
                                text = note.noteEntity.text,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 5
                            )
                            Spacer(Modifier.weight(1f))
                            Spacer(Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50f))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(15.dp),
                                        imageVector = Icons.Rounded.AttachFile,
                                        contentDescription = "Attach File"
                                    )
                                    Text(
                                        text = note.documentsList.size.toString(),
                                        fontSize = 15.sp
                                    )
                                }
                            }
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