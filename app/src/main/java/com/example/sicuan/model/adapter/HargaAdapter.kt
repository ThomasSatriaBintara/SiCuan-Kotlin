package com.example.sicuan.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.model.response.Menu

class HargaAdapter(private var list: List<Menu>) :
    RecyclerView.Adapter<HargaAdapter.HargaViewHolder>() {

    inner class HargaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaMenu: TextView = itemView.findViewById(R.id.tvNamaMenu)
        val tvKeuntungan: TextView = itemView.findViewById(R.id.tvKeuntungan)
        val tvHargaJual: TextView = itemView.findViewById(R.id.tvHargaJual)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HargaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jual, parent, false)
        return HargaViewHolder(view)
    }

    override fun onBindViewHolder(holder: HargaViewHolder, position: Int) {
        val menu = list[position]
        holder.tvNamaMenu.text = menu.nama_menu
        holder.tvKeuntungan.text = "Keuntungan: ${menu.keuntungan ?: 0}%"
        holder.tvHargaJual.text = "Harga Jual: Rp ${menu.harga_jual ?: 0}"
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<Menu>) {
        list = newList
        notifyDataSetChanged()
    }
}
