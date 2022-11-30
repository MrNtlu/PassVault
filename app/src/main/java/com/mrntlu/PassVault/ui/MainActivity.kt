package com.mrntlu.PassVault.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.PassVaultTheme
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.BillingViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel
import com.parse.ParseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val themeViewModel: ThemeViewModel by viewModels()
    private var storeTheme: StoreTheme? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics
        storeTheme = StoreTheme(applicationContext)

        val systemTheme = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> { true }
            Configuration.UI_MODE_NIGHT_NO -> { false }
            else -> { false }
        }

        setContent {
            val theme = storeTheme?.getTheme(systemTheme)?.collectAsState(initial = systemTheme)

            PassVaultTheme(
                darkTheme = theme?.value ?: false,
            ) {
                navController = rememberNavController()
                window.statusBarColor = MaterialTheme.colorScheme.primary.toArgb()

                MainScreen(
                    themeViewModel = themeViewModel,
                    navController = navController,
                )
            }
        }

        // Interstitial Init & Callbacks
        loadInterstitial(this)
        addInterstitialCallbacks(this)
    }

    override fun onDestroy() {
        storeTheme = null
        removeInterstitial()
        super.onDestroy()
    }
}

@Composable
fun MainScreen(
    themeViewModel: ThemeViewModel,
    navController: NavHostController,
) {
    val focusManager = LocalFocusManager.current
    val parseVM = hiltViewModel<ParseAuthViewModel>()
    val homeVM = hiltViewModel<HomeViewModel>()
    val offlineVM = hiltViewModel<OfflineViewModel>()
    val onlinePasswordVM = hiltViewModel<OnlinePasswordViewModel>()
    val billingVM = hiltViewModel<BillingViewModel>()

    val bottomBarItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_online),
            route = "home",
            icon = Icons.Rounded.Cloud,
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_offline),
            route = "offline",
            icon = ImageVector.vectorResource(id = R.drawable.ic_database)
        ),
        BottomNavItem(
            name = stringResource(R.string.bottom_nav_settings),
            route = "settings",
            icon = Icons.Rounded.Settings,
        )
    )
    val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarItems.map { it.route }
    val isCurrentScreenHome = navController.currentBackStackEntry?.destination?.route == "home"
    val isCurrentScreenOffline = navController.currentBackStackEntry?.destination?.route == "offline"
    val isAuthLoading = parseVM.isLoading.value

    val isUserLoggedIn by remember { parseVM.isSignedIn }
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

    LaunchedEffect(key1 = isUserLoggedIn) {
        if (isUserLoggedIn) {
            billingVM.loginUser(ParseUser.getCurrentUser().objectId)
        } else {
            billingVM.logoutUser()
        }
    }

    Scaffold(
        topBar = {
            if (
                (isCurrentScreenOffline && searchWidgetState == SearchWidgetState.OPENED) ||
                (isCurrentScreenHome && isUserLoggedIn && searchWidgetState == SearchWidgetState.OPENED)
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
                    isUserLoggedIn = isUserLoggedIn,
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
                    },
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
            onlinePasswordVM = onlinePasswordVM,
            themeViewModel = themeViewModel,
            billingViewModel = billingVM,
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
    PassVaultTheme(true) {
        MainScreen(viewModel(), rememberNavController())
    }
}