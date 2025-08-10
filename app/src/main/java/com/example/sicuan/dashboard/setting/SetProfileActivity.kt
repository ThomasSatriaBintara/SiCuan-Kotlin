package com.example.sicuan.dashboard.setting

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import kotlinx.coroutines.launch

class SetProfileActivity : AppCompatActivity() {

    private lateinit var etNamaPengguna: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNamaUsaha: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_profile)

        etNamaPengguna = findViewById(R.id.etNama)
        etEmail = findViewById(R.id.etEmail)
        etNamaUsaha = findViewById(R.id.etUsaha)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        getProfileData()
    }

    private fun getProfileData() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getProfile()
                if (response.isSuccessful && response.body()?.success == true) {
                    val profile = response.body()!!.data.profile
                    etNamaPengguna.setText(profile.username)
                    etEmail.setText(profile.email)
                    etNamaUsaha.setText(profile.nama_usaha)
                    etEmail.isEnabled = false // Email tidak bisa diubah
                } else {
                    Toast.makeText(this@SetProfileActivity, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SetProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
