package com.jisulim.snackcart.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jisulim.snackcart.data.DATABASE
import com.jisulim.snackcart.models.entity.WishEntity

@Database(
    entities = [WishEntity::class],
    version = DATABASE.WISH_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wishDao(): WishDao

}