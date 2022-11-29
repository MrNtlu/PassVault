package com.mrntlu.PassVault.ui.widgets.online

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mrntlu.PassVault.R
import com.mrntlu.PassVault.ui.widgets.LoadingView
import com.mrntlu.PassVault.ui.widgets.NoItemView
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.utils.setTextfieldTheme
import com.mrntlu.PassVault.viewmodels.online.ImageListViewModel
import com.mrntlu.PassVault.viewmodels.shared.ImageSelectionViewModel

@Composable
fun ImageSelectionSheet(
    isSheetVisible: Boolean,
    imageSelectionVM: ImageSelectionViewModel,
    onCancel: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val imageListViewModel = hiltViewModel<ImageListViewModel>()
    val imageList by imageListViewModel.imageList

    val searchState = remember { mutableStateOf(TextFieldValue()) }

    fun onSearchClicked() {
        focusManager.clearFocus(force = true)

        imageListViewModel.getImageList(searchState.value.text)
    }

    LaunchedEffect(key1 = isSheetVisible) {
        if (!isSheetVisible) {
            focusManager.clearFocus(force = true)

            searchState.value = TextFieldValue()
        }
    }

    Box(
        modifier = Modifier
            .height((screenHeight * 3) / 5)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
                .imePadding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f),
                    value = searchState.value,
                    onValueChange = { searchState.value = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        keyboardType = KeyboardType.Uri,
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        onSearchClicked()
                    }),
                    label = {
                        Text(text = stringResource(R.string.search_service))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(id = R.string.cd_search)
                        )
                    },
                    colors = TextFieldDefaults.setTextfieldTheme(),
                )

                TextButton(
                    modifier = Modifier
                        .padding(start = 12.dp),
                    onClick = { onSearchClicked() },
                ) {
                    Text(
                        text = stringResource(id = R.string.search),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            if (imageList is Response.Success) {
                (imageList as Response.Success).data?.let { data ->
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 8.dp),
                    ) {
                        items(
                            count = if (data.isNotEmpty()) data.size else 1,
                            key = { index ->
                                if (data.isNotEmpty())
                                    data[index].domain
                                else
                                    ""
                            }
                        ) { index ->
                            if (data.isNotEmpty()) {
                                val image = data[index]

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                        .padding(vertical = 3.dp)
                                        .background(MaterialTheme.colorScheme.background)
                                        .clickable {
                                            imageSelectionVM.setImage(image.logo)

                                            onCancel()
                                        },
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape),
                                            model = ImageRequest.Builder(context)
                                                .data(image.logo)
                                                .build(),
                                            contentDescription = stringResource(id = R.string.cd_image),
                                        )

                                        Text(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 12.dp),
                                            text = image.name,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                    }

                                    Divider(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                    )
                                }
                            } else {
                                NoItemView(modifier = Modifier.fillParentMaxSize())
                            }
                        }
                    }
                }
            } else {
                NoItemView(modifier = Modifier.weight(1f))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        if (imageList is Response.Loading) {
            LoadingView()
        }
    }
}

@Preview
@Composable
fun ImageSelectionSheetPreview() {
    ImageSelectionSheet(true, viewModel(), {})
}