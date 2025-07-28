package com.example.sicuan.dashboard.hpp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.api.ApiClient
import com.example.sicuan.api.ApiService
import com.example.sicuan.R
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.logreg.LoginActivity
import com.example.sicuan.model.adapter.MenuAdapter
import com.example.sicuan.model.request.MenuRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HPPActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerMenu: RecyclerView
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hpp)

        apiService = ApiClient.instance

        recyclerMenu = findViewById(R.id.recyclerMenu)
        recyclerMenu.layoutManager = LinearLayoutManager(this)
        menuAdapter = MenuAdapter(listOf())
        recyclerMenu.adapter = menuAdapter

        fetchMenus()

        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navStok = findViewById<LinearLayout>(R.id.nav_stok)
        val navPenjualan = findViewById<LinearLayout>(R.id.nav_penjualan)
        val navJual = findViewById<LinearLayout>(R.id.nav_jual)
        val fabAdd = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        fabAdd.setOnClickListener {
            showTambahMenuDialog()
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        navStok.setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java))
            finish()
        }

        navPenjualan.setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java))
            finish()
        }

        navJual.setOnClickListener {
            startActivity(Intent(this, JualActivity::class.java))
            finish()
        }
    }

    private fun fetchMenus() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { apiService.getMenus() }
                if (response.isSuccessful && response.body()?.success == true) {
                    val menuList = response.body()!!.data.menus
                    menuAdapter.updateData(menuList)
                } else {
                    Toast.makeText(this@HPPActivity, "Gagal menampilkan menu", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HPPActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTambahMenuDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_menu, null)
        val editMenu = dialogView.findViewById<EditText>(R.id.editMenu)
        val btnTambah = dialogView.findViewById<Button>(R.id.btnTambah)
        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")
        Log.d("HPPActivity", "Current token: $token")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.show()

        btnTambah.setOnClickListener {
            val namaMenu = editMenu.text.toString().trim()
            if (namaMenu.isNotEmpty()) {
                val request = MenuRequest(nama_menu = namaMenu)

                lifecycleScope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            apiService.addMenu(request)
                        }

                        if (response.isSuccessful) {
                            val menuResponse = response.body()
                            if (menuResponse?.success == true) {
                                Toast.makeText(this@HPPActivity, menuResponse.message, Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                                fetchMenus() // Refresh list
                            } else {
                                Toast.makeText(this@HPPActivity, menuResponse?.message ?: "Gagal menambahkan menu", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            when (response.code()) {
                                401 -> Toast.makeText(this@HPPActivity, "Token tidak valid, silakan login ulang", Toast.LENGTH_LONG).show()
                                400 -> Toast.makeText(this@HPPActivity, "Data tidak valid", Toast.LENGTH_SHORT).show()
                                else -> Toast.makeText(this@HPPActivity, "Gagal: ${response.code()} - ${response.message()}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@HPPActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                editMenu.error = "Nama menu tidak boleh kosong"
            }
        }
    }
}
