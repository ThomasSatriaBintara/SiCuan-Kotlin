package com.example.sicuan.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.model.response.Resep

class ResepAdapter(
    private var resepList: List<Resep>,
    private val onDeleteClick: (Resep) -> Unit,
    private val onEditClick: (Resep) -> Unit
) : RecyclerView.Adapter<ResepAdapter.ResepViewHolder>() {

    inner class ResepViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaBahan: TextView = view.findViewById(R.id.tvNamaMenu)
        val tvBiaya: TextView = view.findViewById(R.id.tvHpp)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit) // 🟡 pastikan ada di layout XML!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ResepViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val resep = resepList[position]
        holder.tvNamaBahan.text = resep.nama_bahan
        holder.tvBiaya.text = "Biaya: ${resep.biaya}"

        holder.btnDelete.setOnClickListener { onDeleteClick(resep) }
        holder.btnEdit.setOnClickListener { onEditClick(resep) } // ✅ edit handler
    }

    override fun getItemCount(): Int = resepList.size

    fun updateData(newList: List<Resep>) {
        resepList = newList
        notifyDataSetChanged()
    }
}
