package com.example.sicuan.dashboard.hpp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.request.TambahResepRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditResepActivity : AppCompatActivity() {

    private lateinit var etNamaBahan: EditText
    private lateinit var etHarga: EditText
    private lateinit var etJumlah: EditText
    private lateinit var etJumlahDigunakan: EditText
    private lateinit var spinnerSatuan: Spinner
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button

    private var menuId: String? = null
    private var resepId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_input)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            finish()
        }

        setupNavigation()

        // Ambil data intent
        menuId = intent.getStringExtra("menu_id")
        resepId = intent.getStringExtra("recipe_id")

        if (menuId.isNullOrEmpty() || resepId.isNullOrEmpty()) {
            Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etNamaBahan = findViewById(R.id.editNamaBahan)
        etHarga = findViewById(R.id.editHargaBeli)
        etJumlah = findViewById(R.id.editJumlah)
        etJumlahDigunakan = findViewById(R.id.editJumlahDigunakan)
        spinnerSatuan = findViewById(R.id.spinnerSatuan)
        btnSimpan = findViewById(R.id.btnTambah)
        btnBatal = findViewById(R.id.btnBatal)

        // Disable nama bahan dan ubah warna jadi abu-abu
        etNamaBahan.apply {
            isEnabled = false
            setText(intent.getStringExtra("nama_bahan"))
            setTextColor(resources.getColor(R.color.gray, null))
        }

        // Set nilai awal dari intent
        etHarga.setText(intent.getIntExtra("harga_beli", 0).toString())
        etJumlah.setText(intent.getDoubleExtra("jumlah_beli", 0.0).toInt().toString())
        etJumlahDigunakan.setText(intent.getDoubleExtra("jumlah_digunakan", 0.0).toInt().toString())

        // Spinner satuan
        val satuanArray = resources.getStringArray(R.array.tambah_resep_spinner)
        val satuanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, satuanArray)
        satuanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSatuan.adapter = satuanAdapter

        // (Opsional) set selected item jika kamu kirim nilai satuan lewat intent
        val satuan = intent.getStringExtra("satuan")
        satuan?.let {
            val position = satuanArray.indexOf(it)
            if (position >= 0) spinnerSatuan.setSelection(position)
        }

        btnSimpan.setOnClickListener { updateResep() }
        btnBatal.setOnClickListener { finish() }
    }

    private fun updateResep() {
        val harga = etHarga.text.toString().toIntOrNull()
        val jumlah = etJumlah.text.toString().toIntOrNull()
        val jumlahDigunakan = etJumlahDigunakan.text.toString().toIntOrNull()
        val satuan = spinnerSatuan.selectedItem.toString()

        if (harga == null || jumlah == null || jumlahDigunakan == null) {
            Toast.makeText(this, "Isi semua field dengan benar", Toast.LENGTH_SHORT).show()
            return
        }

        val request = TambahResepRequest(
            nama_bahan = etNamaBahan.text.toString(), // tetap kirim walau tidak bisa diedit
            harga_beli = harga,
            jumlah_beli = jumlah,
            satuan = satuan,
            jumlah_digunakan = jumlahDigunakan
        )

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.instance.updateResep(menuId!!, resepId!!, request)
                }

                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@EditResepActivity, "Berhasil update resep", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@EditResepActivity,
                        "Gagal update: ${response.body()?.message ?: response.errorBody()?.string()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditResepActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupNavigation() {
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
        findViewById<LinearLayout>(R.id.nav_penjualan).setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java)); finish()
        }
    }
}
