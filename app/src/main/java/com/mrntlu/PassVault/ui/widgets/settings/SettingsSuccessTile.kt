package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.Green500

@Composable
fun SettingsSuccessTile() {
    Row(
        modifier = Modifier
            .height(65.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(start = 12.dp)
                .size(24.dp),
            imageVector = Icons.Rounded.Verified,
            contentDescription = "",
            tint = Green500,
        )

        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.ads_removed),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
            )


            Text(
                text = stringResource(R.string.ads_thanks),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }

    Divider()
}