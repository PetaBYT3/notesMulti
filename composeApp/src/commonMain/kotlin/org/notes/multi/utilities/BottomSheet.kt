package org.notes.multi.utilities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleConfirmationBottomSheet(
    title: String,
    text: String,
    onConfirmText: String,
    onDismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss.invoke()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(20.dp))
            Text(text = text)
            Spacer(Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss.invoke()
                        }
                    }
                ) {
                    Text(text = onDismissText)
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onConfirm.invoke()
                            onDismiss.invoke()
                        }
                    }
                ) {
                    Text(text = onConfirmText)
                }
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableUnitBottomSheet(
    title: String,
    content: @Composable () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            onDismiss.invoke()
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(20.dp))
            content()
            Spacer(Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss.invoke()
                        }
                    }
                ) {
                    Text(text = dismissButtonText)
                }
                Spacer(Modifier.width(10.dp))
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = {
                        onConfirm.invoke()
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss.invoke()
                        }
                    }
                ) {
                    Text(text = confirmButtonText)
                }
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}
