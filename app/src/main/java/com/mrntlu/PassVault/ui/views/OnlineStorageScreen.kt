package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.utils.Response
import com.mrntlu.PassVault.viewmodels.HomeViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OnlineStorageScreen(
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BlueLogo,
                        BlueDark,
                        BlueDarkest
                    )
                )
            ),
    ) {
        val passwords = (homeViewModel.passwords.value as Response.Success).data
        passwords?.let {
            LazyColumn {
                items(
                    count = it.size
                ) { index ->
                    Text(text = it[index].getString("Title") ?: "")
                }
            }
        }
    }

}

@Preview
@Composable
fun OnlineStorageScreenPreview() {
    OnlineStorageScreen(viewModel())
}