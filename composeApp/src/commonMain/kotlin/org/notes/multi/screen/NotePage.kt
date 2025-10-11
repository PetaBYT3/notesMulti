package org.notes.multi.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.NoteAction
import org.notes.multi.state.NoteState
import org.notes.multi.viewmodel.NoteViewModel

@Composable
fun NoteScreen(
    noteUid: Int?,
    viewModel: NoteViewModel = koinViewModel(),
    navigator: Navigator = LocalNavigator.currentOrThrow,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScaffoldScreen(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    navigator: Navigator,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.pop().apply {
                                onAction(NoteAction.IsPageClosed)
                            }
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
                onClick = {},
                text = { Text(text = "Save Note") },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = "Save Note"
                    )
                }
            )
        }

    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            navigator = navigator,
            state = state,
            onAction = onAction,
        )
    }

}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    state: NoteState,
    onAction: (NoteAction) -> Unit,
) {
    Column(
        modifier = modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        Row {
            TextField(
                modifier = Modifier.weight(1f),
                value = state.titleDraft ?: "",
                onValueChange = { onAction(NoteAction.TitleDraft(it)) },
                placeholder = { Text(text = "Title") },
                enabled = state.isTitleEditingEnabled
            )
            Spacer(Modifier.width(10.dp))
            IconButton(
                onClick = {
                    if (state.isTitleEditingEnabled) {
                        onAction(NoteAction.IsTitleEditEnabled(false))
                    } else {
                        onAction(NoteAction.IsTitleEditEnabled(true))
                    }
                }
            ) {
                Icon(
                    imageVector =
                        if (state.isTitleEditingEnabled)
                            Icons.Rounded.Close
                        else
                            Icons.Rounded.Edit,
                    contentDescription = "Edit Title"
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        TextField(
            modifier = Modifier
                .fillMaxSize(),
            value = state.textDraft ?: "",
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