package com.example.expensetracker.database.Transaction

import android.icu.text.SimpleDateFormat
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensetracker.R
import java.text.NumberFormat
import java.util.*


@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val amount: Double,
    val description: String,
    val date: Date
)

fun Transaction.getFormattedDate(): String =
    SimpleDateFormat("EEEE, dd MMM yyyy").format(date)


fun Transaction.getFormattedAmount(): String =
    NumberFormat.getCurrencyInstance().format(amount)



