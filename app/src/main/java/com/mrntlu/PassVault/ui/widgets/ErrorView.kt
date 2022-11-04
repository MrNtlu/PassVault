package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.mrntlu.PassVault.utils.setGradientBackground

@Composable
fun ErrorView(
    error: String,
    lottieFile: Int,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieFile))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .setGradientBackground(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = error,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
        )

        LottieAnimation(
            modifier = Modifier
                .padding(top = 16.dp)
                .height(275.dp),
            composition = composition,
            progress = { progress },
        )
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    ErrorView(
        error = "No internet connection",
        lottieFile = com.mrntlu.PassVault.R.raw.no_internet
    )
}