package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            count = if (passwords.isNotEmpty()) passwords.size + 1 else 1,
        ) { index ->
            if (passwords.isNotEmpty()) {
                if (index < passwords.size) {
                    val password = passwords[index]

                    OnlinePasswordListItem(
                        index = index,
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onItemClicked = onItemClicked,
                        password = password.toPasswordItem()
                    )
                } else {
                    Spacer(modifier = Modifier.height(75.dp)) //To prevent FAB overlap
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
            ParseObject.create("Account").apply {
                put("ParseUser", "Test Username")
                put("Title", "Test Title")
                put("Username", "Test Username")
                put("Password", "Test Password")
            }
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