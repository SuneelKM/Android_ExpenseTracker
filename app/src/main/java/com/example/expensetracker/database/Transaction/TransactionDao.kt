package com.example.expensetracker.database.Transaction

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * from transactions WHERE label LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<Transaction>>

    @Query("SELECT * from transactions ORDER BY date ASC")
    fun sortAsc(): Flow<List<Transaction>>

    @Query("SELECT * from transactions WHERE id = :transactionId")
    fun getById(transactionId: Int): Flow<Transaction>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(transaction: Transaction)

    @Query("DELETE from transactions WHERE id = :transactionId")
    suspend fun delete(transactionId: Int)

    @Update
    suspend fun update(transaction: Transaction)

}