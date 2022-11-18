package com.mrntlu.PassVault.utils

import androidx.compose.ui.graphics.Color
import com.mrntlu.PassVault.ui.theme.*

object Constants {
    const val RealmName = "myrealm.realm"
    const val CacheDatabaseName = "parse_cache_database"

    const val ImageEndpoint = "https://logo.clearbit.com/"
    const val BaseApiEndPoint = "https://autocomplete.clearbit.com/v1/"
    const val ApiEndPoint = "companies/suggest"

    val ColorPickerList = listOf(
        Color.Black, Grey400, Grey500, Grey700,
        Red500, Red700, Purple500, Purple700,
        Pink500, Pink700, Blue500, Blue700,
        Green500, Green700, Yellow500, Yellow700,
        Orange500, Orange700, Brown500, Brown700,
    )
}