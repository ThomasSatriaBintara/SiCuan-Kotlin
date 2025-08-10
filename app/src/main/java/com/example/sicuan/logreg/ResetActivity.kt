package com.example.sicuan.logreg

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.model.request.ResetRequest
import kotlinx.coroutines.launch

class ResetActivity : AppCompatActivity() {

    private lateinit var etPasswordBaru: EditText
    private lateinit var showPasswordBtn: ImageButton
    private lateinit var etKonfirmasiPassword: EditText
    private lateinit var showConfirmPasswordBtn: ImageButton
    private lateinit var btnSimpan: Button

    private lateinit var resetToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_reset)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, OTPActivity::class.java))
        }

        etPasswordBaru = findViewById(R.id.et_new_password)
        etKonfirmasiPassword = findViewById(R.id.et_confirm_password)
        btnSimpan = findViewById(R.id.btn_reset_password)
        showPasswordBtn = findViewById(R.id.showPasswordBtn)
        showConfirmPasswordBtn = findViewById(R.id.showConfirmPasswordBtn)

        resetToken = intent.getStringExtra("resetToken") ?: ""

        // Set up password visibility toggle with hold and performClick
        showPasswordBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    etPasswordBaru.transformationMethod = null
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    etPasswordBaru.transformationMethod = PasswordTransformationMethod.getInstance()
                    true
                }
                else -> false
            }
        }

        // Set up confirm password visibility toggle with hold and performClick
        showConfirmPasswordBtn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    etKonfirmasiPassword.transformationMethod = null
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    etKonfirmasiPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    true
                }
                else -> false
            }
        }

        btnSimpan.setOnClickListener {
            val newPassword = etPasswordBaru.text.toString()
            val confirmPassword = etKonfirmasiPassword.text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = ResetRequest(
                otp = resetToken,
                newPassword = newPassword,
                confirmPassword = confirmPassword
            )

            lifecycleScope.launch {
                try {
                    val response = ApiClient.instance.resetPassword("Bearer $resetToken", request)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@ResetActivity, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show()

                        // Tidak menyimpan token reset, langsung arahkan ke Login dan hapus back stack
                        val intent = Intent(this@ResetActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        val errorMessage = response.body()?.message ?: "Gagal mengubah password"
                        Toast.makeText(this@ResetActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ResetActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}