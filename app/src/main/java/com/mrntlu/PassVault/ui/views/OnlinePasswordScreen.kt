package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.widgets.*
import com.mrntlu.PassVault.ui.widgets.online.ImageSelectionSheet
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
    onlineSharedViewModel: OnlinePasswordViewModel,
    imageSelectionViewModel: ImageSelectionViewModel,
    bottomSheetViewModel: BottomSheetViewModel,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val imageSize = 64.dp

    var showInfoDialog by remember { mutableStateOf(false) }
    var showFailureDialog by remember { mutableStateOf(false) }
    var isActionCompleted by remember { mutableStateOf(false) }

    val uiResponse by homeViewModel.uiResponse
    var selectedImage by imageSelectionViewModel.selectedImage
    val uiState = onlineSharedViewModel.state

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { false },
        skipHalfExpanded = true,
    )

    LaunchedEffect(key1 = uiResponse) {
        if (uiResponse is Response.Success) {
            isActionCompleted = true
            homeViewModel.resetUIResponse()
            navController.popBackStack()
        } else if (uiResponse is Response.Failure) {
            showFailureDialog = true
        }
    }

    LaunchedEffect(key1 = onlineSharedViewModel.state) {
        bottomSheetViewModel.setStateValues(onlineSharedViewModel.state)

        selectedImage = if (onlineSharedViewModel.state.getItem() != null)
            onlineSharedViewModel.state.getItem()!!.imageUri
        else
            null
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
                imageSelectionVM = imageSelectionViewModel,
                onCancel = {
                    coroutineScope.launch { modalSheetState.hide() }
                },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 8.dp)
                    .padding(top = 16.dp)
                    .imePadding()
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility (visible = uiState !is UIState.ViewItem) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        PasswordSelectedImageView(
                            selectedImage = selectedImage,
                            imageSize = imageSize,
                            uiState = uiState,
                            bottomSheetVM = bottomSheetViewModel,
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp)
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp),
                                onClick = {
                                    focusManager.clearFocus(force = true)

                                    coroutineScope.launch {
                                        if (modalSheetState.isVisible)
                                            modalSheetState.hide()
                                        else
                                            modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
                                    }
                                },
                            ) {
                                Text(
                                    text = if (selectedImage == null) stringResource(R.string.select_image) else stringResource(R.string.change_image),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }

                            AnimatedVisibility (visible = selectedImage != null) {
                                TextButton(
                                    modifier = Modifier
                                        .padding(horizontal = 3.dp),
                                    onClick = {
                                        selectedImage = null
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.remove_image),
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }

                        ColorPickerRow(
                            bottomSheetVM = bottomSheetViewModel,
                        )
                    }
                }

                PasswordBottomSheetFields(
                    bottomSheetVM = bottomSheetViewModel,
                    uiState = uiState,
                )

                Row(
                    modifier = Modifier
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = bottomSheetViewModel.isEncrypted,
                        onCheckedChange = { bottomSheetViewModel.isEncrypted = it },
                        enabled = uiState.areFieldsEnabled(),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline,
                            disabledColor = MaterialTheme.colorScheme.primary,
                        )
                    )

                    Text(
                        text = stringResource(R.string.encryption_info),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    IconButton(
                        onClick = { showInfoDialog = true },
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp),
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(R.string.cd_what_encryption),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                val textfieldError = stringResource(R.string.textfield_error)

                BottomSheetButtons(
                    isConfirmButtonAvailable = context.isNetworkConnectionAvailable(),
                    confirmBGColor = when (onlineSharedViewModel.state) {
                        is UIState.AddItem -> MaterialTheme.colorScheme.primary
                        is UIState.EditItem -> MaterialTheme.colorScheme.primary
                        is UIState.ViewItem -> MaterialTheme.colorScheme.primaryContainer
                    },
                    confirmText = when(onlineSharedViewModel.state) {
                        is UIState.AddItem -> stringResource(id = R.string.save)
                        is UIState.EditItem -> stringResource(id = R.string.update)
                        is UIState.ViewItem -> stringResource(id = R.string.edit)
                    },
                    confirmTextColor = when (onlineSharedViewModel.state) {
                        is UIState.AddItem -> MaterialTheme.colorScheme.onPrimary
                        is UIState.EditItem -> MaterialTheme.colorScheme.onPrimary
                        is UIState.ViewItem -> MaterialTheme.colorScheme.onPrimaryContainer
                    },
                    onConfirmClicked = {
                        focusManager.clearFocus(force = true)

                        if (onlineSharedViewModel.state is UIState.ViewItem) {
                            onlineSharedViewModel.changeState(
                                UIState.EditItem(
                                    onlineSharedViewModel.state.getItem()!!, onlineSharedViewModel.state.getPosition()!!
                                )
                            )
                        } else {
                            bottomSheetViewModel.titleState.apply {
                                val isTitleEmpty = isEmpty() || isBlank()
                                bottomSheetViewModel.titleError = isTitleEmpty

                                if (isTitleEmpty) {
                                    bottomSheetViewModel.titleErrorMessage = textfieldError
                                }
                            }

                            bottomSheetViewModel.usernameState.apply {
                                val isUsernameEmpty = isEmpty() || isBlank()
                                bottomSheetViewModel.usernameError = isUsernameEmpty

                                if (isUsernameEmpty) {
                                    bottomSheetViewModel.usernameErrorMessage = textfieldError
                                }
                            }

                            bottomSheetViewModel.passwordState.apply {
                                val isPasswordEmpty = isEmpty() || isBlank()
                                bottomSheetViewModel.passwordError = isPasswordEmpty

                                if (isPasswordEmpty) {
                                    bottomSheetViewModel.passwordErrorMessage = textfieldError
                                }
                            }

                            if (!(bottomSheetViewModel.titleError || bottomSheetViewModel.usernameError || bottomSheetViewModel.passwordError)) {
                                when(uiState) {
                                    is UIState.AddItem -> {
                                        val encryptedPassword: String? = if (bottomSheetViewModel.isEncrypted) {
                                            Cryptography().encrypt(bottomSheetViewModel.passwordState)
                                        } else null

                                        homeViewModel.addPassword(
                                            bottomSheetViewModel.titleState,
                                            bottomSheetViewModel.usernameState,
                                            encryptedPassword ?: bottomSheetViewModel.passwordState,
                                            bottomSheetViewModel.noteState,
                                            bottomSheetViewModel.isEncrypted,
                                            selectedImage,
                                            bottomSheetViewModel.selectedColor.getAsString(),
                                        )
                                    }
                                    is UIState.EditItem -> {
                                        val encryptedPassword: String? = if (bottomSheetViewModel.isEncrypted) {
                                            Cryptography().encrypt(bottomSheetViewModel.passwordState)
                                        } else null

                                        homeViewModel.editPassword(
                                            uiState.position,
                                            bottomSheetViewModel.titleState,
                                            bottomSheetViewModel.usernameState,
                                            encryptedPassword ?: bottomSheetViewModel.passwordState,
                                            bottomSheetViewModel.noteState,
                                            bottomSheetViewModel.isEncrypted,
                                            selectedImage,
                                            bottomSheetViewModel.selectedColor.getAsString(),
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }
                    },
                    dismissText = when(onlineSharedViewModel.state) {
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
        }

        if (showInfoDialog) {
            CustomDialog(
                isConfirmButtonVisible = false,
                title = stringResource(R.string.cd_what_encryption),
                text = {
                    Text(
                        text = stringResource(id = R.string.encryption_explanation),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                onConfirmClicked = {},
                dismissContainerColor = MaterialTheme.colorScheme.background,
                dismissTextColor = MaterialTheme.colorScheme.onBackground,
                onDismissClicked = { showInfoDialog = false },
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

        if (uiResponse is Response.Loading || uiResponse is Response.Success || isActionCompleted) {
            LoadingView()
        }
    }
}

@Preview
@Composable
fun OnlinePasswordScreenPreview() {
    OnlinePasswordScreen(rememberNavController(), viewModel(), viewModel(), viewModel(), viewModel())
}