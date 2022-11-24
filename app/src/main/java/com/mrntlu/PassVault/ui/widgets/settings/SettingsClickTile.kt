package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.RateReview
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsClickTile(
    settingsClickTileModel: SettingsClickTileModel
) {
    Row(
        modifier = Modifier
            .height(65.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                settingsClickTileModel.onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(24.dp),
            imageVector = settingsClickTileModel.icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = settingsClickTileModel.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            settingsClickTileModel.subTitle?.let {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(28.dp),
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }

    Divider(color = MaterialTheme.colorScheme.onBackground)
}

@Preview
@Composable
fun SettingsClickTilePreview() {
    SettingsClickTile(
        settingsClickTileModel = SettingsClickTileModel(
            title = "Rate & Review",
            subTitle = "You can support by rating application.",
            icon = Icons.Rounded.RateReview,
            onClick = {}
        )
    )
}