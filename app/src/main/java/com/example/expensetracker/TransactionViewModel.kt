package com.example.expensetracker

import android.app.Application
import androidx.lifecycle.*
import com.example.expensetracker.database.AppDatabase.Companion.getDatabase
import com.example.expensetracker.database.Transaction.Transaction
import kotlinx.coroutines.launch
import java.text.NumberFormat

class TransactionViewModel(app: Application):AndroidViewModel(app){

    private val repo = TransactionRepository(getDatabase(app))

    var totalAmount = 0.0
    var budgetAmount = 0.0
    var expenseAmount = 0.0

    fun getAllTransactions(isAsc:Boolean) = repo.getAll(isAsc).asLiveData()

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
        return repo.getById(transaction).asLiveData()
    }

    fun searchDatabase(searchQuery: String, isAsc:Boolean): LiveData<List<Transaction>> {
        return repo.searchDatabase(searchQuery, isAsc).asLiveData()
    }

    fun updateDashboard(transactions: List<Transaction>) {
        totalAmount = transactions.sumOf { it.amount }
        budgetAmount = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        expenseAmount = totalAmount - budgetAmount
    }

    fun formattedAmount(amount:Double): String =
        NumberFormat.getCurrencyInstance().format(amount)

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