package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.utils.toPasswordItem
import com.parse.ParseObject

@Composable
fun OnlinePasswordList(
    passwords: List<ParseObject>,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onItemClicked: (Int) -> Unit,
) {
    LazyColumn {
        items(
            count = passwords.size
        ) { index ->
            val password = passwords[index]

            OnlinePasswordListItem(
                index = index,
                onEditClicked = onEditClicked,
                onDeleteClicked = onDeleteClicked,
                onItemClicked = onItemClicked,
                password = password.toPasswordItem()
            )
        }
    }
}

@Preview
@Composable
fun OnlinePasswordListPreview() {
    OnlinePasswordList(
        listOf(
            ParseObject.create("Account").apply {
                put("ParseUser", "Test Username")
                put("Title", "Test Title")
                put("Username", "Test Username")
                put("Password", "Test Password")
            }
        ), {}, {}, {}
    )
}