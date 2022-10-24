package com.mrntlu.PassVault.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.parse.ParseObject

fun Modifier.setGradientBackground(): Modifier = background(
    brush = Brush.verticalGradient(
        colors = listOf(
            BlueLogo,
            BlueDark,
            BlueDarkest
        )
    )
)

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun ParseObject.toPasswordItem() = PasswordItem(
    getString("Username") ?: "",
    getString("Title") ?: "",
    getString("Note"),
    getString("Password") ?: "",
    getBoolean("IsEncrypted")
)

fun <T> SheetState<T>.getItem(): T? = when(this) {
    is SheetState.EditItem -> item
    is SheetState.ViewItem -> item
    is SheetState.AddItem -> null
}

fun <T> SheetState<T>.getPosition(): Int? = when(this) {
    is SheetState.EditItem -> position
    is SheetState.ViewItem -> position
    is SheetState.AddItem -> null
}

fun <T> SheetState<T>.areFieldsEnabled(): Boolean = when(this) {
    is SheetState.EditItem -> true
    is SheetState.ViewItem -> false
    is SheetState.AddItem -> true
}