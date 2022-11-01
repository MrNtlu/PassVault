package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.models.OfflinePassword

@Composable
fun OfflinePasswordList(
    passwords: List<OfflinePassword>,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDescriptionClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            count = if (passwords.isNotEmpty()) passwords.size + 1 else 1,
        ) { index ->
            if (passwords.isNotEmpty()) {
                if (index < passwords.size) {
                    val password = passwords[index]

                    OfflinePasswordListItem(
                        index = index,
                        password = password,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onDescriptionClicked = onDescriptionClicked,
                    )
                } else {
                    Spacer(modifier = Modifier.height(75.dp))
                }
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