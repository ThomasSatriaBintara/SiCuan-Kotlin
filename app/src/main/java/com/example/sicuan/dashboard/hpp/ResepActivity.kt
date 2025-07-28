package com.example.sicuan.dashboard.hpp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sicuan.api.ApiClient
import com.example.sicuan.R
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.model.adapter.ResepAdapter
import com.example.sicuan.model.response.ResepResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResepActivity : AppCompatActivity() {

    private var idMenu: String? = null
    private var namaMenu: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var resepAdapter: ResepAdapter

    // ✅ Activity Result API
    private val tambahResepLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loadDataResep()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resep)

        setupNavigation()

        idMenu = intent.getStringExtra("id_menu")
        namaMenu = intent.getStringExtra("nama_menu")

        if (idMenu.isNullOrEmpty()) {
            Toast.makeText(this, "Menu ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        recyclerView = findViewById(R.id.recyclerMenu)
        resepAdapter = ResepAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = resepAdapter

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, TambahResepActivity::class.java)
            intent.putExtra("menu_id", idMenu)
            tambahResepLauncher.launch(intent)
        }

        loadDataResep()
    }

    private fun setupNavigation() {
        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            setNavigationOnClickListener {
                startActivity(Intent(this@ResepActivity, HPPActivity::class.java))
            }
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
        findViewById<LinearLayout>(R.id.nav_penjualan).setOnClickListener {
            startActivity(Intent(this, PenjualanActivity::class.java)); finish()
        }
    }

    private fun loadDataResep() {
        if (idMenu == null) return

        ApiClient.instance.getResep(idMenu!!).enqueue(object : Callback<ResepResponse> {
            override fun onResponse(call: Call<ResepResponse>, response: Response<ResepResponse>) {
                if (response.isSuccessful) {
                    val listResep = response.body()?.data?.recipes ?: emptyList()
                    resepAdapter.updateData(listResep)
                } else {
                    Toast.makeText(this@ResepActivity, "Gagal ambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResepResponse>, t: Throwable) {
                Toast.makeText(this@ResepActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}