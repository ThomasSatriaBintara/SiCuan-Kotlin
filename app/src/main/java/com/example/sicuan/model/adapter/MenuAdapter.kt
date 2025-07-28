package com.example.sicuan.model.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.dashboard.hpp.ResepActivity
import com.example.sicuan.model.response.Menu

class MenuAdapter(
    private var menuList: List<Menu>
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaMenu: TextView = view.findViewById(R.id.tvNamaMenu)
        val tvHpp: TextView = view.findViewById(R.id.tvHpp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menuList[position]
        holder.tvNamaMenu.text = menu.nama_menu
        holder.tvHpp.text = "HPP : ${menu.hpp}"

        // ✅ Tambahkan klik listener di sini
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Toast.makeText(context, "Klik menu ID: ${menu.id}", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, ResepActivity::class.java)
            intent.putExtra("nama_menu", menu.nama_menu) // jika ingin kirim data nama menu
            intent.putExtra("id_menu", menu.id) // id dikirim sebagai String
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = menuList.size

    fun updateData(newList: List<Menu>) {
        menuList = newList
        notifyDataSetChanged()
    }
}