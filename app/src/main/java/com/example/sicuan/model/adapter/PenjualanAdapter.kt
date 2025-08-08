package com.example.sicuan.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.model.response.Penjualan

class PenjualanAdapter(
    private var list: List<Penjualan>,
    private val onDeleteClick: (Penjualan) -> Unit
) : RecyclerView.Adapter<PenjualanAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaMenu: TextView = view.findViewById(R.id.tvNamaProduk)
        val tvJumlahLaku: TextView = view.findViewById(R.id.tvJumlahLaku)
        val tvIncome: TextView = view.findViewById(R.id.tvIncome)
        val tvProfit: TextView = view.findViewById(R.id.tvProfit)

        val btnDelete: View = view.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_penjualan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNamaMenu.text = item.nama_menu
        holder.tvJumlahLaku.text = "Laku: ${item.laku}"
        holder.tvIncome.text = "Income: Rp ${item.income}"
        holder.tvProfit.text = "Profit: Rp ${item.profit}"

        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }

    }

    fun updateData(newList: List<Penjualan>) {
        list = newList
        notifyDataSetChanged()
    }
}
