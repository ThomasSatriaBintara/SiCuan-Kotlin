package com.example.sicuan.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.model.response.Stock

class StokAdapter(
    private var stocks: List<Stock>,
    private val onEditClicked: (Stock) -> Unit
) : RecyclerView.Adapter<StokAdapter.StockViewHolder>() {

    inner class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNo: TextView = view.findViewById(R.id.tvNo)
        val tvNama: TextView = view.findViewById(R.id.tvNamaBahan)
        val tvJumlah: TextView = view.findViewById(R.id.tvJumlah)
        val tvMin: TextView = view.findViewById(R.id.tvMinStok)
        val btnAksi: ImageView = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stok, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stocks[position]
        holder.tvNo.text = (position + 1).toString()
        holder.tvNama.text = stock.nama_bahan
        holder.tvJumlah.text = "${stock.jumlah} ${stock.satuan}"
        holder.tvMin.text = stock.minimum_stock.toString()

        holder.btnAksi.setOnClickListener {
            onEditClicked(stock)
        }
    }

    override fun getItemCount(): Int = stocks.size

    fun updateData(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }
}
