package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions ORDER BY date DESC")
    fun getAll(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(transaction: Transaction)

    @Query("DELETE from transactions WHERE id = :transactionId")
    fun delete(transactionId: Int)

    @Query("SELECT * from transactions WHERE id = :transactionId")
    fun getById(transactionId: Int): Transaction

    @Update
    fun update(transaction: Transaction)

    @Query("SELECT * from transactions WHERE label LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>


}