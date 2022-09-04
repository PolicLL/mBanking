package com.example.mbanking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mbanking.database.converter.DateConverter
import com.example.mbanking.database.dao.UserDao
import com.example.mbanking.database.model.Account
import com.example.mbanking.database.model.Transaction
import com.example.mbanking.database.model.User

@Database(
    entities = [
        User::class,
        Account::class,
        Transaction::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract val schoolDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {

            synchronized(this) {

                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "mBanking_db"
                ).build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
