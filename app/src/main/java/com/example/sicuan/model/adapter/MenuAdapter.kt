package com.example.sicuan.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.dashboard.hpp.ResepActivity
import com.example.sicuan.model.response.Menu

class MenuAdapter(
    private var menuList: List<Menu>,
    private val onDeleteClick: (String) -> Unit,
    private val onEditClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaMenu: TextView = view.findViewById(R.id.tvNamaMenu)
        val tvHpp: TextView = view.findViewById(R.id.tvHpp)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit) // Tambahkan ID ini di XML item_menu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.tvNamaMenu.text = menu.nama_menu
        holder.tvHpp.text = "HPP : ${menu.hpp}"

        holder.btnDelete.setOnClickListener { onDeleteClick(menu.id) }
        holder.btnEdit.setOnClickListener { onEditClick(menu) }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ResepActivity::class.java)
            intent.putExtra("nama_menu", menu.nama_menu)
            intent.putExtra("id_menu", menu.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = menuList.size

    fun updateData(newList: List<Menu>) {
        menuList = newList
        notifyDataSetChanged()
    }
}