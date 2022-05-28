package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.alterpat.budgettracker.TransactionAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf(
            Transaction("Weekend Budget", 400.00),
            Transaction("Bananas", -4.00),
            Transaction("Car", -40.90),
            Transaction("Lunch", -25.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget,Weekend Budget", 1000000000.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00),
            Transaction("Weekend Budget", 400.00)
        )
        transactionAdapter = TransactionAdapter(transactions)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = transactionAdapter
        updateDashboard()

        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun updateDashboard(){
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter { it.amount>0 }.map{it.amount}.sum()
        val expenseAmount = totalAmount - budgetAmount

        balance.text = "$ %.2f".format(totalAmount)
        budget.text = "$ %.2f".format(budgetAmount)
        expense.text = "$ %.2f".format(expenseAmount)
    }
}