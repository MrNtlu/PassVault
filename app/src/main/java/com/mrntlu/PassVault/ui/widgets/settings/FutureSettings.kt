package com.mrntlu.PassVault.ui.widgets.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.printLog
import com.mrntlu.PassVault.utils.sendMail

data class SettingsClickTileModel(
    val title: String,
    val subTitle: String?,
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
fun FutureSettings(
    navController: NavController,
) {
    val localContext = LocalContext.current

    val reviewManager = remember {
        ReviewManagerFactory.create(localContext)
    }

    fun openReviewIntent() {
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

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        ContextCompat.getDrawable(LocalContext.current, R.mipmap.ic_launcher_round)?.let {
            Image(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                bitmap = it.toBitmap().asImageBitmap(),
                contentDescription = null
            )
        }

//        Text(
//            modifier = Modifier
//                .padding(top = 16.dp)
//                .padding(horizontal = 12.dp)
//                .padding(bottom = 8.dp),
//            text = "User Settings",
//            fontWeight = FontWeight.Bold,
//            color = Color.DarkGray,
//        )
//
//        LazyColumn(
//            modifier = Modifier
//                .height(65.dp.times(accountSettingsList.size)),
//        ) {
//            items(
//                count = accountSettingsList.size,
//            ) { index ->
//                SettingsClickTile(accountSettingsList[index])
//            }
//        }

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp),
            text = stringResource(id = R.string.settings),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(
            modifier = Modifier
                .height(IntrinsicSize.Min),
        ) {
            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = "Rate & Review",
                    subTitle = "Please take a moment to rate it in Play Store.",
                    icon = Icons.Rounded.RateReview,
                    onClick = {
                        try {
                            val request = reviewManager.requestReviewFlow()
                            request.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val reviewInfo = it.result
                                    reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
                                } else {
                                    openReviewIntent()
                                }
                            }
                        } catch (_: Error) {
                            openReviewIntent()
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

            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = stringResource(R.string.terms_conditions_),
                    subTitle = null,
                    icon = Icons.Rounded.AccountBalance,
                    onClick = {
                        navController.navigate("policy/${true}")
                    }
                )
            )

            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = stringResource(R.string.privacy_policy_),
                    subTitle = null,
                    icon = Icons.Rounded.PrivacyTip,
                    onClick = {
                        navController.navigate("policy/${false}")
                    }
                )
            )
        }
    }
}