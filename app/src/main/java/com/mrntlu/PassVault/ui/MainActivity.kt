package com.mrntlu.PassVault.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mrntlu.PassVault.AppIntros.SliderIntro
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.PassVaultTheme
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.utils.SearchWidgetState
import com.mrntlu.PassVault.utils.addInterstitialCallbacks
import com.mrntlu.PassVault.utils.loadInterstitial
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showSlider(applicationContext)
        firebaseAnalytics = Firebase.analytics

        setContent {
            PassVaultTheme {
                navController = rememberNavController()

                MainScreen(navController = navController)
            }
        }

        // Interstitial Init & Callbacks
        loadInterstitial(this)
        addInterstitialCallbacks(this)
    }

    private fun showSlider(context: Context) {
        val thread = Thread {
            val getPrefs = PreferenceManager.getDefaultSharedPreferences(context)
            val isFirstStart = getPrefs.getBoolean("should_show_intro", true)
            if (isFirstStart) {
                startActivity(Intent(context, SliderIntro::class.java))
                val e = getPrefs.edit()
                e.putBoolean("should_show_intro", false)
                e.apply()
            }
        }
        try {
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController
) {
    val focusManager = LocalFocusManager.current
    val parseVM = hiltViewModel<ParseAuthViewModel>()
    val homeVM = hiltViewModel<HomeViewModel>()
    val offlineVM = hiltViewModel<OfflineViewModel>()
    val onlinePasswordVM = hiltViewModel<OnlinePasswordViewModel>()

    val bottomBarItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_online),
            route = "home",
            icon = Icons.Rounded.Cloud
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_offline),
            route = "offline",
            icon = ImageVector.vectorResource(id = R.drawable.ic_database)
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_settings),
            route = "settings",
            icon = Icons.Rounded.Settings
        )
    )
    val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarItems.map { it.route }
    val isCurrentScreenHome = navController.currentBackStackEntry?.destination?.route == "home"
    val isCurrentScreenOffline = navController.currentBackStackEntry?.destination?.route == "offline"
    val isAuthLoading = parseVM.isLoading.value

    val isUserLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }
    var showDialog by remember { mutableStateOf(false) }
    var searchWidgetState by remember{ mutableStateOf(SearchWidgetState.CLOSED) }
    var searchTextState by remember { mutableStateOf("") }

    LaunchedEffect(key1 = isCurrentScreenHome) {
        if (!isCurrentScreenHome && searchWidgetState == SearchWidgetState.OPENED) {
            searchWidgetState = SearchWidgetState.CLOSED
            searchTextState = ""
        }
    }

    LaunchedEffect(key1 = isCurrentScreenOffline) {
        if (!isCurrentScreenOffline && searchWidgetState == SearchWidgetState.OPENED) {
            searchWidgetState = SearchWidgetState.CLOSED
            searchTextState = ""
        }
    }

    Scaffold(
        topBar = {
            if (
                (isCurrentScreenOffline && searchWidgetState == SearchWidgetState.OPENED) ||
                (isCurrentScreenHome && isUserLoggedIn.value && searchWidgetState == SearchWidgetState.OPENED)
            ) {
                SearchAppBar(
                    text = searchTextState,
                    onResetSearch = {
                        if (isCurrentScreenHome)
                            homeVM.resetPassword()
                        else
                            offlineVM.resetPassword()
                    },
                    onTextChange = { searchTextState = it },
                    onCloseClicked = { searchWidgetState = SearchWidgetState.CLOSED },
                    onSearchClicked = {
                        if (isCurrentScreenHome)
                            homeVM.searchPassword(it)
                        else
                            offlineVM.searchPassword(it)
                        focusManager.clearFocus(force = true)
                    }
                )
            } else if (navController.currentBackStackEntry?.destination?.route != "online") {
                DefaultAppBar(
                    navController = navController,
                    isAuthLoading = isAuthLoading,
                    isUserLoggedIn = isUserLoggedIn.value,
                    showBottomBar = showBottomBar,
                    isCurrentScreenHome = isCurrentScreenHome,
                    isCurrentScreenOffline = isCurrentScreenOffline,
                    onSearchClicked = { searchWidgetState = SearchWidgetState.OPENED },
                    onLogOutClicked = {
                        showDialog = true
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    items = bottomBarItems,
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    }
                )
            }
        }
    ) {
        NavigationComposable(
            navController = navController,
            padding = it,
            parseVM = parseVM,
            homeVM = homeVM,
            offlineVM = offlineVM,
            onlinePasswordVM = onlinePasswordVM
        )

        if (showDialog) {
            AYSDialog(
                text = stringResource(R.string.ays_logout),
                onConfirmClicked = {
                    showDialog = false
                    parseVM.parseSignout()
                }
            ) {
                showDialog = false
            }
        }

        if (isAuthLoading) {
            LoadingView()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PassVaultTheme {
        MainScreen(rememberNavController())
    }
}