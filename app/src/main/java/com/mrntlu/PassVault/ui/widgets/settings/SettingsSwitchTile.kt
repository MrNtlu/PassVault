package com.mrntlu.PassVault.ui.widgets.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.viewmodels.shared.ThemeViewModel

@Composable
fun SettingsSwitchTile(
    themeViewModel: ThemeViewModel,
    settingsSwitchTileModel: SettingsSwitchTileModel,
) {
    var switchState by remember { themeViewModel.isDarkThemeEnabled }

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
            imageVector = if (switchState) settingsSwitchTileModel.onIcon else settingsSwitchTileModel.offIcon,
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
                text = if (switchState) settingsSwitchTileModel.onTitle else settingsSwitchTileModel.offTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            settingsSwitchTileModel.subTitle?.let {
                Text(
                    text = it,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Switch(
            modifier = Modifier
                .padding(end = 4.dp),
            checked = switchState,
            onCheckedChange = {
                switchState = it

                settingsSwitchTileModel.onSwitchChange(it)
            },
            thumbContent = {
                Icon(
                    modifier = Modifier
                        .size(SwitchDefaults.IconSize),
                    imageVector = if (switchState) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                    contentDescription = ""
                )
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }

    Divider()
}

@Preview
@Composable
fun SettingsSwitchTilePreview() {
    SettingsSwitchTile(
        viewModel(),
        settingsSwitchTileModel = SettingsSwitchTileModel(
            onTitle = "Dark Theme",
            offTitle = "Light Theme",
            subTitle = "You can change application theme.",
            onIcon = Icons.Rounded.DarkMode,
            offIcon = Icons.Rounded.LightMode,
        ){}
    )
}