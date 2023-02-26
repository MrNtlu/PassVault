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
import com.mrntlu.PassVault.utils.PreferenceStore
import com.mrntlu.PassVault.utils.findActivity
import com.mrntlu.PassVault.utils.isNetworkConnectionAvailable
import com.mrntlu.PassVault.utils.sendMail
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel
import com.parse.ParseUser
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
    val preferenceStore = remember { PreferenceStore(context) }
    val coroutineScope = rememberCoroutineScope()
    val systemTheme = isSystemInDarkTheme()

    var settingsErrorDialog by remember { mutableStateOf<String?>(null) }
    val isErrorOccured by remember { billingViewModel.isErrorOccured }
    val errorMessage by remember { billingViewModel.errorMessage }
    val message by remember { billingViewModel.message }
    val isPurchased by remember { billingViewModel.isPurchased }

    val reviewManager = remember {
        ReviewManagerFactory.create(context)
    }

    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            preferenceStore.getTheme(systemTheme).collect {
                themeViewModel.setTheme(it)
            }
        }

        billingViewModel.resetError()
        billingViewModel.resetMessage()
    }

    LaunchedEffect(key1 = isErrorOccured) {
        if (isErrorOccured) {
            billingViewModel.resetMessage()
            delay(4000L)
            billingViewModel.resetError()
        }
    }

    LaunchedEffect(key1 = message) {
        if (message != null) {
            billingViewModel.resetError()
            delay(4000L)
            billingViewModel.resetMessage()
        }
    }

    fun openReviewIntent() {
        val playIntent = Intent().apply {
            action = Intent.ACTION_VIEW

            data = Uri.parse("https://play.google.com/store/apps/details?id=com.mrntlu.PassVault")

        }
        try {
            context.startActivity(playIntent)
        } catch (_: Exception) {}
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        AnimatedVisibility(visible = isErrorOccured) {
            TopSnackbar(
                isError = true,
                message = errorMessage ?: ""
            ) {
                billingViewModel.resetError()
            }
        }

        AnimatedVisibility(visible = message != null) {
            TopSnackbar(
                isError = false,
                message = message ?: ""
            ) {
                billingViewModel.resetMessage()
            }
        }

        if(context.isNetworkConnectionAvailable()) {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Min),
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 8.dp),
                    text = stringResource(R.string.in_app_purchases),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if(!isPurchased) {
                    billingViewModel.productList?.forEach { product ->
                        SettingsClickTile(
                            settingsClickTileModel = SettingsClickTileModel(
                                title = product.product.title.split("(")[0],
                                subTitle = product.product.description,
                                icon = Icons.Rounded.ShoppingCart,
                                onClick = {
                                    try {
                                        if (context.findActivity() != null && ParseUser.getCurrentUser() != null) {
                                            Purchases.sharedInstance.purchasePackageWith(
                                                context.findActivity()!!,
                                                product,
                                                onError = { error, _ ->
                                                    billingViewModel.onPurchaseError(error)
                                                },
                                                onSuccess = { _, customerInfo ->
                                                    billingViewModel.onPurchaseSuccess(customerInfo)
                                                }
                                            )
                                        } else if (ParseUser.getCurrentUser() == null) {
                                            settingsErrorDialog = "You need to be logged in to remove ads."
                                        }
                                    } catch (_: Exception) {
                                        settingsErrorDialog = "You need to be logged in to remove ads."
                                    }
                                }
                            )
                        )
                    }

                    SettingsClickTile(
                        settingsClickTileModel = SettingsClickTileModel(
                            title = stringResource(R.string.restore_purchase),
                            subTitle = stringResource(R.string.restore_desc),
                            icon = Icons.Rounded.Restore,
                            onClick = {
                                if (ParseUser.getCurrentUser() != null) {
                                    billingViewModel.restorePurchase()
                                } else {
                                    settingsErrorDialog = "You need to be logged in to restore your purchase."
                                }
                            }
                        )
                    )
                } else {
                    SettingsSuccessTile()
                }
            }
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
                    onTitle = stringResource(R.string.dark_theme),
                    offTitle = stringResource(R.string.light_theme),
                    subTitle = stringResource(R.string.theme_desc),
                    onIcon = Icons.Rounded.DarkMode,
                    offIcon = Icons.Rounded.LightMode,
                    onSwitchChange = {
                        themeViewModel.setTheme(it)

                        coroutineScope.launch {
                            preferenceStore.saveTheme(it)
                        }
                    }
                )
            )

            SettingsClickTile(
                settingsClickTileModel = SettingsClickTileModel(
                    title = stringResource(R.string.rate_review),
                    subTitle = stringResource(R.string.rate_review_desc),
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
                    title = stringResource(R.string.feedback_suggestion),
                    subTitle = stringResource(R.string.feedback_desc),
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

    if (settingsErrorDialog != null) {
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            SettingsErrorDialog(
                text = settingsErrorDialog ?: "",
                onConfirmClicked = { navController.navigate("home") },
                onDismissClicked = {
                    showDialog = false
                    settingsErrorDialog = null
                }
            )
        }
    }
}