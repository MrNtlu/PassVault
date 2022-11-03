package com.mrntlu.PassVault.ui.widgets

import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mrntlu.PassVault.R

@Composable
fun OfflineItemDropdownMenu(
    index: Int,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
    onCopyClicked: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded, 
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(
            text = stringResource(id = R.string.edit),
            icon = Icons.Rounded.Edit
        ) {
            onDismissRequest()
            onEditClicked(index)
        }

        DropdownMenuItem(
            text = stringResource(id = R.string.delete),
            icon = Icons.Rounded.Delete
        ) {
            onDismissRequest()
            onDeleteClicked(index)
        }

        Divider()

        DropdownMenuItem(
            text = stringResource(R.string.copy_str),
            icon = Icons.Rounded.ContentCopy
        ) {
            onDismissRequest()
            onCopyClicked()
        }

        DropdownMenuItem(
            text = stringResource(R.string.details),
            icon = Icons.Rounded.Info
        ) {
            onDismissRequest()
            onDetailsClicked(index)
        }
    }
}