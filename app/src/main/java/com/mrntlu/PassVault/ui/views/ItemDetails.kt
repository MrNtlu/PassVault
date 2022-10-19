package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mrntlu.PassVault.utils.setGradientBackground
import com.mrntlu.PassVault.viewmodels.auth.FirebaseAuthViewModel
import com.mrntlu.PassVault.viewmodels.auth.ParseAuthViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ItemDetails(
    navController: NavHostController,
    firebaseVM: FirebaseAuthViewModel,
    parseVM: ParseAuthViewModel,
) {
    Scaffold{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .setGradientBackground(),
        ) {

        }
    }
}