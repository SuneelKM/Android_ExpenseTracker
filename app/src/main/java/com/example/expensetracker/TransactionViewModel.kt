package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import kotlinx.coroutines.launch

class TransactionViewModel(app: Application):AndroidViewModel(app){
    private val repo: TransactionRepository
    val allTransactions : LiveData<List<Transaction>>?
    var totalAmount = 0.0
    var budgetAmount = 0.0
    var expenseAmount = 0.0
//    var currentTransaction:LiveData<Transaction> = LiveData<Transaction>()

    init {
        repo = TransactionRepository(app)
        allTransactions = repo.getAll()
//        currentTransaction = LiveData<Transaction>()
    }

    fun getAllTransactions() = viewModelScope.launch {
        repo.getAll()
    }
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        repo.insertAll(transaction)
    }
    fun deleteTransactions(transactionId: Int) = viewModelScope.launch {
        repo.delete(transactionId)
    }
    fun updateTransactions(transaction: Transaction) = viewModelScope.launch {
        repo.update(transaction)
    }

    fun getById(transaction: Int) : Transaction? {
     return repo.getById(transaction)
    }

    fun sortAsc() : LiveData<List<Transaction>>? {
        return repo.sortAsc()
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>? {
        return repo.searchDatabase(searchQuery)
    }

    fun updateDashboard(transactions: List<Transaction>) {
        totalAmount = transactions.map { it.amount }.sum()
        budgetAmount = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        expenseAmount = totalAmount - budgetAmount
    }

}