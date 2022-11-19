package com.mrntlu.PassVault.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ImageListItem(
    @SerializedName("domain")
    val domain: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("name")
    val name: String
)