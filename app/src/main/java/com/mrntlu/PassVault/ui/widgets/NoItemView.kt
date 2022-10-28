package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NoAccounts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrntlu.PassVault.R

@Composable
fun NoItemView(
    modifier: Modifier,
    icon: ImageVector = Icons.Rounded.NoAccounts,
    text: String = stringResource(R.string.no_item_text)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(58.dp),
            imageVector = icon,
            contentDescription = stringResource(R.string.cd_empty),
            tint = Color.White,
        )

        Text(
            modifier = Modifier.padding(top = 3.dp),
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun NoItemViewPreview() {
    NoItemView(Modifier.fillMaxSize())
}