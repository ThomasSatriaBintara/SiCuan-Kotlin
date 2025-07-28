package com.example.sicuan.dashboard.penjualan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PenjualanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan)

        val etTanggalAwal = findViewById<EditText>(R.id.etTanggalAwal)
        val etTanggalAkhir = findViewById<EditText>(R.id.etTanggalAkhir)

        etTanggalAwal.setOnClickListener {
            showDatePicker { selectedDate ->
                etTanggalAwal.setText(selectedDate)
            }
        }

        etTanggalAkhir.setOnClickListener {
            showDatePicker { selectedDate ->
                etTanggalAkhir.setText(selectedDate)
            }
        }

        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navHpp = findViewById<LinearLayout>(R.id.nav_hpp)
        val navStok = findViewById<LinearLayout>(R.id.nav_stok)
        val navJual = findViewById<LinearLayout>(R.id.nav_jual)

        val fabAdd = findViewById<Button>(R.id.btnTambah)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahPenjualanActivity::class.java))
            finish()
        }
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

        navStok.setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java))
            finish()
        }

        navJual.setOnClickListener {
            startActivity(Intent(this, JualActivity::class.java))
            finish()
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val dateString = sdf.format(Date(selection))
            onDateSelected(dateString)
        }

        picker.show(supportFragmentManager, "DATE_PICKER")
    }


}