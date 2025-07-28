package com.example.sicuan.logreg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sicuan.dashboard.home.MainActivity
import com.example.sicuan.R
import com.example.sicuan.api.ApiClient
import com.example.sicuan.model.request.LoginRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val daftarLink = findViewById<TextView>(R.id.tvDaftar)
        val lupaPassword = findViewById<TextView>(R.id.tvLupaPassword)

        daftarLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        lupaPassword.setOnClickListener {
            startActivity(Intent(this, LupaActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = LoginRequest(email, password)
                val response = ApiClient.instance.loginUser(request)

                withContext(Dispatchers.Main) {
                    Log.d("LoginActivity", "Response code: ${response.code()}")
                    Log.d("LoginActivity", "Response body: ${response.body()}")

                    if (response.isSuccessful && response.body()?.success == true) {
                        val responseData = response.body()?.data
                        val token = responseData?.access_token
                        val username = responseData?.username

                        Log.d("LoginActivity", "Token received: $token")

                        if (!token.isNullOrEmpty()) {
                            // ✅ PERBAIKI: Gunakan nama dan key yang sama dengan ApiClient
                            val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
                            sharedPref.edit()
                                .putString("token", token) // key "token" sesuai ApiClient
                                .putString("username", username)
                                .putString("email", email)
                                .apply()

                            Log.d("LoginActivity", "Token saved successfully")

                            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()

                            // Pindah ke Dashboard
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("USERNAME", username)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Token tidak ditemukan dalam response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMessage = response.body()?.message ?: "Login gagal. Cek email/password."
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginActivity", "Login error: ${e.message}", e)
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}