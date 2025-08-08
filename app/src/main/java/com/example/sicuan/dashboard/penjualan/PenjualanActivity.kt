package com.example.sicuan.dashboard.penjualan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.api.ApiService
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.adapter.PenjualanAdapter
import com.example.sicuan.model.response.Penjualan
import com.example.sicuan.model.response.PenjualanResponse
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PenjualanActivity : AppCompatActivity() {

    private lateinit var penjualanAdapter: PenjualanAdapter
    private lateinit var rvPenjualan: RecyclerView
    private lateinit var llNoData: LinearLayout
    private lateinit var apiService: ApiService

    private lateinit var etTanggalAwal: EditText
    private lateinit var etTanggalAkhir: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        apiService = ApiClient.instance
        rvPenjualan = findViewById(R.id.rvPenjualan)
        llNoData = findViewById(R.id.llNoData)
        etTanggalAwal = findViewById(R.id.etTanggalAwal)
        etTanggalAkhir = findViewById(R.id.etTanggalAkhir)

        penjualanAdapter = PenjualanAdapter(emptyList()) { penjualan ->
            showDeleteConfirmationDialog(penjualan)
        }

        rvPenjualan.layoutManager = LinearLayoutManager(this)
        rvPenjualan.adapter = penjualanAdapter

        val openDateRange = {
            showRangeDatePicker { startDate, endDate ->
                loadCustomSales(startDate, endDate)
            }
        }

        etTanggalAwal.setOnClickListener { openDateRange() }
        etTanggalAkhir.setOnClickListener { openDateRange() }

        // Navigasi
        findViewById<Button>(R.id.btnTambah).setOnClickListener {
            startActivity(Intent(this, TambahPenjualanActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_hpp).setOnClickListener {
            startActivity(Intent(this, HPPActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_stok).setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_jual).setOnClickListener {
            startActivity(Intent(this, JualActivity::class.java)); finish()
        }

        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener {
                startActivity(Intent(this@PenjualanActivity, MainActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadSalesToday()
    }

    private fun showRangeDatePicker(onRangeSelected: (String, String) -> Unit) {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pilih Rentang Tanggal")
            .setTheme(R.style.CustomDateRangePickerTheme)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startMillis = selection.first ?: return@addOnPositiveButtonClickListener
            val endMillis = selection.second ?: return@addOnPositiveButtonClickListener

            val dayDiff = ((endMillis - startMillis) / (1000 * 60 * 60 * 24)) + 1

            if (dayDiff > 7) {
                Toast.makeText(this, "Maksimal rentang tanggal 7 hari", Toast.LENGTH_SHORT).show()
                return@addOnPositiveButtonClickListener
            }

            val formatServer = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formatView = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

            val startDateFormatted = formatServer.format(Date(startMillis))
            val endDateFormatted = formatServer.format(Date(endMillis))

            val startView = formatView.format(Date(startMillis))
            val endView = formatView.format(Date(endMillis))

            etTanggalAwal.setText(startView)
            etTanggalAkhir.setText(endView)

            onRangeSelected(startDateFormatted, endDateFormatted)
        }

        picker.show(supportFragmentManager, "RANGE_DATE_PICKER")
    }

    private fun loadSalesToday() {
        lifecycleScope.launch {
            try {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                format.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

                val todayStr = format.format(calendar.time)

                val response = apiService.getSalesCustom(todayStr, todayStr)
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data?.sales ?: emptyList()
                    updateRecycler(data)
                    // Tambahkan log jika perlu untuk debug
                    Log.d("BAR_CHART", "Fetched ${data.size} sales for $todayStr")
                } else {
                    Toast.makeText(this@PenjualanActivity, "Gagal memuat data hari ini", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PenjualanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadCustomSales(startDate: String, endDate: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.getSalesCustom(startDate, endDate)
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data?.sales ?: emptyList()
                    updateRecycler(data)
                } else {
                    Toast.makeText(this@PenjualanActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PenjualanActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecycler(data: List<Penjualan>) {
        if (data.isEmpty()) {
            llNoData.visibility = View.VISIBLE
            rvPenjualan.visibility = View.GONE
        } else {
            llNoData.visibility = View.GONE
            rvPenjualan.visibility = View.VISIBLE
            penjualanAdapter.updateData(data)
        }
    }

    private fun showDeleteConfirmationDialog(penjualan: Penjualan) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Penjualan")
            .setMessage("Yakin ingin menghapus penjualan ${penjualan.nama_menu}?")
            .setPositiveButton("Hapus") { _, _ ->
                deletePenjualan(penjualan.id)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun deletePenjualan(salesId: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.deletePenjualan(salesId)
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@PenjualanActivity, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                    loadSalesToday()
                } else {
                    Toast.makeText(this@PenjualanActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PenjualanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}