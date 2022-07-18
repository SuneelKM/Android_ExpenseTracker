package com.example.expensetracker

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query


class TransactionRepository(context: Context) {

    var db:TransactionDao? = AppDatabase.getInstance(context)?.transactionDao()

    suspend fun getAll(): List<Transaction>?{
        return db?.getAll()
    }

    suspend fun getById(transaction: Int): Transaction?{
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

    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>?{
        return db?.searchDatabase(searchQuery)
    }
    fun sortAsc(): LiveData<List<Transaction>>? {
        return db?.sortAsc()
    }

}


