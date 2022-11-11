package com.mrntlu.PassVault.ui.widgets.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.runtime.*
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
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.printLog
import com.mrntlu.PassVault.utils.sendMail

data class SettingsClickTileModel(
    val title: String,
    val subTitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

private val accountSettingsList = listOf(
    SettingsClickTileModel(
        "Delete Account",
        "Delete your account & online storage.",
        Icons.Rounded.DeleteForever,
        {}
    )
)

@Composable
fun FutureSettings() {
    val localContext = LocalContext.current

    val reviewManager = remember {
        ReviewManagerFactory.create(localContext)
    }

    val reviewInfo = rememberReviewTask(reviewManager)

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
                .padding(top = 16.dp)
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp),
            text = "User Settings",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
        )

        LazyColumn {
            items(
                count = accountSettingsList.size
            ) { index ->
                SettingsClickTile(accountSettingsList[index])
            }
        }

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp),
            text = "Other Settings",
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
        )

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = "Rate & Review",
                    subTitle = "You can support by rating application.",
                    icon = Icons.Rounded.RateReview,
                    onClick = {
                        if (reviewInfo != null) {
                            reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
                        } else {
                            val playIntent = Intent().apply {
                                action = Intent.ACTION_VIEW

                                data = Uri.parse("https://play.google.com/store/apps/details?id=com.mrntlu.PassVault")

                            }
                            try {
                                localContext.startActivity(playIntent)
                            } catch (e: Exception) {
                                printLog("$e")
                            }
                        }
                    }
                )
            )

            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = "Feedback & Suggestion",
                    subTitle = "You can send email to mrntlu@gmail.com",
                    icon = Icons.Rounded.Feedback,
                    onClick = {
                        localContext.sendMail("mrntlu@gmail.com")
                    }
                )
            )
        }
    }
}

@Composable
fun SettingsClickTile(
    settingsClickTileModel: SettingsClickTileModel
) {
    Row(
        modifier = Modifier
            .height(75.dp)
            .background(Color.White)
            .clickable {
                settingsClickTileModel.onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(32.dp),
            imageVector = settingsClickTileModel.icon,
            contentDescription = ""
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = settingsClickTileModel.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )

            Text(
                text = settingsClickTileModel.subTitle,
                fontSize = 14.sp,
            )
        }

        //TODO: Better Arrow
        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(36.dp),
            imageVector = Icons.Rounded.ArrowRight,
            contentDescription = ""
        )
    }

    Divider()
}

@Composable
fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember { mutableStateOf(null) }

    reviewManager.requestReviewFlow().addOnCompleteListener {
        if (it.isSuccessful) {
            reviewInfo = it.result
        }
    }

    return reviewInfo
}