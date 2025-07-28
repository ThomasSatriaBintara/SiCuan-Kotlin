package com.example.sicuan.logreg

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sicuan.api.ApiClient
import com.example.sicuan.R
import com.example.sicuan.model.request.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.inputUsername)
        val etEmail = findViewById<EditText>(R.id.inputEmail)
        val etPassword = findViewById<EditText>(R.id.inputPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.REinputPassword)
        val etNamaUsaha = findViewById<EditText>(R.id.inputNamaUsaha)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val loginLink = findViewById<TextView>(R.id.tvMasuk)

        // Event saat tombol daftar ditekan
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val namaUsaha = etNamaUsaha.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak sama!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(
                email = email,
                username = username,
                password = password,
                confirmPassword = confirmPassword,
                nama_usaha = namaUsaha
            )

            registerUser(request)
        }

        // Link ke halaman login
        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser(request: RegisterRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.registerUser(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
