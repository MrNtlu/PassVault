package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetButtons(
    isConfirmButtonAvailable: Boolean = true,
    confirmBGColor: Color,
    confirmText: String,
    confirmTextColor: Color,
    onConfirmClicked: () -> Unit,
    dismissText: String,
    onDismissClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (isConfirmButtonAvailable) {
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = confirmBGColor
                ),
                onClick = onConfirmClicked,
            ) {
                Text(
                    text = confirmText,
                    color = confirmTextColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onDismissClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        ) {
            Text(
                text = dismissText,
                color = MaterialTheme.colorScheme.surfaceVariant,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}