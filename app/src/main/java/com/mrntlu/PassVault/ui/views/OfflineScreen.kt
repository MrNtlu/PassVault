package com.mrntlu.PassVault.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.ui.theme.BlueMidnight
import com.mrntlu.PassVault.utils.*
import com.mrntlu.PassVault.viewmodels.offline.OfflineViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OfflineScreen(
    offlineViewModel: OfflineViewModel,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        offlineViewModel.getOfflinePasswords()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .setGradientBackground(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (adCount % 4 == 1) {
                        loadInterstitial(context)
                        showInterstitial(context)
                    }
                    adCount++

                    offlineViewModel.addPassword("Test","Test $adCount", "Test")
                },
                backgroundColor = BlueMidnight,
                contentColor = Color.White,
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = false,
        content = {
            val passwords by offlineViewModel.password

            passwords?.let {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .setGradientBackground()
                ) {
                    items(
                        count = it.size
                    ) { index ->
                        val password = it[index]

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .padding(horizontal = 4.dp),
                            elevation = 4.dp,
                            backgroundColor = Color.White,
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(text = password.idMail)
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun OfflineScreenPreview() {
    OfflineScreen(viewModel())
}