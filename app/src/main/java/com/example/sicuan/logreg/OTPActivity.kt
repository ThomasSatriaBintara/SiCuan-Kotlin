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
import kotlinx.coroutines.launch

class OTPActivity : AppCompatActivity() {

    private lateinit var etOtp: EditText
    private lateinit var btnVerifikasi: Button
    private lateinit var email: String  // optional, kalau ingin dikirim lagi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_otp)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, LupaActivity::class.java))
        }

        etOtp = findViewById(R.id.et_otp)
        btnVerifikasi = findViewById(R.id.btn_verifikasi_otp)

        email = intent.getStringExtra("email") ?: ""

        btnVerifikasi.setOnClickListener {
            val otpCode = etOtp.text.toString()

            if (otpCode.isEmpty()) {
                Toast.makeText(this, "Kode OTP tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val body = mapOf("otp" to otpCode)
                    val response = ApiClient.instance.verifyOtp(body)

                    if (response.isSuccessful && response.body()?.success == true) {
                        val otpToken = response.body()!!.data.resetToken

                        Toast.makeText(this@OTPActivity, "OTP valid", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@OTPActivity, ResetActivity::class.java)
                        intent.putExtra("resetToken", otpToken)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@OTPActivity, "OTP tidak valid", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@OTPActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
