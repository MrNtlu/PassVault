package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.ui.theme.Purple500
import com.mrntlu.PassVault.ui.theme.Yellow700
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.ui.widgets.online.ImageSelectionSheet
import com.mrntlu.PassVault.ui.widgets.online.OnlinePasswordAppBar
import com.mrntlu.PassVault.ui.widgets.online.PasswordSelectedImageView
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.online.BottomSheetViewModel
import com.mrntlu.PassVault.viewmodels.online.HomeViewModel
import com.mrntlu.PassVault.viewmodels.shared.ImageSelectionViewModel
import com.mrntlu.PassVault.viewmodels.shared.OnlinePasswordViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OnlinePasswordScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    sharedViewModel: OnlinePasswordViewModel,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val imageSize = 64.dp
    val topBarImageSize = 48.dp

    val imageSelectionVM = hiltViewModel<ImageSelectionViewModel>()

    val bottomSheetVM by remember { mutableStateOf(BottomSheetViewModel()) }

    var showInfoDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }

    val uiResponse by homeViewModel.uiResponse
    var selectedImage by imageSelectionVM.selectedImage
    val uiState = sharedViewModel.state

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { false },
        skipHalfExpanded = true
    )

    LaunchedEffect(key1 = uiResponse) {
        if (uiResponse is Response.Failure) {
            showFailureDialog = true
        } else if (uiResponse is Response.Success) {
            navController.popBackStack()
            homeViewModel.resetUIResponse()
        }
    }

    LaunchedEffect(key1 = sharedViewModel.state) {
        bottomSheetVM.setStateValues(sharedViewModel.state)

        if (sharedViewModel.state.getItem() != null) {
            selectedImage = sharedViewModel.state.getItem()!!.imageUri
        }
    }

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            ImageSelectionSheet(
                isSheetVisible = modalSheetState.isVisible,
                imageSelectionVM = imageSelectionVM,
                onCancel = {
                    coroutineScope.launch { modalSheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {

                OnlinePasswordAppBar(
                    selectedImage = selectedImage,
                    topBarImageSize = topBarImageSize,
                    uiState = uiState,
                    uiResponse = uiResponse,
                    bottomSheetVM = bottomSheetVM,
                    onNavigationClicked = {
                        homeViewModel.resetUIResponse()
                        navController.popBackStack()
                    }
                )
            },
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
                    if (uiState !is UIState.ViewItem){
                        PasswordSelectedImageView(
                            selectedImage = selectedImage,
                            imageSize = imageSize,
                            uiState = uiState,
                            bottomSheetVM = bottomSheetVM,
                        )

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
                    }

                    PasswordBottomSheetFields(
                        bottomSheetVM = bottomSheetVM,
                        uiState = uiState,
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
                            enabled = uiState.areFieldsEnabled(),
                            colors = CheckboxDefaults.colors(
                                checkedColor = BlueLogo
                            )
                        )

                        Text(
                            text = stringResource(R.string.encryption_info),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        IconButton(
                            onClick = { showInfoDialog = true },
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(20.dp),
                                imageVector = Icons.Rounded.Info,
                                contentDescription = stringResource(R.string.cd_what_encryption),
                                tint = Purple500,
                            )
                        }
                    }

                    val textfieldError = stringResource(R.string.textfield_error)
                    val cryptoKey = stringResource(id = R.string.crypto_key)

                    BottomSheetButtons(
                        isConfirmButtonAvailable = context.isNetworkConnectionAvailable(),
                        confirmBGColor = when (sharedViewModel.state) {
                            is UIState.AddItem -> BlueLogo
                            is UIState.EditItem -> BlueLogo
                            is UIState.ViewItem -> Yellow700
                        },
                        confirmText = when(sharedViewModel.state) {
                            is UIState.AddItem -> stringResource(id = R.string.save)
                            is UIState.EditItem -> stringResource(id = R.string.update)
                            is UIState.ViewItem -> stringResource(id = R.string.edit)
                        },
                        onConfirmClicked = {
                            focusManager.clearFocus(force = true)

                            if (sharedViewModel.state is UIState.ViewItem) {
                                sharedViewModel.changeState(
                                    UIState.EditItem(
                                        sharedViewModel.state.getItem()!!, sharedViewModel.state.getPosition()!!
                                    )
                                )
                            } else {
                                bottomSheetVM.titleState.apply {
                                    val isTitleEmpty = isEmpty() || isBlank()
                                    bottomSheetVM.titleError = isTitleEmpty

                                    if (isTitleEmpty) {
                                        bottomSheetVM.titleErrorMessage = textfieldError
                                    }
                                }

                                bottomSheetVM.usernameState.apply {
                                    val isUsernameEmpty = isEmpty() || isBlank()
                                    bottomSheetVM.usernameError = isUsernameEmpty

                                    if (isUsernameEmpty) {
                                        bottomSheetVM.usernameErrorMessage = textfieldError
                                    }
                                }

                                bottomSheetVM.passwordState.apply {
                                    val isPasswordEmpty = isEmpty() || isBlank()
                                    bottomSheetVM.passwordError = isPasswordEmpty

                                    if (isPasswordEmpty) {
                                        bottomSheetVM.passwordErrorMessage = textfieldError
                                    }
                                }

                                if (!(bottomSheetVM.titleError || bottomSheetVM.usernameError || bottomSheetVM.passwordError)) {
                                    when(uiState) {
                                        is UIState.AddItem -> {
                                            val encryptedPassword: String? = if (bottomSheetVM.isEncrypted) {
                                                Cryptography(cryptoKey).encrypt(bottomSheetVM.passwordState)
                                            } else null

                                            homeViewModel.addPassword(
                                                bottomSheetVM.titleState,
                                                bottomSheetVM.usernameState,
                                                encryptedPassword ?: bottomSheetVM.passwordState,
                                                bottomSheetVM.noteState,
                                                bottomSheetVM.isEncrypted,
                                                selectedImage,
                                                bottomSheetVM.selectedColor.getAsString(),
                                            )
                                        }
                                        is UIState.EditItem -> {
                                            val encryptedPassword: String? = if (bottomSheetVM.isEncrypted) {
                                                Cryptography(cryptoKey).encrypt(bottomSheetVM.passwordState)
                                            } else null

                                            homeViewModel.editPassword(
                                                uiState.position,
                                                bottomSheetVM.titleState,
                                                bottomSheetVM.usernameState,
                                                encryptedPassword ?: bottomSheetVM.passwordState,
                                                bottomSheetVM.noteState,
                                                bottomSheetVM.isEncrypted,
                                                selectedImage,
                                                bottomSheetVM.selectedColor.getAsString(),
                                            )
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        },
                        dismissText = when(sharedViewModel.state) {
                            is UIState.AddItem -> stringResource(id = R.string.cancel)
                            is UIState.EditItem -> stringResource(id = R.string.cancel)
                            is UIState.ViewItem -> stringResource(id = R.string.close)
                        },
                        onDismissClicked = {
                            focusManager.clearFocus(force = true)
                            navController.popBackStack()
                        }
                    )
                }

                if (showInfoDialog) {
                    AlertDialog(
                        onDismissRequest = { showInfoDialog = false },
                        title = {
                            Text(
                                text = stringResource(R.string.cd_what_encryption),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(id = R.string.encryption_explanation),
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        },
                        confirmButton = {},
                        dismissButton = {
                            Button(
                                onClick = { showInfoDialog = false },
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    )
                }

                if (showFailureDialog && uiResponse is Response.Failure) {
                    val error = (uiResponse as Response.Failure).errorMessage

                    ErrorDialog(
                        error = error,
                        onDismissClicked = {
                            showFailureDialog = false
                        }
                    )
                }

                if (uiResponse is Response.Loading) {
                    LoadingView()
                }
            }
        )
    }
}

@Preview
@Composable
fun OnlinePasswordScreenPreview() {
    OnlinePasswordScreen(rememberNavController(), viewModel(), viewModel())
}