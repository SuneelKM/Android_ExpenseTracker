package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.*
import com.example.expensetracker.database.Transaction.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(app: Application):AndroidViewModel(app){
    private val repo: TransactionRepository
    lateinit var allTransactions: LiveData<List<Transaction>>
    var totalAmount = 0.0
    var budgetAmount = 0.0
    var expenseAmount = 0.0

    init {
        repo = TransactionRepository(app)
        getAllTransactions()
    }

    private fun getAllTransactions() {
        allTransactions = repo.getAll()!!.asLiveData()
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

    fun getTransactionById(transaction: Int):LiveData<Transaction> {
        return repo.getById(transaction)!!.asLiveData()
    }

    fun sortAsc() : LiveData<List<Transaction>> {
        return repo.sortAsc()!!.asLiveData()
    }


    fun searchDatabase(searchQuery: String): LiveData<List<Transaction>>? {
        return repo.searchDatabase(searchQuery)!!.asLiveData()
    }

    fun updateDashboard(transactions: List<Transaction>) {
        totalAmount = transactions.sumOf { it.amount }
        budgetAmount = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        expenseAmount = totalAmount - budgetAmount
    }

    class TransactionViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}