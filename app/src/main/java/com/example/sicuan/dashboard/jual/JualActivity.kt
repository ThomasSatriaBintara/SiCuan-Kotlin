package com.example.sicuan.dashboard.jual

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.api.ApiClient
import com.example.sicuan.R
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.adapter.HargaAdapter
import com.example.sicuan.model.request.JualRequest
import com.example.sicuan.model.response.Menu
import kotlinx.coroutines.launch

class JualActivity : AppCompatActivity() {

    private lateinit var spinnerNamaMenu: Spinner
    private lateinit var etHpp: EditText
    private lateinit var etKeuntungan: EditText
    private lateinit var etHargaJual: EditText
    private lateinit var btnSimpan: Button

    private var listMenu: List<Menu> = emptyList()
    private var selectedMenu: Menu? = null

    private lateinit var hargaAdapter: HargaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jual)

        // Bottom Navigation
        findViewById<LinearLayout>(R.id.nav_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_hpp).setOnClickListener {
            startActivity(Intent(this, HPPActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_stok).setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java)); finish()
        }
        findViewById<LinearLayout>(R.id.nav_penjualan).setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java)); finish()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        val btnHitung = findViewById<Button>(R.id.btnHitung)
        val layoutDaftar = findViewById<CardView>(R.id.layoutDaftarHarga)
        val layoutHitung = findViewById<CardView>(R.id.layoutHitungHarga)

        btnDaftar.setOnClickListener {
            layoutDaftar.visibility = View.VISIBLE
            layoutHitung.visibility = View.GONE
        }
        btnHitung.setOnClickListener {
            layoutDaftar.visibility = View.GONE
            layoutHitung.visibility = View.VISIBLE
        }

        // Input
        spinnerNamaMenu = findViewById(R.id.spinnerNamaMenu)
        etHpp = findViewById(R.id.etHpp)
        etKeuntungan = findViewById(R.id.etKeuntungan)
        etHargaJual = findViewById(R.id.etHargaJual)
        btnSimpan = findViewById(R.id.btnSimpan)

        loadMenuWithCoroutine()
        setupListeners()
        loadDaftarHarga()
    }

    private fun loadMenuWithCoroutine() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMenus()
                if (response.isSuccessful && response.body()?.success == true) {
                    listMenu = response.body()?.data?.menus ?: emptyList()
                    val menuNames = listMenu.map { it.nama_menu }

                    val adapter = ArrayAdapter(this@JualActivity, android.R.layout.simple_spinner_item, menuNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerNamaMenu.adapter = adapter

                    spinnerNamaMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            selectedMenu = listMenu[position]
                            etHpp.setText(selectedMenu?.hpp.toString())
                            calculateHargaJual()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                } else {
                    Toast.makeText(this@JualActivity, "Gagal mengambil menu", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@JualActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        etKeuntungan.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculateHargaJual()
            }
        })

        btnSimpan.setOnClickListener {
            val keuntungan = etKeuntungan.text.toString().toIntOrNull()
            val namaMenu = selectedMenu?.nama_menu

            if (namaMenu.isNullOrEmpty() || keuntungan == null) {
                Toast.makeText(this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = JualRequest(
                nama_menu = namaMenu,
                keuntungan = keuntungan
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.instance.patchHargaJual(request)
                    if (response.isSuccessful) {
                        Toast.makeText(this@JualActivity, "Harga jual berhasil disimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@JualActivity, "Gagal menyimpan harga jual", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@JualActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun calculateHargaJual() {
        val hpp = selectedMenu?.hpp ?: 0
        val persen = etKeuntungan.text.toString().toFloatOrNull() ?: 0f
        val hargaJual = ((persen / 100) * hpp) + hpp
        etHargaJual.setText(hargaJual.toInt().toString())
    }

    private fun loadDaftarHarga() {
        val rvHarga = findViewById<RecyclerView>(R.id.rvHargaJual)
        hargaAdapter = HargaAdapter(emptyList())
        rvHarga.layoutManager = LinearLayoutManager(this)
        rvHarga.adapter = hargaAdapter

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMenus()
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data?.menus ?: emptyList()
                    hargaAdapter.updateData(data)
                } else {
                    Toast.makeText(this@JualActivity, "Gagal memuat harga", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@JualActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
