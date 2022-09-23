package com.mrntlu.PassVault.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrntlu.PassVault.models.PasswordItem

@Composable
fun OnlinePasswordListItem(
    password: PasswordItem
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        elevation = 4.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    )
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = password.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                )

                Text(
                    text = password.username,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = password.password,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (password.note != null && password.note.isNotEmpty()) {
                    Text(
                        text = password.note,
                        color = Color.Black,
                        fontSize = 14.sp,
                    )
                }
            }

            IconButton(modifier = Modifier.padding(end = 4.dp).size(24.dp), onClick = { Toast.makeText(context, "${password.title} Coppied", Toast.LENGTH_SHORT).show() }) {
                Icon(
                    imageVector = Icons.Rounded.ContentCopy,
                    contentDescription = "Copy Password",
                    tint = Color.Black,
                )
            }
        }
    }
}

@Preview
@Composable
fun OnlinePasswordListItemPreview() {
    OnlinePasswordListItem(PasswordItem("Test", "Test Title", null, "Test Password"))

    OnlinePasswordListItem(PasswordItem("Test User Name This is an Example", "Test Long Title This is an example of long text", null, "Test Password"))
}