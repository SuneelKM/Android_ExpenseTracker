package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf()
        println("HELLO I AM 1")

        db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()

//        db = Room.databaseBuilder(this,
//        AppDatabase::class.java,
//        "transactions").build()
        println("HELLO I AM 2")

        transactionAdapter = TransactionAdapter(transactions)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = transactionAdapter



        addBtn.setOnClickListener{
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchAll(){
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }
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

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}