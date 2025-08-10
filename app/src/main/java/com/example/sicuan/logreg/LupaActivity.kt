package com.example.sicuan.logreg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.api.ApiClient
import com.example.sicuan.R
import com.example.sicuan.model.request.LupaRequest
import kotlinx.coroutines.launch

class LupaActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var btnKirim: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        etEmail = findViewById(R.id.et_email)
        btnKirim = findViewById(R.id.btn_send_code)

        btnKirim.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val request = LupaRequest(email)
                    val response = ApiClient.instance.forgetPassword(request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@LupaActivity, "Kode OTP telah dikirim", Toast.LENGTH_SHORT).show()

                        // Intent ke halaman OTP sambil mengirim email
                        val intent = Intent(this@LupaActivity, OTPActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LupaActivity, "Gagal mengirim kode OTP", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LupaActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}