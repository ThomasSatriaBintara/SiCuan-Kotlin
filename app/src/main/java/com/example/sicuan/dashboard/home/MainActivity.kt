package com.example.sicuan.dashboard.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sicuan.R
import com.example.sicuan.chart.BarChartUtil
import com.example.sicuan.dashboard.*
import com.example.sicuan.dashboard.hpp.HPPActivity
import com.example.sicuan.dashboard.jual.JualActivity
import com.example.sicuan.dashboard.penjualan.PenjualanActivity
import com.example.sicuan.dashboard.setting.SettingActivity
import com.example.sicuan.dashboard.stok.StokActivity
import com.example.sicuan.databinding.ActivityMainBinding
import com.example.sicuan.logreg.LoginActivity
import com.example.sicuan.repository.MainRepository
import com.example.sicuan.viewmodel.MainViewModel
import com.example.sicuan.viewmodel.MainViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = MainRepository() // Pastikan MainRepository tidak butuh context
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        if (!isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        mainViewModel.profile.observe(this, Observer { profile ->
            binding.greetingText.text = getString(R.string.halo_sicuan, profile.username)
            binding.usernameText.text = profile.nama_usaha
        })

        mainViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        mainViewModel.fetchProfile()

        setupChart()
    }

    private fun setupChart() {
        val barChart = binding.barChart
        val entries = BarChartUtil.createSampleEntries()
        val labels = listOf("Es Teh", "Es Jeruk", "Good Day", "Es Sirup")

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
            binding.bottomNavigation.navHpp.setOnClickListener {
                startActivity(Intent(this@MainActivity, HPPActivity::class.java))
                finish()
            }
            binding.bottomNavigation.navStok.setOnClickListener {
                startActivity(Intent(this@MainActivity, StokActivity::class.java))
                finish()
            }
            binding.bottomNavigation.navPenjualan.setOnClickListener {
                startActivity(Intent(this@MainActivity, PenjualanActivity::class.java))
                finish()
            }
            binding.bottomNavigation.navJual.setOnClickListener {
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