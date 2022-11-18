package com.mrntlu.PassVault.services

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mrntlu.PassVault.models.PasswordItem
import com.mrntlu.PassVault.ui.theme.Red500
import com.mrntlu.PassVault.utils.getAsString

@Database(
    entities = [PasswordItem::class],
    version = 3,
    exportSchema = false,
)
abstract class ParseDatabase: RoomDatabase() {
    abstract fun getParseDao(): ParseDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(" Alter Table account Add Column image_uri Text Default null")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("Alter Table account Add Column image_color Text Not Null Default ${Red500.getAsString()}")
    }
}