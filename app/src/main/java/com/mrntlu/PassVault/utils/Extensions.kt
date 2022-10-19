package com.mrntlu.PassVault.utils

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo

fun Modifier.setGradientBackground(): Modifier = background(
    brush = Brush.verticalGradient(
        colors = listOf(
            BlueLogo,
            BlueDark,
            BlueDarkest
        )
    )
)