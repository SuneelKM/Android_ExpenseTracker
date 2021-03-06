package com.example.expensetracker

import android.content.Context
import androidx.room.*


@Database(entities = [Transaction::class], version = 3, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "transactions")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}