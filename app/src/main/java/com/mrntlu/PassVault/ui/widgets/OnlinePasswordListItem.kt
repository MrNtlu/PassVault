package com.mrntlu.PassVault.ui.widgets

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mrntlu.PassVault.models.PasswordItem
import kotlin.math.min

@Composable
fun OnlinePasswordListItem(
    index: Int,
    onItemClicked: (Int) -> Unit,
    password: PasswordItem
) {
    val color = remember { ColorGenerator.MATERIAL.randomColor }
    val drawable = remember { TextDrawable.builder().buildRound(password.title.trim { it <= ' ' }.substring(0, 1), color) }

    val context = LocalContext.current
    val passwordVisiblityState = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable { onItemClicked(index) },
        elevation = 4.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                painter = rememberAsyncImagePainter(model = drawable),
                contentDescription = "",
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    )
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
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

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (passwordVisiblityState.value)
                            password.password
                        else
                            "•".repeat(min(password.password.length, 12)),
                        color = Color.Black,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    IconToggleButton(
                        modifier = Modifier.padding(end = 16.dp).size(24.dp),
                        checked = passwordVisiblityState.value,
                        onCheckedChange = {
                            passwordVisiblityState.value = it
                        },
                    ) {
                        Icon(
                            imageVector = if (passwordVisiblityState.value) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                            contentDescription = "Toggle Password Visibility",
                            tint = Color.Black,
                        )
                    }

                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = {
                            Toast.makeText(
                                context,
                                "${password.title} Coppied",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "Copy Password",
                            tint = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OnlinePasswordListItemPreview() {
    OnlinePasswordListItem(0, {},PasswordItem("Test", "Test Title", null, "Test Password"))

    OnlinePasswordListItem(0, {}, PasswordItem("Test User Name This is an Example", "Test Long Title This is an example of long text", "This is an example note", "Test Password"))
}