package com.example.expensetracker

import android.content.Context
import androidx.lifecycle.LiveData


class TransactionRepository(context: Context) {

    var db:TransactionDao? = AppDatabase.getInstance(context)?.transactionDao()

    fun getAll(): LiveData<List<Transaction>>?{
        return db?.getAll()
    }

    fun insertAll(transaction: Transaction){
        db?.insertAll(transaction)
    }

    fun delete(transaction: Transaction) {
        db?.delete(transaction)
    }


    fun update(transaction: Transaction){
        db?.update(transaction)
    }

}


