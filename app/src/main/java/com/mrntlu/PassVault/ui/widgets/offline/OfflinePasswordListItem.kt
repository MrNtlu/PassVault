package com.mrntlu.PassVault.ui.widgets

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.OfflinePassword
import kotlin.math.min

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OfflinePasswordListItem(
    index: Int,
    password: OfflinePassword,
    onEditClicked: (Int) -> Unit,
    onDeleteClicked: (Int) -> Unit,
    onDescriptionClicked: (Int) -> Unit,
) {
    val context = LocalContext.current
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val color = remember { ColorGenerator.MATERIAL.randomColor }
    val drawable = remember { TextDrawable.builder().buildRound(password.idMail.trim { it <= ' ' }.substring(0, 1).capitalize(Locale.current), color) }

    var passwordVisiblityState by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        val clipData = ClipData.newPlainText("Password", password.password)

                        clipData.description.extras = PersistableBundle().apply {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                            } else {
                                putBoolean("android.content.extra.IS_SENSITIVE", true)
                            }
                        }
                        clipboard.setPrimaryClip(clipData)

                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                            Toast
                                .makeText(
                                    context,
                                    "${password.idMail} Copied",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    },
                    onTap = {
                        onDescriptionClicked(index)
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(start = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                painter = rememberAsyncImagePainter(model = drawable),
                contentDescription = stringResource(R.string.cd_image),
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
                    text = password.idMail,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )

                AnimatedContent(
                    targetState = passwordVisiblityState,
                ) { isPasswordVisible ->
                    Text(
                        text = if (isPasswordVisible)
                            password.password
                        else
                            "•".repeat(min(password.password.length, 12)),
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 6.dp)
                    .padding(horizontal = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                IconToggleButton(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .size(20.dp),
                    checked = passwordVisiblityState,
                    onCheckedChange = { passwordVisiblityState = !passwordVisiblityState },
                ) {
                    Icon(
                        imageVector = if (passwordVisiblityState) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                        contentDescription = stringResource(id = R.string.cd_password),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(R.string.cd_item_menu),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )

                    OfflineItemDropdownMenu(
                        index = index,
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        onEditClicked = onEditClicked,
                        onDeleteClicked = onDeleteClicked,
                        onDetailsClicked = onDescriptionClicked,
                        onCopyClicked = {
                            val clipData = ClipData.newPlainText("Password", password.password)

                            clipData.description.extras = PersistableBundle().apply {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                                } else {
                                    putBoolean("android.content.extra.IS_SENSITIVE", true)
                                }
                            }
                            clipboard.setPrimaryClip(clipData)

                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                                Toast
                                    .makeText(
                                        context,
                                        "${password.idMail} Copied",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OfflinePasswordListItemPreview() {
    OfflinePasswordListItem(
        0,
        OfflinePassword(
            "Test", "test", "test"
        ),
        {}, {}, {}
    )
}