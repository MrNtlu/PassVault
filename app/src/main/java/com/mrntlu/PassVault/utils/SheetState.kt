package com.mrntlu.PassVault.utils

sealed class SheetState<out T> {
    object AddItem: SheetState<Nothing>()

    data class EditItem<out T>(
        val item: T
    ): SheetState<T>()

    data class ViewItem<out T>(
        val item: T
    ): SheetState<T>()
}