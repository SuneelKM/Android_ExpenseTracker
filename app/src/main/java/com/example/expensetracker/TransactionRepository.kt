package com.example.expensetracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.expensetracker.database.AppDatabase
import com.example.expensetracker.database.Transaction.Transaction
import com.example.expensetracker.database.Transaction.TransactionDao
import kotlinx.coroutines.flow.Flow


class TransactionRepository(context: Context) {

    var db: TransactionDao? = AppDatabase.getDatabase(context).transactionDao()

    fun getAll(): Flow<List<Transaction>>?{
        return db?.getAll()
    }

    fun getById(transaction: Int): Flow<Transaction>?{
        return db?.getById(transaction)
    }
    suspend fun insertAll(transaction: Transaction){
        db?.insertAll(transaction)
    }

    suspend fun delete(transaction: Int) {
        db?.delete(transaction)
    }

    suspend fun update(transaction: Transaction){
        db?.update(transaction)
    }

    fun searchDatabase(searchQuery: String): Flow<List<Transaction>>?{
        return db?.searchDatabase(searchQuery)
    }
    fun sortAsc(): Flow<List<Transaction>>? {
        return db?.sortAsc()
    }

}


