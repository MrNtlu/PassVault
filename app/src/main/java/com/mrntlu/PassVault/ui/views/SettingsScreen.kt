package com.mrntlu.PassVault.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.DarkWhite
import com.vansuita.materialabout.builder.AboutBuilder

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(DarkWhite),
        verticalArrangement = Arrangement.Center,
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            factory = {
                AboutBuilder.with(it)
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
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = { navController.navigate("policy/${true}") }
            ) {
                Text(
                    text = stringResource(R.string.terms_conditions_),
                    fontSize = 12.sp,
                    color = BlueDarkest,
                    fontWeight = FontWeight.Bold,
                )
            }

            TextButton(
                onClick = { navController.navigate("policy/${false}") },
            ) {
                Text(
                    text = stringResource(R.string.privacy_policy_),
                    fontSize = 12.sp,
                    color = BlueDarkest,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        /*Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            TextButton(
                onClick = { *//*TODO Implement delete account*//* }
            ) {
                Text(
                    text = "Delete Account",
                    fontSize = 10.sp,
                    color = BlueMidnight,
                    fontWeight = FontWeight.Bold,
                )
            }
        }*/
    }
}



@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(rememberNavController())
}