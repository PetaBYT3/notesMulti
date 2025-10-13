package org.notes.multi.utilities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class MenuList(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    menuList: List<MenuList>,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(
            onClick = {
                if (expanded) {
                    expanded = false
                } else {
                    expanded = true
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Menu"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            menuList.forEach { menu ->
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
                        expanded = false
                    }
                )
            }
        }
    }
}