package com.example.expensetracker


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.R
import com.example.expensetracker.Transaction
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlin.math.abs

class TransactionAdapter(private var transactions: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label : TextView = view.findViewById(R.id.label)
        val amount : TextView = view.findViewById(R.id.amount)
        val date : TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_layout, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.amount.context

        if(transaction.amount >= 0){
            holder.amount.text = "+ $%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        }else {
            holder.amount.text = "- $%.2f".format(abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.label.text = transaction.label
        holder.date.text = transaction.date
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("transactionId", transaction.id)
            intent.putExtra("label", transaction.label)
            intent.putExtra("amount", transaction.amount)
            intent.putExtra("description", transaction.description)
            intent.putExtra("date",transaction.date)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun getTransactions(transaction: List<Transaction>) {
        this.transactions.clear()
        this.transactions.addAll(transaction)
        notifyDataSetChanged()
    }

}