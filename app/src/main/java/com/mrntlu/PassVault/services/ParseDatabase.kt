package com.mrntlu.PassVault.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrntlu.PassVault.models.PasswordItem

@Database(entities = [PasswordItem::class], version = 1, exportSchema = false)
abstract class ParseDatabase: RoomDatabase() {
    abstract fun getParseDao(): ParseDao
}