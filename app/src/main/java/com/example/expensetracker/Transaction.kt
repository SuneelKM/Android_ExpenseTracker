package com.example.expensetracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val amount: Double,
    val description: String,
    val date: Date
)