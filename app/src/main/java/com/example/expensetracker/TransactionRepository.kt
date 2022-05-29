package com.example.expensetracker

import android.content.Context
import androidx.lifecycle.LiveData


class TransactionRepository(context: Context) {

    var db:TransactionDao? = AppDatabase.getInstance(context)?.transactionDao()

    fun getAll(): LiveData<List<Transaction>>?{
        return db?.getAll()
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


}


