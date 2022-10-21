package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnlineItemDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    index: Int,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
    ) {
        OnlineItemDropdownMenuItem(
            text = "Edit",
            icon = Icons.Rounded.Edit
        ) {
            onDismissRequest()
            onEditClicked(index)
        }

        OnlineItemDropdownMenuItem(
            text = "Delete",
            icon = Icons.Rounded.Delete
        ) {
            onDismissRequest()
            onDeleteClicked(index)
        }

        Divider()

        OnlineItemDropdownMenuItem(
            text = "Details",
            icon = Icons.Rounded.Info
        ) {
            onDismissRequest()
            onDetailsClicked(index)
        }
    }
}

@Composable
fun OnlineItemDropdownMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        onClick = { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = icon,
                contentDescription = text,
            )

            Text(
                text = text,
                fontWeight = FontWeight.Medium
            )
        }
    }
}