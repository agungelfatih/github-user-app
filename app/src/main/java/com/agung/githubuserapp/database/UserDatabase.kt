package com.agung.githubuserapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteUser::class],
    version = 2
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun favoritUserDao() : FavoriteUserDao

    companion object {
        @Volatile
        var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDatabase::class.java, "user_database")
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }

    }
}