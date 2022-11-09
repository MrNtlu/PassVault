package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.models.PasswordItem

@Composable
fun OnlinePasswordList(
    passwords: List<PasswordItem>,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        items(
            count = if (passwords.isNotEmpty()) passwords.size + 1 else 1,
            key = { index ->
                if (passwords.isNotEmpty() && index < passwords.size) {
                    passwords[index].parseID
                } else ""
            }
        ) { index ->
            if (passwords.isNotEmpty()) {
                if (index < passwords.size) {
                    val password = passwords[index]

                    OnlinePasswordListItem(
                        index = index,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onItemClicked = onItemClicked,
                        password = password
                    )
                } else {
                    Spacer(modifier = Modifier.height(80.dp)) //To prevent FAB overlap
                }
            } else {
                NoItemView(modifier = Modifier.fillParentMaxSize())
            }
        }
    }
}

@Preview
@Composable
fun OnlinePasswordListPreview() {
    OnlinePasswordList(
        listOf(
            PasswordItem(
                "Test Username",
                "Test Title",
                null,
                "Test Password",
                true,
            )
        ), {}, {}, {}
    )
}

@Preview
@Composable
fun OnlinePasswordListEmptyPreview() {
    OnlinePasswordList(
        listOf(), {}, {}, {}
    )
}