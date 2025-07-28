package com.example.sicuan.dashboard.hpp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.request.TambahResepRequest
import com.example.sicuan.model.response.TambahResepResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahResepActivity : AppCompatActivity() {

    private lateinit var etNamaBahan: EditText
    private lateinit var etHarga: EditText
    private lateinit var etJumlah: EditText
    private lateinit var etJumlahDigunakan: EditText
    private lateinit var spinnerSatuan: Spinner
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button

    private var menuId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep_input)

        val navHome = findViewById<LinearLayout>(R.id.nav_home)
        val navHpp = findViewById<LinearLayout>(R.id.nav_hpp)
        val navStok = findViewById<LinearLayout>(R.id.nav_stok)
        val navJual = findViewById<LinearLayout>(R.id.nav_jual)
        val navPenjualan = findViewById<LinearLayout>(R.id.nav_penjualan)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HPPActivity::class.java))
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

        navPenjualan.setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java))
            finish()
        }

        menuId = intent.getStringExtra("menu_id")
        if (menuId.isNullOrEmpty()) {
            Toast.makeText(this, "Menu ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inisialisasi komponen UI
        etNamaBahan = findViewById(R.id.editNamaBahan)
        etHarga = findViewById(R.id.editHargaBeli)
        etJumlah = findViewById(R.id.editJumlah)
        etJumlahDigunakan = findViewById(R.id.editJumlahDigunakan)
        spinnerSatuan = findViewById(R.id.spinnerSatuan)
        btnSimpan = findViewById(R.id.btnTambah)
        btnBatal = findViewById(R.id.btnBatal)

        // Set adapter untuk spinner satuan
        val satuanAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.tambah_resep_spinner,
            android.R.layout.simple_spinner_item
        )
        satuanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSatuan.adapter = satuanAdapter

        // Tombol Simpan
        btnSimpan.setOnClickListener {
            kirimDataKeAPI()
        }

        // Tombol Batal
        btnBatal.setOnClickListener {
            finish()
        }
    }

    private fun kirimDataKeAPI() {
        // Ambil dan validasi input

        val nama = etNamaBahan.text.toString().trim()
        val harga = etHarga.text.toString().toIntOrNull()
        val jumlah = etJumlah.text.toString().toIntOrNull()
        val satuan = spinnerSatuan.selectedItem.toString()
        val jumlahDigunakan = etJumlahDigunakan.text.toString().toIntOrNull()

        if (nama.isEmpty() || harga == null || jumlah == null || jumlahDigunakan == null) {
            Toast.makeText(this, "Harap isi semua field dengan benar", Toast.LENGTH_SHORT).show()
            return
        }

        // Siapkan request
        val request = TambahResepRequest(
            nama_bahan = nama,
            harga_beli = harga,
            jumlah_beli = jumlah,
            satuan = satuan,
            jumlah_digunakan = jumlahDigunakan,
        )

        // Panggil API
        ApiClient.instance.addResep(menuId!!, request)
            .enqueue(object : Callback<TambahResepResponse<Any>> {
                override fun onResponse(
                    call: Call<TambahResepResponse<Any>>,
                    response: Response<TambahResepResponse<Any>>
                ) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res?.success == true) {
                            Toast.makeText(this@TambahResepActivity, res.message, Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this@TambahResepActivity, res?.message ?: "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@TambahResepActivity, "Gagal simpan: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<TambahResepResponse<Any>>, t: Throwable) {
                    Toast.makeText(this@TambahResepActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}