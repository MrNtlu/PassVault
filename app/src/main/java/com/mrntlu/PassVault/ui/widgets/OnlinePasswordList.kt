package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NoAccounts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            count = if (passwords.isNotEmpty()) passwords.size else 1,
        ) { index ->
            if (passwords.isNotEmpty()) {
                val password = passwords[index]

                OnlinePasswordListItem(
                    index = index,
                    onEditClicked = onEditClicked,
                    onDeleteClicked = onDeleteClicked,
                    onItemClicked = onItemClicked,
                    password = password.toPasswordItem()
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(58.dp),
                        imageVector = Icons.Rounded.NoAccounts,
                        contentDescription = "Empty Icon",
                        tint = Color.White,
                    )

                    Text(
                        modifier = Modifier.padding(top = 3.dp),
                        text = "Nothing in here :(",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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