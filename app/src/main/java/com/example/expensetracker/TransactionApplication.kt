package com.example.expensetracker

import android.app.Application
import com.example.expensetracker.database.AppDatabase

class TransactionApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
