package com.example.sicuan.dashboard.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sicuan.R
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.logreg.LoginActivity

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val navProfile = findViewById<LinearLayout>(R.id.setProfile)
        val navPassword = findViewById<LinearLayout>(R.id.setPassword)
        val navTentang = findViewById<LinearLayout>(R.id.setTentang)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, SetProfileActivity::class.java))
            finish()
        }

        navPassword.setOnClickListener {
            startActivity(Intent(this, SetPasswordActivity::class.java))
            finish()
        }

        navTentang.setOnClickListener {
            startActivity(Intent(this, SetTentangActivity::class.java))
            finish()
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
