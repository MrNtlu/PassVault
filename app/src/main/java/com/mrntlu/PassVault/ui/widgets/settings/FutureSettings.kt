package com.mrntlu.PassVault.ui.widgets.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.utils.StoreTheme
import com.mrntlu.PassVault.utils.findActivity
import com.mrntlu.PassVault.utils.printLog
import com.mrntlu.PassVault.utils.sendMail
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.purchasePackageWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SettingsClickTileModel(
    val title: String,
    val subTitle: String?,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

data class SettingsSwitchTileModel(
    val onTitle: String,
    val offTitle: String,
    val subTitle: String?,
    val onIcon: ImageVector,
    val offIcon: ImageVector,
    val onSwitchChange: (Boolean) -> Unit,
)

@Composable
fun FutureSettings(
    themeViewModel: ThemeViewModel,
    billingViewModel: BillingViewModel,
    navController: NavController,
) {
    val context = LocalContext.current
    val storeTheme = remember { StoreTheme(context) }
    val coroutineScope = rememberCoroutineScope()
    val systemTheme = isSystemInDarkTheme()

    val isErrorOccured by billingViewModel.isErrorOccured
    val errorMessage by billingViewModel.errorMessage


    val reviewManager = remember {
        ReviewManagerFactory.create(context)
    }

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            storeTheme.getTheme(systemTheme).collect {
                themeViewModel.setTheme(it)
            }
        }

        billingViewModel.resetError()
    }

    LaunchedEffect(key1 = isErrorOccured) {
        if (isErrorOccured) {
            delay(4000L)
            billingViewModel.resetError()
        }
    }

    fun openReviewIntent() {
        val playIntent = Intent().apply {
            action = Intent.ACTION_VIEW

            data = Uri.parse("https://play.google.com/store/apps/details?id=com.mrntlu.PassVault")

        }
        try {
            context.startActivity(playIntent)
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
        AnimatedVisibility(visible = isErrorOccured) {
            ErrorTopSnackbar(error = errorMessage ?: "") {
                billingViewModel.resetError()
            }
        }

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp),
            text = "In-App Purchases",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Column(
            modifier = Modifier
                .height(IntrinsicSize.Min),
        ) {
            billingViewModel.productList?.forEach { product ->
                SettingsClickTile(
                    settingsClickTileModel = SettingsClickTileModel(
                        title = product.product.title.split("(")[0],
                        subTitle = product.product.description,
                        icon = Icons.Rounded.ShoppingCart,
                        onClick = {
                            if (context.findActivity() != null) {
                                Purchases.sharedInstance.purchasePackageWith(
                                    context.findActivity()!!,
                                    product,
                                    onError = { error, _ ->
                                        billingViewModel.onPurchaseError(error)
                                    },
                                    onSuccess = { purchase, customerInfo ->
                                        billingViewModel.onPurchaseSuccess(purchase, customerInfo)
                                    }
                                )
                            }
                        }
                    )
                )
            }

            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = "Restore Purchase",
                    subTitle = "Restore your previous purchase.",
                    icon = Icons.Rounded.Restore,
                    onClick = {
                        billingViewModel.restorePurchase()
                    }
                )
            )
        }

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
            SettingsSwitchTile(
                themeViewModel = themeViewModel,
                settingsSwitchTileModel = SettingsSwitchTileModel(
                    onTitle = "Dark Theme",
                    offTitle = "Light Theme",
                    subTitle = "You can change the application theme.",
                    onIcon = Icons.Rounded.DarkMode,
                    offIcon = Icons.Rounded.LightMode,
                    onSwitchChange = {
                        themeViewModel.setTheme(it)

                        coroutineScope.launch {
                            storeTheme.saveTheme(it)
                        }
                    }
                )
            )

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
                                    reviewManager.launchReviewFlow(context as Activity, reviewInfo)
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
                        context.sendMail("mrntlu@gmail.com")
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