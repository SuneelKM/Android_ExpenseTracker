package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions ORDER BY date DESC")
    suspend fun getAll(): List<Transaction>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(transaction: Transaction)

    @Query("DELETE from transactions WHERE id = :transactionId")
    suspend fun delete(transactionId: Int)

    @Query("SELECT * from transactions WHERE id = :transactionId")
    suspend fun getById(transactionId: Int): Transaction

    @Update
    suspend fun update(transaction: Transaction)

    @Query("SELECT * from transactions WHERE label LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>

    @Query("SELECT * from transactions ORDER BY date ASC")
    fun sortAsc(): LiveData<List<Transaction>>


}