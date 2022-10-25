package com.mrntlu.PassVault.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.mrntlu.PassVault.AppIntros.SliderIntro
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.PassVaultTheme
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.utils.SearchWidgetState
import com.mrntlu.PassVault.utils.addInterstitialCallbacks
import com.mrntlu.PassVault.utils.loadInterstitial
import com.mrntlu.PassVault.viewmodels.HomeViewModel
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showSlider(applicationContext)

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
    val firebaseVM = hiltViewModel<FirebaseAuthViewModel>()
    val parseVM = hiltViewModel<ParseAuthViewModel>()
    val homeScreenVM = hiltViewModel<HomeViewModel>()

    val bottomBarItems = listOf(
        BottomNavItem(
            name ="Online Storage",
            route = "home",
            icon = Icons.Rounded.Home
        ),
        BottomNavItem(
            name ="Offline Storage",
            route = "offline",
            icon = Icons.Rounded.Lock
        ),
        BottomNavItem(
            name ="Settings",
            route = "settings",
            icon = Icons.Rounded.Settings
        )
    )
    val showBottomBar = navController.currentBackStackEntryAsState().value?.destination?.route in bottomBarItems.map { it.route }
    val isCurrentScreenHome = navController.currentBackStackEntry?.destination?.route == "home"
    val isAuthLoading = firebaseVM.isLoading.value || parseVM.isLoading.value

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

    Scaffold(
        topBar = {
            if (isCurrentScreenHome && isUserLoggedIn.value && searchWidgetState == SearchWidgetState.OPENED) {
                SearchAppBar(
                    text = searchTextState,
                    onResetSearch = { homeScreenVM.resetPassword() },
                    onTextChange = { searchTextState = it },
                    onCloseClicked = { searchWidgetState = SearchWidgetState.CLOSED },
                    onSearchClicked = {
                        homeScreenVM.searchPassword(it)
                        focusManager.clearFocus(force = true)
                    }
                )
            } else {
                DefaultAppBar(
                    navController = navController,
                    isAuthLoading = isAuthLoading,
                    isUserLoggedIn = isUserLoggedIn.value,
                    showBottomBar = showBottomBar,
                    isCurrentScreenHome = isCurrentScreenHome,
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
            firebaseVM = firebaseVM,
            parseVM = parseVM,
            homeViewmodel = homeScreenVM,
        )

        if (showDialog) {
            AYSDialog(
                text = "Do you want to log out?",
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