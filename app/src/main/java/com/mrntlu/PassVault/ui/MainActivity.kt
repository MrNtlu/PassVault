package com.mrntlu.PassVault.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.mrntlu.PassVault.AppIntros.SliderIntro
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.PassVaultTheme
import com.mrntlu.PassVault.ui.widgets.BottomNavigationBar

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


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
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
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        NavigationComposable(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PassVaultTheme {
        MainScreen(rememberNavController())
    }
}