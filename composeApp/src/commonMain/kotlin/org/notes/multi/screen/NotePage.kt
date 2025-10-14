package org.notes.multi.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Upload
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.NoteAction
import org.notes.multi.getImage
import org.notes.multi.localdata.database.NotesEntity
import org.notes.multi.utilities.SimpleConfirmationBottomSheet
import org.notes.multi.state.NoteState
import org.notes.multi.utilities.CustomDropDownMenu
import org.notes.multi.utilities.MenuList
import org.notes.multi.viewmodel.NoteViewModel

@OptIn(InternalVoyagerApi::class)
@Composable
fun NoteScreen(
    note: NotesEntity?,
    viewModel: NoteViewModel = koinViewModel(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    BackHandler(enabled = true) {
        unSavedChangesHandler(
            state = state,
            onAction = onAction,
            navigator = navigator
        )
    }

    LaunchedEffect(note) {
        if (note != null) {
            onAction(NoteAction.SelectedNotes(note))
        } else {
            onAction(NoteAction.ClearNote)
        }
    }

    ScaffoldScreen(
        note = note,
        navigator = navigator,
        state = state,
        onAction = onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    note: NotesEntity?,
    navigator: Navigator,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            unSavedChangesHandler(
                                state = state,
                                onAction = onAction,
                                navigator = navigator
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text(text = "Note") },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (state.titleDraft.isNotBlank()) {
                        onAction(NoteAction.InsertNote)
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Note Saved !",
                                withDismissAction = true
                            )
                        }
                    } else {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = "Title cannot be empty !",
                                withDismissAction = true
                            )
                        }
                    }
                },
                text = { Text(text = "Save Note") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = "Save Note"
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            snackBarHostState = snackBarHostState,
            state = state,
            onAction = onAction,
        )
    }

    if (state.bottomSheetDiscard) {
        SimpleConfirmationBottomSheet(
            title = "Discard Changes",
            text = "Are you sure you want to discard changes ?",
            onConfirmText = "Discard",
            onDismissText = "Keep",
            onConfirm = {
                navigator.pop()
            },
            onDismiss = {
                onAction(NoteAction.BottomSheetDiscard(false))
            }
        )
    }

    if (state.bottomSheetDeleteImage) {
        SimpleConfirmationBottomSheet(
            title = "Delete Image",
            text = "Are you sure you want to delete image ?",
            onConfirmText = "Delete",
            onDismissText = "Cancel",
            onConfirm = {
                onAction(NoteAction.DeleteImage)
            },
            onDismiss = {
                onAction(NoteAction.BottomSheetDeleteImage(false))
            }
        )
    }
}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    snackBarHostState: SnackbarHostState,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val imagePicker = rememberFilePickerLauncher(
        type = PickerType.Image,
        onResult = { file ->
            scope.launch {
                if (file != null) {
                    val imageByte = file.readBytes().toList()
                    onAction(NoteAction.SaveImage(imageByte))
                }
            }
        }
    )

    Column(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
            .verticalScroll(scrollState, true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.imageName.isNotBlank()) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(getImage(state.imageName))
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
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    IconButton(
                        onClick = {
                            onAction(NoteAction.DropDownImage(true))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More",
                        )
                    }
                    val menuList = listOf(
                        MenuList(
                            title = "Upload Image",
                            icon = Icons.Rounded.Upload,
                            onClick = { imagePicker.launch() }
                        ),
                        MenuList(
                            title = "Delete Image",
                            icon = Icons.Rounded.Delete,
                            onClick = {
                                if (state.imageName.isNotBlank()) {
                                    onAction(NoteAction.BottomSheetDeleteImage(true))
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "No image to delete !",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            }
                        ),
                    )
                    CustomDropDownMenu(
                        menuList = menuList,
                        isExpanded = state.dropDownImage,
                        onDismiss = { onAction(NoteAction.DropDownImage(false)) }
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.titleDraft,
            onValueChange = { onAction(NoteAction.TitleDraft(it)) },
            placeholder = { Text(text = "Title") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            singleLine = false
        )
        HorizontalDivider(Modifier.padding(vertical = 10.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.textDraft,
            onValueChange = { onAction(NoteAction.TextDraft(it)) },
            placeholder = { Text(text = "Write something here...") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            singleLine = false
        )
    }
}

private fun unSavedChangesHandler(
    state: NoteState,
    onAction: (NoteAction) -> Unit,
    navigator: Navigator,
) {
    if (state.title != state.titleDraft || state.text != state.textDraft) {
        onAction(NoteAction.BottomSheetDiscard(true))
    } else {
        navigator.pop()
    }
}