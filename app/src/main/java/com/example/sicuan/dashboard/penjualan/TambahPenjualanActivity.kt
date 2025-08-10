package com.example.sicuan.dashboard.penjualan

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.request.PenjualanRequest
import com.example.sicuan.model.response.Menu
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TambahPenjualanActivity : AppCompatActivity() {

    private lateinit var spinnerMenu: Spinner
    private lateinit var tvTanggal: TextView
    private lateinit var tvHpp: TextView
    private lateinit var tvHargaJual: TextView
    private lateinit var etJumlahLaku: EditText
    private lateinit var etKeterangan: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button

    private var selectedTanggal: String = ""
    private var selectedNamaMenu: String = ""
    private var selectedHpp: Int = 0
    private var selectedHargaJual: Int = 0
    private var menuList: List<Menu> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_penjualan_input)

        spinnerMenu = findViewById(R.id.spinnerMenu)
        tvTanggal = findViewById(R.id.etTanggal)
        tvHpp = findViewById(R.id.etHpp)
        tvHargaJual = findViewById(R.id.etHargaJual)
        etJumlahLaku = findViewById(R.id.etJumlahLaku)
        etKeterangan = findViewById(R.id.etKeterangan)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnBatal = findViewById(R.id.btnBatal)

        tvTanggal.setOnClickListener {
            showDatePicker()
        }

        btnBatal.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                startActivity(Intent(this@TambahPenjualanActivity, PenjualanActivity::class.java))
            }
        }

        // Get daftar menu dari backend
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getMenus()
                if (response.isSuccessful) {
                    val menus = response.body()?.data?.menus ?: emptyList()
                    menuList = menus

                    val menuNames = menus.map { it.nama_menu }
                    val adapter = ArrayAdapter(
                        this@TambahPenjualanActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        menuNames
                    )
                    spinnerMenu.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(this@TambahPenjualanActivity, "Gagal memuat menu", Toast.LENGTH_SHORT).show()
            }
        }

        spinnerMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMenu = menuList[position]
                selectedNamaMenu = selectedMenu.nama_menu
                selectedHpp = selectedMenu.hpp
                selectedHargaJual = selectedMenu.harga_jual ?: 0

                tvHpp.text = "Rp ${NumberFormat.getInstance().format(selectedHpp)}"
                tvHargaJual.text = "Rp ${NumberFormat.getInstance().format(selectedHargaJual)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSimpan.setOnClickListener {
            val jumlahLaku = etJumlahLaku.text.toString().toIntOrNull()
            val keterangan = etKeterangan.text.toString()

            if (selectedTanggal.isEmpty() || selectedNamaMenu.isEmpty() || jumlahLaku == null) {
                Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = PenjualanRequest(
                tanggal = selectedTanggal,
                nama_menu = selectedNamaMenu,
                jumlah_laku = jumlahLaku,
                keterangan = if (keterangan.isBlank()) null else keterangan
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.instance.postSales(request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@TambahPenjualanActivity, "Berhasil menyimpan penjualan", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@TambahPenjualanActivity, PenjualanActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@TambahPenjualanActivity, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@TambahPenjualanActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val pickedCalendar = Calendar.getInstance()
            pickedCalendar.set(year, month, dayOfMonth)

            val sdfBackend = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // hanya tanggal
            val sdfDisplay = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

            selectedTanggal = sdfBackend.format(pickedCalendar.time) // ⬅️ hasil: 2025-08-06
            tvTanggal.text = sdfDisplay.format(pickedCalendar.time)

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }

}
