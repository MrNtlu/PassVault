package com.mrntlu.PassVault.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mrntlu.PassVault.utils.setGradientBackground

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .setGradientBackground(),
        verticalArrangement = Arrangement.Center,
    ) {
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth(),
//            factory = {
//                AboutBuilder.with(it)
//                    .setName("MrNtlu")
//                    .setSubTitle("Mobile Developer")
//                    .setAppIcon(R.mipmap.ic_launcher_round)
//                    .setAppName(R.string.app_name)
//                    .addGooglePlayStoreLink("8269784969410642250")
//                    .addWebsiteLink("http://burakfidan.com/")
//                    .addGitHubLink("MrNtlu")
//                    .addEmailLink("mrntlu@gmail.com")
//                    .addLinkedInLink("burak-fidan")
//                    .addFiveStarsAction("com.mrntlu.PassVault")
//                    .addMoreFromMeAction("MrNtlu")
//                    .addFeedbackAction("mrntlu@gmail.com")
//                    .setVersionNameAsAppSubTitle()
//                    .addShareAction(R.string.app_name)
//                    .setWrapScrollView(true)
//                    .setLinksAnimated(true)
//                    .setShowAsCard(true)
//                    .build()
//            }
//        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                count = 3
            ) {
                Divider()

                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    Text(text = "Delete account")

                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = "")
                }

                Divider()
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}