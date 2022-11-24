package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.mrntlu.PassVault.R

@Composable
fun NoItemView(
    modifier: Modifier,
    text: String = stringResource(R.string.no_item_text)
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_item))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever)

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            modifier = Modifier
                .height(275.dp),
            composition = composition,
            progress = { progress },
        )

        Text(
            modifier = Modifier
                .padding(vertical = 16.dp),
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun NoItemViewPreview() {
    NoItemView(Modifier.fillMaxSize())
}