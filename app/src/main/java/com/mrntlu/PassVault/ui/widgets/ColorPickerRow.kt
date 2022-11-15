package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mrntlu.PassVault.ui.theme.*
import com.mrntlu.PassVault.viewmodels.BottomSheetViewModel

@Composable
fun ColorPickerRow(
    bottomSheetVM: BottomSheetViewModel
) {
    val colorPickerList = listOf(
        Red500, Red700, Purple500, Purple700,
        Pink500, Pink700, Blue500, Blue700,
        Green500, Green700, Yellow500, Yellow700,
        Orange500, Orange700, Brown500, Brown700,
        Grey500, Grey700, Color.Black
    )

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .padding(bottom = 6.dp)
            .imePadding()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            items(
                count = colorPickerList.size,
                key = { index ->
                    colorPickerList[index].toString()
                },
            ) { index ->
                val color = colorPickerList[index]

                val modifier = if (bottomSheetVM.selectedColor == color) {
                    Modifier
                        .size(29.dp)
                        .border(BorderStroke(2.dp, Yellow600), shape = CircleShape)
                } else {
                    Modifier
                        .size(29.dp)
                        .clickable {
                            bottomSheetVM.selectedColor = color
                        }
                }

                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(21.dp),
                        onDraw = {
                            drawCircle(color = color)
                        }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun ColorPickerRowPreview() {
    ColorPickerRow(viewModel())
}