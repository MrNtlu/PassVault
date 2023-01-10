package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.isNetworkConnectionAvailable

@Composable
fun OnlineItemDropdownMenu(
    index: Int,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
) {
    val context = LocalContext.current

    DropdownMenu(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        if (context.isNetworkConnectionAvailable()) {
            DropdownMenuItem(
                text = stringResource(id = R.string.edit),
                icon = Icons.Rounded.Edit,
            ) {
                onDismissRequest()
                onEditClicked(index)
            }

            DropdownMenuItem(
                text = stringResource(id = R.string.delete),
                icon = Icons.Rounded.Delete,
            ) {
                onDismissRequest()
                onDeleteClicked(index)
            }

            Divider()
        }

        DropdownMenuItem(
            text = stringResource(R.string.details),
            icon = Icons.Rounded.Info,
        ) {
            onDismissRequest()
            onDetailsClicked(index)
        }
    }
}

@Composable
fun DropdownMenuItem(
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
                tint = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}