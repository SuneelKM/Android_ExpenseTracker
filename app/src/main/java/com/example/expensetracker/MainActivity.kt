package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.data.SettingsDataStore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var settingsDataStore: SettingsDataStore
    private var isAsc:Boolean = false

    private val vm: TransactionViewModel by viewModels{
        TransactionViewModel.TransactionViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingsDataStore = SettingsDataStore(this)
        settingsDataStore.preferenceFlow.asLiveData().observe(this) { boolean ->
            isAsc = boolean
            vm.getAllTransactions(isAsc).observe(this) {
                vm.updateDashboard(it)
                balance.text = vm.formattedAmount(vm.totalAmount)
                budget.text = vm.formattedAmount(vm.budgetAmount)
                expense.text = vm.formattedAmount(vm.expenseAmount)
                transactionAdapter.submitList(it)
            }
        }

        transactionAdapter = TransactionAdapter {
            val intent = Intent(this, DetailedActivity::class.java)
            intent.putExtra("transactionId", it.id)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        sortButton.setOnClickListener {
            lifecycleScope.launch {
                settingsDataStore.saveLayoutToPreferencesStore(!isAsc, this@MainActivity)
            }
        }
    }

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

        vm.searchDatabase(searchQuery, isAsc).observe(this) { list ->
            list.let {
                transactionAdapter.submitList(it)
                vm.updateDashboard(it)
                balance.text = vm.formattedAmount(vm.totalAmount)
                budget.text = vm.formattedAmount(vm.budgetAmount)
                expense.text = vm.formattedAmount(vm.expenseAmount)

            }
        }
    }

}




