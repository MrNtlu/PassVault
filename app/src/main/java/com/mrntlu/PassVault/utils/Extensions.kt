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
import androidx.compose.ui.graphics.Color
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.BlueDark
import com.mrntlu.PassVault.ui.theme.BlueDarkest
import com.mrntlu.PassVault.ui.theme.BlueLogo
import com.mrntlu.PassVault.utils.Constants.ColorPickerList
import com.parse.ParseObject

fun String.isValidEmail(): Boolean {
    return trim().isNotBlank() && trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Color.getAsString() = value.toString()

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
    imageUri = getString("ImageUri"),
    imageColor = getString("ImageColor") ?: ColorPickerList[(ColorPickerList.indices).random()].getAsString(),
    parseID = objectId
)

fun <T> UIState<T>.getItem(): T? = when(this) {
    is UIState.EditItem -> item
    is UIState.ViewItem -> item
    is UIState.AddItem -> null
}

fun <T> UIState<T>.getPosition(): Int? = when(this) {
    is UIState.EditItem -> position
    is UIState.ViewItem -> position
    is UIState.AddItem -> null
}

fun <T> UIState<T>.areFieldsEnabled(): Boolean = when(this) {
    is UIState.EditItem -> true
    is UIState.ViewItem -> false
    is UIState.AddItem -> true
}