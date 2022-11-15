package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.amulyakhare.textdrawable.TextDrawable
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.Purple500
import com.mrntlu.PassVault.ui.widgets.BottomSheetButtons
import com.mrntlu.PassVault.ui.widgets.ColorPickerRow
import com.mrntlu.PassVault.ui.widgets.PasswordBottomSheetFields
import com.mrntlu.PassVault.ui.widgets.online.ImageSelectionSheet
import com.mrntlu.PassVault.utils.Constants
import com.mrntlu.PassVault.utils.SheetState
import com.mrntlu.PassVault.utils.areFieldsEnabled
import com.mrntlu.PassVault.utils.setGradientBackground
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnlinePasswordScreen(

) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val imageSize = 52.dp

    val bottomSheetVM by remember { mutableStateOf(BottomSheetViewModel()) }

    var titleError by remember { mutableStateOf(false) }
    var titleErrorMessage by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var usernameErrorMessage by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    var selectedImage: String? by remember { mutableStateOf(null) }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { false },
        skipHalfExpanded = true
    )

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            ImageSelectionSheet()
        }
    ) {
        Scaffold(
            modifier = Modifier
                .setGradientBackground(),
            content = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 36.dp)
                        .padding(bottom = 8.dp)
                        .padding(top = 16.dp)
                        .imePadding()
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (selectedImage == null) {
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

                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = "or",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(imageSize)
                                .border(
                                    BorderStroke(3.dp, bottomSheetVM.selectedColor),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(imageSize.minus(10.dp))
                                    .clip(CircleShape),
                                model = ImageRequest.Builder(context)
                                    .data(Constants.ImageEndpoint + selectedImage)
                                    .build(),
                                contentDescription = stringResource(id = R.string.cd_image),
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            modifier = Modifier
                                .padding(horizontal = 3.dp),
                            onClick = {
                                coroutineScope.launch {
                                    if (modalSheetState.isVisible)
                                        modalSheetState.hide()
                                    else
                                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                }
//                                selectedImage = "netflix.com"
                            },
                        ) {
                            Text(
                                text = if (selectedImage == null) "Select Image" else "Change Image",
                                color = Purple500,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }

                        if (selectedImage != null) {
                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp),
                                onClick = {
                                    selectedImage = null
                                }
                            ) {
                                Text(
                                    text = "Remove Image",
                                    color = Purple500,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }

                    ColorPickerRow(
                        bottomSheetVM = bottomSheetVM,
                    )

                    PasswordBottomSheetFields(
                        bottomSheetVM = bottomSheetVM,
                        sheetState = SheetState.AddItem,
                        titleError,
                        titleErrorMessage,
                        usernameError,
                        usernameErrorMessage,
                        passwordError,
                        passwordErrorMessage
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = bottomSheetVM.isEncrypted,
                            onCheckedChange = { bottomSheetVM.isEncrypted = it },
                            enabled = SheetState.AddItem.areFieldsEnabled(),
                            colors = CheckboxDefaults.colors(
                                checkedColor = BlueLogo
                            )
                        )

                        Text(
                            text = stringResource(R.string.encryption_info),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        //                    IconButton(
                        //                        onClick = onInfoDialogClicked,
                        //                    ) {
                        //                        Icon(
                        //                            modifier = Modifier
                        //                                .size(20.dp),
                        //                            imageVector = Icons.Rounded.Info,
                        //                            contentDescription = stringResource(com.mrntlu.PassVault.R.string.cd_what_encryption),
                        //                            tint = Purple500,
                        //                        )
                        //                    }
                    }

                    val textfieldError = stringResource(R.string.textfield_error)
                    val cryptoKey = stringResource(id = R.string.crypto_key)

                    BottomSheetButtons(
                        isConfirmButtonAvailable = true,
                        confirmBGColor = BlueLogo,
                        confirmText = stringResource(id = R.string.save),
                        onConfirmClicked = {
                            focusManager.clearFocus(force = true)

                        },
                        dismissText = stringResource(id = R.string.cancel),
                        onDismissClicked = {
                            focusManager.clearFocus(force = true)
                        }
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun OnlinePasswordScreenPreview() {
    OnlinePasswordScreen()
}