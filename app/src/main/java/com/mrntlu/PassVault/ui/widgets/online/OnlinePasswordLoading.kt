package com.mrntlu.PassVault.ui.widgets.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mrntlu.PassVault.ui.widgets.AnimatedShimmer

@Composable
fun OnlinePasswordLoading(
    isDarkThemeEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        repeat(15) {
            AnimatedShimmer(isDarkThemeEnabled)
        }
    }
}