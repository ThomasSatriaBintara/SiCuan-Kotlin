package com.example.sicuan.dashboard.setting

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
import com.example.sicuan.model.request.SetPasswordRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException // ✅ Import yang benar

class SetPasswordActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSimpan: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_password)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        etPassword = findViewById(R.id.etPasswordBaru)
        etConfirmPassword = findViewById(R.id.etConfirmPasswordBaru)
        btnSimpan = findViewById(R.id.btnUbahPassword)
        progressBar = findViewById(R.id.progressBar)

        btnSimpan.setOnClickListener {
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            changePassword(password, confirmPassword)
        }
    }

    private fun changePassword(password: String, confirmPassword: String) {
        btnSimpan.isEnabled = false
        progressBar.visibility = View.VISIBLE

        val request = SetPasswordRequest(
            password = password,
            confirmPassword = confirmPassword
        )

        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.patchPassword(request)
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@SetPasswordActivity, "Password berhasil diubah", Toast.LENGTH_SHORT).show()

                    // ✅ Intent ke MainActivity
                    val intent = Intent(this@SetPasswordActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@SetPasswordActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SetPasswordActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSimpan.isEnabled = true
            }
        }
    }

}
