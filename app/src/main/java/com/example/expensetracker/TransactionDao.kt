package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    fun getAll(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(transaction: Transaction)

    @Query("DELETE from transactions WHERE id = :transactionId")
    fun delete(transactionId: Int)

    @Update
    fun update(transaction: Transaction)


}