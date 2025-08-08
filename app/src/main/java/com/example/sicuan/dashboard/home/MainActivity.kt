package com.example.sicuan.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sicuan.R
import com.example.sicuan.chart.BarChartUtil
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.setting.SettingActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.databinding.ActivityMainBinding
import com.example.sicuan.logreg.LoginActivity
import com.example.sicuan.model.response.Penjualan
import com.example.sicuan.repository.MainRepository
import com.example.sicuan.viewmodel.MainViewModel
import com.example.sicuan.viewmodel.MainViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = MainRepository()
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupNavigation()

        mainViewModel.profile.observe(this) { profile ->
            binding.greetingText.text = getString(R.string.halo_sicuan, profile.username)
            binding.usernameText.text = profile.nama_usaha
        }

        mainViewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        mainViewModel.salesSummary.observe(this) { summary ->
            binding.totalPenjualan.text = "Rp. %,d".format(summary.totalPenjualan).replace(",", ".")
            binding.totalKeuntungan.text = "Rp. %,d".format(summary.totalKeuntungan).replace(",", ".")
        }

        mainViewModel.sales.observe(this) { sales ->
            setupChartWithData(sales)
        }

        mainViewModel.fetchProfile()
        mainViewModel.fetchSalesSummary()
        mainViewModel.fetchSalesToday()
    }

    private fun setupChartWithData(sales: List<Penjualan>) {
        Log.d("BAR_CHART", "Sales size: ${sales.size}")
        sales.forEach {
            Log.d("BAR_CHART", "Menu: ${it.nama_menu}, Laku: ${it.laku}")
        }

        val barChart = binding.barChart

        val grouped = sales.groupBy { it.nama_menu }
            .mapValues { it.value.sumOf { item -> item.laku } }

        val entries = grouped.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value.toFloat())
        }

        val labels = grouped.keys.toList()

        val dataSet = BarChartUtil.createDataSet(entries, this)
        val data = BarChartUtil.createBarData(dataSet)

        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        barChart.invalidate()
    }

    private fun setupNavigation() {
        with(binding) {
            bottomNavigation.navHpp.setOnClickListener {
                startActivity(Intent(this@MainActivity, HPPActivity::class.java))
                finish()
            }
            bottomNavigation.navStok.setOnClickListener {
                startActivity(Intent(this@MainActivity, StokActivity::class.java))
                finish()
            }
            bottomNavigation.navPenjualan.setOnClickListener {
                startActivity(Intent(this@MainActivity, PenjualanActivity::class.java))
                finish()
            }
            bottomNavigation.navJual.setOnClickListener {
                startActivity(Intent(this@MainActivity, JualActivity::class.java))
                finish()
            }
            settingsIcon.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                finish()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        return !token.isNullOrEmpty()
    }
}