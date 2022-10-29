package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.mrntlu.PassVault.R

data class SettingsTileModel(
    val title: String,
    val subTitle: String,
    val icon: ImageVector,
)

private val accountSettingsList = listOf(
    SettingsTileModel(
        "Delete Account",
        "Delete your account & online storage.",
        Icons.Rounded.DeleteForever
    )
)

private val appSettingsList = listOf(
    SettingsTileModel(
        "Dark Theme",
        "Enable/Disable Dark Theme",
        Icons.Rounded.DarkMode,
    )
)

private val otherSettingsList = listOf(
    SettingsTileModel(
        "Rate & Review",
        "You can support by rating application.",
        Icons.Rounded.RateReview,
    ),
    SettingsTileModel(
        "Feedback & Suggestion",
        "You can send email to mrntlu@gmail.com",
        Icons.Rounded.Feedback,
    )
)

@Composable
fun FutureSettings() {
    Column {
        ContextCompat.getDrawable(LocalContext.current, R.mipmap.ic_launcher_round)?.let {
            Image(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                bitmap = it.toBitmap().asImageBitmap(),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier
                .padding(12.dp),
            text = "User Settings",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
        )

        LazyColumn {
            items(
                count = accountSettingsList.size
            ) { index ->
                SettingsTile(accountSettingsList[index])
            }
        }

        Text(
            modifier = Modifier
                .padding(12.dp),
            text = "Other Settings",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(
                count = otherSettingsList.size
            ) { index ->
                SettingsTile(otherSettingsList[index])
            }
        }
    }
}

@Composable
fun SettingsTile(
    settingsTileModel: SettingsTileModel
) {
    Row(
        modifier = Modifier
            .height(75.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = settingsTileModel.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = settingsTileModel.subTitle,
                fontSize = 14.sp,
            )
        }

        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(36.dp),
            imageVector = settingsTileModel.icon,
            contentDescription = ""
        )
    }

    Divider()
}