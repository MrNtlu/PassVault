package com.mrntlu.PassVault.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.mrntlu.PassVault.AppIntros.SliderIntro
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.PassVaultTheme
import com.mrntlu.PassVault.ui.widgets.AYSDialog
import com.mrntlu.PassVault.ui.widgets.BottomNavigationBar
import com.mrntlu.PassVault.ui.widgets.LoadingView
import com.mrntlu.PassVault.utils.addInterstitialCallbacks
import com.mrntlu.PassVault.utils.loadInterstitial
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
    val firebaseVM = hiltViewModel<FirebaseAuthViewModel>()
    val parseVM = hiltViewModel<ParseAuthViewModel>()

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
    val isUserLoggedIn by remember { mutableStateOf(parseVM.isSignedIn) }
    val isAuthLoading = firebaseVM.isLoading.value || parseVM.isLoading.value
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when(navController.currentDestination?.route) {
                            "login" -> {
                                "Login"
                            }
                            "register" -> {
                                "Register"
                            }
                            else -> ""
                        },
                        color = Color.White
                    )
                },
                navigationIcon = {
                    if (!showBottomBar && navController.previousBackStackEntry != null && !isAuthLoading) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                actions = {
                    if (navController.currentBackStackEntry?.destination?.route == "home" && isUserLoggedIn.value) {
                        IconButton(
                            onClick = {
                                showDialog = true
                            }
                        ){
                            Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Log out", tint = Color.White)
                        }
                    }
                },
                elevation = 8.dp,
                backgroundColor = BlueLogo
            )
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
            parseVM = parseVM
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