package com.example.expensetracker


import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.amountInput
import kotlinx.android.synthetic.main.activity_add_transaction.amountLayout
import kotlinx.android.synthetic.main.activity_add_transaction.closeBtn
import kotlinx.android.synthetic.main.activity_add_transaction.descriptionInput
import kotlinx.android.synthetic.main.activity_add_transaction.labelInput
import kotlinx.android.synthetic.main.activity_add_transaction.labelLayout
import kotlinx.android.synthetic.main.activity_detailed.*

import java.util.*

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction
    lateinit var vm: TransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        vm = TransactionViewModel(application)

        val label = intent.getStringExtra("label")
        val amount = intent.getDoubleExtra("amount", 0.0)
        val description = intent.getStringExtra("description")
        val transactionId = intent.getIntExtra("transactionId", -1)
        val date = intent.getStringExtra("date")


        labelInput.setText(label)
        amountInput.setText(amount.toString())
        descriptionInput.setText(description)

        calendarDate1.setText(date)



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

            val myFormat = "EEE, dd MMM yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            calendarDate1.setText(sdf.format(cal.time))

        }

        calendarDate1.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        labelInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                amountLayout.error = null
        }

        updateBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            var amount = amountInput.text.toString().toDoubleOrNull()
            var date = calendarDate1.text.toString()
            println("Hello    $date")

            if (label.isEmpty())
                labelLayout.error = "Please enter a valid label"
            else if (amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                amount = Math.abs(amount)
                if(expense1.isChecked){
                    amount = -amount
                }
                val transaction = Transaction(transactionId, label, amount, description, date)
                vm.updateTransactions(transaction)
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this,"Transaction Updated", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
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

        closeBtn.setOnClickListener {
            finish()
        }
    }

}
