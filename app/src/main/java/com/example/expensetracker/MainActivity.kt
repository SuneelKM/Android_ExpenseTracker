package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.Transaction.Transaction
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var transactionAdapter: TransactionAdapter

    private val vm: TransactionViewModel by viewModels{
        TransactionViewModel.TransactionViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactionAdapter = TransactionAdapter {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("transactionId", it.id)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter

        vm.allTransactions.observe(this) {
            vm.updateDashboard(it)
            balance.text = formattedAmount(vm.totalAmount)
            budget.text = formattedAmount(vm.budgetAmount)
            expense.text = formattedAmount(vm.expenseAmount)
            transactionAdapter.submitList(it)
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        var sort = "desc"
        sortButton.setOnClickListener{
            sort = if(sort == "desc") {
                vm.sortAsc().observe(this) {
                    transactionAdapter.submitList(it)
                }
                "asc"
            } else {
                vm.allTransactions.observe(this) {
                    transactionAdapter.submitList(it)
                }
                "desc"
            }

        }
    }

    fun formattedAmount(amount:Double): String =
        NumberFormat.getCurrencyInstance().format(amount)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        vm.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                transactionAdapter.submitList(it)
                vm.updateDashboard(it)
                balance.text = formattedAmount(vm.totalAmount)
                budget.text = formattedAmount(vm.budgetAmount)
                expense.text = formattedAmount(vm.expenseAmount)

            }
        }
    }

}




