package com.example.sicuan.dashboard.stok

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.api.ApiClient
import com.example.sicuan.R
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.model.adapter.StokAdapter
import com.example.sicuan.model.request.StokRequest
import com.example.sicuan.model.response.Stock
import kotlinx.coroutines.launch

class StokActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var tvHampirHabis: TextView
    private lateinit var adapter: StokAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stok)

        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navHpp = findViewById<LinearLayout>(R.id.nav_hpp)
        val navPenjualan = findViewById<LinearLayout>(R.id.nav_penjualan)
        val navJual = findViewById<LinearLayout>(R.id.nav_jual)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        navHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        navHpp.setOnClickListener {
            startActivity(Intent(this, HPPActivity::class.java))
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

        recyclerView = findViewById(R.id.recyclerViewStok)
        tvTotal = findViewById(R.id.tvTotalBahan)
        tvHampirHabis = findViewById(R.id.tvHampirHabis)

        adapter = StokAdapter(emptyList()) { stock ->
            showEditStokDialog(stock)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchStok()
    }

    private fun fetchStok() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getStocks()
                if (response.isSuccessful && response.body()?.success == true) {
                    val stocks = response.body()?.data?.stocks ?: emptyList()
                    adapter.updateData(stocks)
                    tvTotal.text = stocks.size.toString()
                    tvHampirHabis.text = stocks.count { it.jumlah <= it.minimum_stock }.toString()
                } else {
                    Toast.makeText(this@StokActivity, "Gagal memuat stok", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@StokActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditStokDialog(stock: Stock) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_stok, null)

        val etNamaBahan = dialogView.findViewById<EditText>(R.id.etNamaBahan)
        val etJumlah = dialogView.findViewById<EditText>(R.id.etJumlah)
        val etMinimumStok = dialogView.findViewById<EditText>(R.id.etMinimumStok)
        val spinnerTransaksi = dialogView.findViewById<Spinner>(R.id.spinnerTransaksi)
        val etKeterangan = dialogView.findViewById<EditText>(R.id.etKeterangan)
        val btnSimpan = dialogView.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)

        etNamaBahan.setText(stock.nama_bahan)
        etNamaBahan.isEnabled = false
        etJumlah.setText(stock.jumlah.toString())
        etMinimumStok.setText(stock.minimum_stock.toString())

        ArrayAdapter.createFromResource(
            this,
            R.array.stok_spinner,
            android.R.layout.simple_spinner_item
        ).also { adapterSpinner ->
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTransaksi.adapter = adapterSpinner
        }

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnSimpan.setOnClickListener {
            val jumlah = etJumlah.text.toString().toIntOrNull() ?: 0
            val minStok = etMinimumStok.text.toString().toIntOrNull() ?: 0
            val transaksi = spinnerTransaksi.selectedItem?.toString() ?: "PEMBELIAN"
            val keterangan = etKeterangan.text.toString()

            val request = StokRequest(
                nama_bahan = stock.nama_bahan,
                jumlah = jumlah,
                jenis_transaksi = transaksi,
                keterangan = keterangan,
                minimum_stock = minStok
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.instance.patchStock(stock.id, request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@StokActivity, "Stok berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        fetchStok()
                    } else {
                        Toast.makeText(this@StokActivity, "Gagal update stok", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@StokActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }
}
