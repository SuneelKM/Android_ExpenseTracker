package com.example.expensetracker


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.database.Transaction.Transaction
import com.example.expensetracker.database.Transaction.getFormattedAmount
import com.example.expensetracker.database.Transaction.getFormattedDate
import com.example.expensetracker.databinding.TransactionLayoutBinding

class TransactionAdapter(private val onItemClicked: (Transaction) -> Unit) :
    ListAdapter<Transaction, TransactionAdapter.TransactionHolder>(DiffCallback) {

    class TransactionHolder(private var binding: TransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.labelT.text = transaction.label

            val context = binding.labelT.context
            if (transaction.amount >= 0) {
                binding.amountT.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else binding.amountT.setTextColor(ContextCompat.getColor(context, R.color.red))

            binding.amountT.text = transaction.getFormattedAmount()
            binding.dateT.text = transaction.getFormattedDate()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        return TransactionHolder(
            TransactionLayoutBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {

        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.date == newItem.date
            }
        }
    }

}