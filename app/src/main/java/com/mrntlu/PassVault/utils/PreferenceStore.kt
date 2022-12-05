package com.mrntlu.PassVault.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

        val THEME_KEY = booleanPreferencesKey("theme")
        val ADS_DIALOG_KEY = booleanPreferencesKey("ads_dialog")
    }

    fun getTheme(isSystemDarkTheme: Boolean): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: isSystemDarkTheme
        }

    suspend fun saveTheme(isDarkThemeEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkThemeEnabled
        }
    }

    fun getAdsDialog(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ADS_DIALOG_KEY] ?: false
        }

    suspend fun saveAdsDialog() {
        context.dataStore.edit { preferences ->
            preferences[ADS_DIALOG_KEY] = true
        }
    }
}