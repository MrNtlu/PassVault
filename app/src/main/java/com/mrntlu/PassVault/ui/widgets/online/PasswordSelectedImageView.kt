package com.mrntlu.PassVault.ui.widgets.online

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amulyakhare.textdrawable.TextDrawable
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.utils.Constants
import com.mrntlu.PassVault.utils.UIState
import com.mrntlu.PassVault.utils.isNetworkConnectionAvailable
import com.mrntlu.PassVault.viewmodels.online.BottomSheetViewModel

@Composable
fun PasswordSelectedImageView(
    selectedImage: String?,
    imageSize: Dp,
    uiState: UIState<PasswordItem>,
    bottomSheetVM: BottomSheetViewModel,
) {
    val context = LocalContext.current
    var isImageLoading by remember { mutableStateOf(false) }

    AnimatedVisibility (selectedImage == null || !context.isNetworkConnectionAvailable()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val drawable = TextDrawable.builder().buildRound(
                if (bottomSheetVM.titleState.isNotEmpty()) bottomSheetVM.titleState.trim { it <= ' ' }
                    .substring(0, 1)
                else "",
                bottomSheetVM.selectedColor.hashCode()
            )
            Image(
                modifier = Modifier
                    .size(imageSize),
                painter = rememberAsyncImagePainter(model = drawable),
                contentDescription = stringResource(id = R.string.cd_image),
            )

            if (uiState !is UIState.ViewItem) {
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.or),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }

    AnimatedVisibility(visible = selectedImage != null && context.isNetworkConnectionAvailable()) {
        Box(
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(animateColorAsState(bottomSheetVM.selectedColor).value),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(imageSize.minus(6.dp))
                    .clip(CircleShape),
                model = ImageRequest.Builder(context)
                    .data(Constants.ImageEndpoint + selectedImage)
                    .listener(
                        onStart = {
                            isImageLoading = true
                        },
                        onSuccess = { _, _ ->
                            isImageLoading = false
                        },
                        onError = { _, _ ->
                            isImageLoading = false
                        }
                    )
                    .build(),
                contentDescription = stringResource(id = R.string.cd_image),
            )

            if (isImageLoading) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
    
}