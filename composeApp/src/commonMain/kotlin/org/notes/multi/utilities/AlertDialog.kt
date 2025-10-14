package org.notes.multi.utilities

import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleConfirmationAlertDialog(
    title: String,
    text: String,
    onConfirmText: String,
    onDismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val dialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = true
    )

    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = dialogProperties,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(MaterialTheme.colorScheme.background)
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
                            onDismiss.invoke()
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
                                onConfirm.invoke()
                            }.invokeOnCompletion {
                                onDismiss.invoke()
                            }
                        }
                    ) {
                        Text(text = onConfirmText)
                    }
                }
            }
        },
    )

//    BasicAlertDialog(
//        onDismissRequest = { onDismiss.invoke() },
//        properties = dialogProperties,
//        content = {
//
//        }
//    )
}