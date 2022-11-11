package com.mrntlu.PassVault.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.parse.ParseObject

fun String.isValidEmail(): Boolean {
    return trim().isNotBlank() && trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

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

fun Context.isNetworkConnectionAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}

fun Context.sendMail(to: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No mail app found", Toast.LENGTH_SHORT).show()
    } catch (t: Throwable) {
        Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
    }
}

fun ParseObject.toPasswordItem() = PasswordItem(
    getString("Username") ?: "",
    getString("Title") ?: "",
    getString("Note"),
    getString("Password") ?: "",
    getBoolean("IsEncrypted"),
    parseID = objectId
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