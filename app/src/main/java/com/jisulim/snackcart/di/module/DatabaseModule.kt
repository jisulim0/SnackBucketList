package com.jisulim.snackcart.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import com.jisulim.snackcart.data.DATABASE
import com.jisulim.snackcart.data.repository.WishRepository
import com.jisulim.snackcart.data.room.AppDatabase
import com.jisulim.snackcart.data.room.WishDao
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 싱글턴이라 context 주입 없이 바로 사용 가능.

    // hilt로 room database 만듬
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context, AppDatabase::class.java, DATABASE.WISH_NAME
        ).addMigrations(
            MIGRATION_1_2
        ).build()

    // hilt로 dao 클래스 만듬
    @Singleton
    @Provides
    fun provideWishDAO(appDatabase: AppDatabase): WishDao = appDatabase.wishDao()

    @Singleton
    @Provides
    fun provideRepository(wishDao: WishDao): WishRepository = WishRepository(wishDao)

}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE 'wish' ADD COLUMN 'site_alias' TEXT NOT NULL DEFAULT 'emart'"
        )
    }

}