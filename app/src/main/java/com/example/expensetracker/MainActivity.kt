package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var vm: TransactionViewModel
    var transactions = ArrayList<Transaction>()
    lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = TransactionViewModel(application)

        transactionAdapter = TransactionAdapter(transactions)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = transactionAdapter

        vm.allTransactions?.observe(this) {
            vm.updateDashboard(it)
            balance.text = "$ %.2f".format(vm.totalAmount)
            budget.text = "$ %.2f".format(vm.budgetAmount)
            expense.text = "$ %.2f".format(vm.expenseAmount)
            transactionAdapter.getTransactions(it)
        }


        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }


}