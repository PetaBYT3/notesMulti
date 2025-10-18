package org.notes.multi.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.FilePresent
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
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
import org.notes.multi.getFile
import org.notes.multi.openFile
import org.notes.multi.state.NoteState
import org.notes.multi.utilities.CustomDropDownMenu
import org.notes.multi.utilities.DropDownList
import org.notes.multi.utilities.SimpleConfirmationBottomSheet
import org.notes.multi.viewmodel.NoteViewModel

@OptIn(InternalVoyagerApi::class)
@Composable
fun NoteScreen(
    navController: NavController,
    uId: Long?,
    viewModel: NoteViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    LaunchedEffect(uId) {
        onAction(NoteAction.SelectedNotes(uId))
    }

    BackHandler(enabled = true) {
        unSavedChangesHandler(
            navController = navController,
            state = state,
            onAction = onAction,
        )
    }

    ScaffoldScreen(
        navController = navController,
        state = state,
        onAction = onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    navController: NavController,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            unSavedChangesHandler(
                                navController = navController,
                                state = state,
                                onAction = onAction,
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Row {
                        Text(text = "Note")
                        Spacer(Modifier.width(10.dp))
                        AnimatedVisibility(
                            visible = state.initialTitle != state.draftTitle || state.initialText != state.draftText,
                            content = {
                                Text(text = "Unsaved Changes")
                            }
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExpandedFloatingActionButton(
                navController = navController,
                snackBarHostState = snackBarHostState,
                state = state,
                onAction = onAction
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
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
                navController.popBackStack()
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

    if (state.bottomSheetDeleteDocument) {
        SimpleConfirmationBottomSheet(
            title = "Delete Document",
            text = """Are you sure you want to delete "${state.documentToDelete?.documentName}" ?""",
            onConfirmText = "Delete",
            onDismissText = "Cancel",
            onConfirm = {
                onAction(NoteAction.DeleteDocument)
            },
            onDismiss = {
                onAction(NoteAction.BottomSheetDeleteDocument(false, null))
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
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
                    val imageExtension = file.name.substringAfterLast(".")

                    onAction(NoteAction.SaveImage(
                        imageBytes = imageByte,
                        imageExtension = imageExtension
                    ))
                }
            }
        }
    )
    val documentPicker = rememberFilePickerLauncher(
        onResult = { file ->
            scope.launch {
                if (file != null) {
                    val documentByte = file.readBytes().toList()
                    val documentName = file.name

                    onAction(NoteAction.SaveDocument(
                        documentByte = documentByte,
                        documentName = documentName,
                    ))
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
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.image != null) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(getFile(state.image.imagePath))
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
                    val dropDownLists = listOf(
                        DropDownList(
                            title = "Upload Image",
                            icon = Icons.Rounded.Upload,
                            onClick = {
                                if (state.uId != 0L) {
                                    imagePicker.launch()
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Save at least once !",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            }
                        ),
                        DropDownList(
                            title = "Delete Image",
                            icon = Icons.Rounded.Delete,
                            onClick = {
                                if (state.image != null) {
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
                        dropDownList = dropDownLists,
                        isExpanded = state.dropDownImage,
                        onDismiss = { onAction(NoteAction.DropDownImage(false)) }
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Rounded.AttachFile,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = "${state.documents.size} Document Saved")
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            if (state.expandDocuments) {
                                onAction(NoteAction.ExpandDocument(false))
                            } else {
                                onAction(NoteAction.ExpandDocument(true))
                            }
                        }
                    ) {
                        val animatedRotation by animateFloatAsState(
                            targetValue = if (state.expandDocuments) 180f else 0f,
                            animationSpec = tween(500)
                        )

                        Icon(
                            modifier = Modifier
                                .rotate(animatedRotation),
                            imageVector = Icons.Rounded.ArrowDownward,
                            contentDescription = "More",
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.expandDocuments,
                    content = {
                        Column {
                            state.documents.forEach { document ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            enabled = true,
                                            onClick = {
                                                openFile(document.documentPath)
                                            }
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(Modifier.width(10.dp))
                                    Icon(
                                        imageVector = Icons.Rounded.FilePresent,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(
                                        modifier = Modifier
                                            .weight(1f),
                                        text = document.documentName,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    IconButton(
                                        onClick = {
                                            onAction(NoteAction.BottomSheetDeleteDocument(true, document))
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                onClick = {
                                    if (state.uId != 0L) {
                                        documentPicker.launch()
                                    } else {
                                        scope.launch {
                                            snackBarHostState.showSnackbar(
                                                message = "Save at least once !",
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                }
                            ) {
                                Text(text = "Add Document")
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = state.draftTitle,
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
            value = state.draftText,
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

@Composable
private fun ExpandedFloatingActionButton(
    navController: NavController,
    snackBarHostState: SnackbarHostState,
    state: NoteState,
    onAction: (NoteAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(
            visible = state.expandFloatingActionButton,
            enter = fadeIn() + slideInVertically(initialOffsetY = {it}),
            exit = fadeOut() + slideOutVertically(targetOffsetY = {it}),
            content = {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            if (state.draftTitle.isNotBlank()) {
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
                                        message = "Title can't be empty !",
                                        withDismissAction = true
                                    )
                                }
                            }
                        },
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Rounded.Save,
                                    contentDescription = "Save"
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(text = "Save")
                                Spacer(Modifier.width(10.dp))
                            }
                        }
                    )
                    SmallFloatingActionButton(
                        onClick = {
                            if (state.draftTitle.isNotBlank()) {
                                onAction(NoteAction.InsertNote)
                                navController.popBackStack()
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Title can't be empty !",
                                        withDismissAction = true
                                    )
                                }
                            }
                        },
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Rounded.ExitToApp,
                                    contentDescription = "Save"
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(text = "Save & Exit")
                                Spacer(Modifier.width(10.dp))
                            }
                        }
                    )
                    Spacer(Modifier.height(5.dp))
                }
            }
        )
        FloatingActionButton(
            onClick = {
                if (state.expandFloatingActionButton) {
                    onAction(NoteAction.ExpandFloatingActionButton(false))
                } else {
                    onAction(NoteAction.ExpandFloatingActionButton(true))
                }
            }
        ) {
            val animatedRotation by animateFloatAsState(
                targetValue = if (state.expandFloatingActionButton) 180f else 0f,
                animationSpec = tween(500)
            )
            Icon(
                modifier = Modifier
                    .rotate(animatedRotation),
                imageVector = Icons.Rounded.ArrowUpward,
                contentDescription = "Add"
            )
        }
    }
}

private fun unSavedChangesHandler(
    navController: NavController,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    if (state.initialTitle != state.draftTitle || state.initialText != state.draftText) {
        onAction(NoteAction.BottomSheetDiscard(true))
    } else {
        navController.popBackStack()
    }
}