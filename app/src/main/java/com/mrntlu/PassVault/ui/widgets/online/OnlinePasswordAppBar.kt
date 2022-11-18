package com.mrntlu.PassVault.ui.widgets.online

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.viewmodels.online.BottomSheetViewModel

@Composable
fun OnlinePasswordAppBar(
    selectedImage: String?,
    topBarImageSize: Dp,
    uiState: UIState<PasswordItem>,
    uiResponse: Response<Nothing>,
    bottomSheetVM: BottomSheetViewModel,
    onNavigationClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            if (uiState !is UIState.ViewItem) {
                Text(
                    text = if (uiState is UIState.AddItem)
                        stringResource(R.string.create)
                    else
                        stringResource(R.string.edit),
                    color = Color.White
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    PasswordSelectedImageView(
                        selectedImage = selectedImage,
                        imageSize = topBarImageSize,
                        uiState = uiState,
                        bottomSheetVM = bottomSheetVM,
                    )
                }
            }
        },
        navigationIcon = {
            if (uiResponse !is Response.Loading) {
                IconButton(
                    onClick = onNavigationClicked
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = Color.White
                    )
                }
            }
        },
        actions = { // Empty button to center password image
            TextButton(
                enabled = false,
                onClick = {},
                content = {},
            )
        },
        elevation = 8.dp,
        backgroundColor = BlueLogo,
    )
}

@Preview
@Composable
fun OnlinePasswordAppBarPreview() {

}