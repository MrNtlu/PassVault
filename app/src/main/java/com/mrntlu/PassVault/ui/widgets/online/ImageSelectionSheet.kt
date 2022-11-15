package com.mrntlu.PassVault.ui.widgets.online

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mrntlu.PassVault.utils.printLog
import com.mrntlu.PassVault.viewmodels.online.ImageListViewModel

@Composable
fun ImageSelectionSheet(

) {
    val imageListViewModel = hiltViewModel<ImageListViewModel>()
    val imageList by imageListViewModel.imageList

    LaunchedEffect(key1 = true) {
        imageListViewModel.getImageList("netflix")
    }

    LaunchedEffect(key1 = imageList) {
        printLog("Value $imageList")
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {

        //TODO: Search textfield, search button, lazycolumn list
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 48.dp)
                .padding(bottom = 8.dp)
                .padding(top = 16.dp)
                .imePadding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}

@Preview
@Composable
fun ImageSelectionSheetPreview() {
    ImageSelectionSheet()
}