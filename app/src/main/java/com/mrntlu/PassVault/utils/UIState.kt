package com.mrntlu.PassVault.utils

sealed class UIState<out T> {
    object AddItem: UIState<Nothing>()

    data class EditItem<out T>(
        val item: T,
        val position: Int
    ): UIState<T>()

    data class ViewItem<out T>(
        val item: T,
        val position: Int
    ): UIState<T>()
}