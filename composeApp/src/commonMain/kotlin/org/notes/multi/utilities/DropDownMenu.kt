package org.notes.multi.utilities

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DropDownList(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun CustomDropDownMenu(
    dropDownList: List<DropDownList>,
    isExpanded: Boolean,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {
        dropDownList.forEach { menu ->
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = menu.icon,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(text = menu.title)
                    }
                },
                onClick = {
                    menu.onClick()
                    onDismiss.invoke()
                }
            )
        }
    }
}