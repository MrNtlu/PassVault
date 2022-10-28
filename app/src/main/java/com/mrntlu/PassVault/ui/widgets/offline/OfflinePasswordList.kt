package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.models.OfflinePassword
import com.mrntlu.PassVault.utils.setGradientBackground

@Composable
fun OfflinePasswordList(
    passwords: List<OfflinePassword>,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDescriptionClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .setGradientBackground(),
    ) {
        items(
            count = if (passwords.isNotEmpty()) passwords.size else 1,
        ) { index ->
            if (passwords.isNotEmpty()) {
                val password = passwords[index]

                OfflinePasswordListItem(
                    index = index,
                    password = password,
                    onEditClicked = onEditClicked,
                    onDeleteClicked = onDeleteClicked,
                    onDescriptionClicked = onDescriptionClicked,
                )
            } else {
                NoItemView(modifier = Modifier.fillParentMaxSize())
            }
        }
    }
}

@Preview
@Composable
fun OfflinePasswordListPreview() {
    OfflinePasswordList(
        listOf(
            OfflinePassword(
                "test", "test", "test"
            )
        ),
        {}, {}, {}
    )
}

@Preview
@Composable
fun OfflinePasswordEmptyListPreview() {
    OfflinePasswordList(
        listOf(),{}, {}, {}
    )
}