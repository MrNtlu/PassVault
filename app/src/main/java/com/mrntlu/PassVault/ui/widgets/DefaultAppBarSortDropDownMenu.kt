package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.SortType

@Composable
fun DefaultAppBarSortDropDownMenu(
    dropdownExpanded: Boolean,
    onDropdownDismiss: () -> Unit,
    sortType: SortType,
    onSortTypeChanged: (SortType) -> Unit,
    onSortClicked: () -> Unit,
) {
    IconButton(
        onClick = onSortClicked
    ){
        Icon(
            imageVector = Icons.Rounded.Sort,
            contentDescription = stringResource(R.string.cd_sort),
            tint = Color.White,
        )

        if (dropdownExpanded) { //To prevent memory leak!
            DropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                expanded = dropdownExpanded,
                onDismissRequest = onDropdownDismiss
            ) {
                for (item in SortType.values()) {
                    DropdownMenuItem(
                        onClick = {
                            onDropdownDismiss()
                            onSortTypeChanged(item)
                        }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (item.title == sortType.title) {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(24.dp),
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = stringResource(id = R.string.cd_sort),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(24.dp)
                                )
                            }

                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}