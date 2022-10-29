package com.mrntlu.PassVault.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.DarkWhite
import com.vansuita.materialabout.builder.AboutBuilder

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkWhite),
        verticalArrangement = Arrangement.Center,
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = {
                AboutBuilder.with(it)
                    .setPhoto(R.mipmap.profile_picture)
                    .setCover(R.mipmap.profile_cover)
                    .setName("MrNtlu")
                    .setSubTitle("Mobile Developer")
                    .setAppIcon(R.mipmap.ic_launcher_round)
                    .setAppName(R.string.app_name)
                    .addGooglePlayStoreLink("8269784969410642250")
                    .addWebsiteLink("http://burakfidan.com/")
                    .addGitHubLink("MrNtlu")
                    .addEmailLink("mrntlu@gmail.com")
                    .addLinkedInLink("burak-fidan")
                    .addFiveStarsAction("com.mrntlu.PassVault")
                    .addMoreFromMeAction("MrNtlu")
                    .addFeedbackAction("mrntlu@gmail.com")
                    .setVersionNameAsAppSubTitle()
                    .addShareAction(R.string.app_name)
                    .setWrapScrollView(true)
                    .setLinksAnimated(true)
                    .setShowAsCard(false)
                    .build()
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { /*TODO Open Terms & Codifitions*/ }
            ) {
                Text(
                    text = "Terms & Conditions",
                    fontSize = 12.sp,
                )
            }

            TextButton(
                onClick = { /*TODO Open Privacy Policy*/ }
            ) {
                Text(
                    text = "Privacy Policy",
                    fontSize = 12.sp,
                )
            }
        }
    }
}



@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}