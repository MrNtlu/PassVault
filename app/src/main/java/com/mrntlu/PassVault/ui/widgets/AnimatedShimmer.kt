package com.mrntlu.PassVault.ui.widgets

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedShimmer(
    isDarkTheme: Boolean
) {
    val color = if (isDarkTheme) Color.LightGray else Color.Gray

    val shimmerColors = listOf(
        color.copy(alpha = 0.6f),
        color.copy(alpha = 0.2f),
        color.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 750f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 750,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    ShimmerOnlineLoadingItem(brush = brush)
}

@Composable
fun ShimmerOnlineLoadingItem(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(start = 6.dp)
                .padding(vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(brush),
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    )
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .height(24.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(brush),
                )

                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .background(brush),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 6.dp)
                    .padding(horizontal = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(end = 3.dp)
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush),
                )

                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(brush),
                )
            }
        }
    }
}