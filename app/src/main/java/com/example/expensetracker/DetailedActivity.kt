package com.example.expensetracker


import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_detailed.*

import java.util.*
import kotlin.math.abs

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction
    lateinit var vm: TransactionViewModel
    lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        vm = TransactionViewModel(application)

        val label = intent.getStringExtra("label")
        var amount = intent.getDoubleExtra("amount", 0.0)
        val description = intent.getStringExtra("description")
        val transactionId = intent.getIntExtra("transactionId", -1)
        val date = intent.getStringExtra("date")

        if(amount>0) incomeEdit.isChecked = true

        labelInputEdit.setText(label)
        amountInputEdit.setText(abs(amount).toString())
        descriptionInputEdit.setText(description)
        calendarDateEdit.setText(date)

        val labelExpense = resources.getStringArray(R.array.labelExpense)
        val labelIncome = resources.getStringArray(R.array.labelIncome)
        var labels = labelExpense
        if(incomeEdit.isChecked) labels = labelIncome
        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, labels)
        labelInputEdit.setAdapter(arrayAdapter)

        expenseEdit.setOnClickListener {
            if (labelInputEdit.text.toString() !in labelExpense.toList()) {
                labelInputEdit.setText("")
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, labelExpense)
                labelInputEdit.setAdapter(arrayAdapter)
            }
        }

        incomeEdit.setOnClickListener {
            if (labelInputEdit.text.toString() !in labelIncome.toList()) {
                labelInputEdit.setText("")
                arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, labelIncome)
                labelInputEdit.setAdapter(arrayAdapter)
            }
        }


        detailRootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "EEEE, dd MMM yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            calendarDateEdit.setText(sdf.format(cal.time))

        }

        calendarDateEdit.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }



        labelInputEdit.addTextChangedListener {
            if (it!!.isNotEmpty())
                labelLayoutEdit.error = null
        }

        amountInputEdit.addTextChangedListener {
            if (it!!.isNotEmpty())
                amountLayoutEdit.error = null
        }

        updateBtnEdit.setOnClickListener {
            val label = labelInputEdit.text.toString()
            val description = descriptionInputEdit.text.toString()
            var amount = amountInputEdit.text.toString().toDoubleOrNull()
            val date = calendarDateEdit.text.toString()


            if (label.isEmpty())
                labelLayoutEdit.error = "Please enter a valid label"
            else if (amount == null)
                amountLayoutEdit.error = "Please enter a valid amount"
            else {
                if(expenseEdit.isChecked){
                    amount = -amount
                }
                val transaction = Transaction(transactionId, label, amount, description, date)
                vm.updateTransactions(transaction)
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Transaction Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        deleteBtnEdit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete this item?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                vm.deleteTransactions(transactionId)
                dialog.cancel()
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Transaction Deleted", Toast.LENGTH_SHORT).show()

            })
            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.cancel()
            })

            builder.create().show()

        }

        closeBtnEdit.setOnClickListener {
            finish()
        }
    }

}
