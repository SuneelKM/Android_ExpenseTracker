package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TransactionViewModel(app: Application):AndroidViewModel(app){
    private val repo: TransactionRepository
    var allTransactions = MutableLiveData<List<Transaction>>()
    var totalAmount = 0.0
    var budgetAmount = 0.0
    var expenseAmount = 0.0
    private var _transactionById =  MutableLiveData<Transaction>()
    var transactionById:LiveData<Transaction> = _transactionById

    init {
        repo = TransactionRepository(app)
        getAllTransactions()
    }

    private fun getAllTransactions() = viewModelScope.launch {
        allTransactions.value = repo.getAll()
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

    fun getTransactionById(transaction: Int) = viewModelScope.launch {
        _transactionById.value = repo.getById(transaction)!!
    }

    fun sortAsc() : LiveData<List<Transaction>>? {
        return repo.sortAsc()
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>? {
        return repo.searchDatabase(searchQuery)
    }

    fun updateDashboard(transactions: List<Transaction>) {
        totalAmount = transactions.sumOf { it.amount }
        budgetAmount = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        expenseAmount = totalAmount - budgetAmount
    }

}