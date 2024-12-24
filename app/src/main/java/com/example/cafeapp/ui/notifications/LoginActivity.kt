package com.example.cafeapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cafeapp.MainActivity
import com.example.cafeapp.R
import com.example.cafeapp.data.UserSession
import com.example.cafeapp.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "입력칸을 모두 채워주세요", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun login(username: String, password: String) {
        db.collection("User")
            .document(username)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedPassword = document.getString("password")

                    // 로그인 성공
                    if (storedPassword == password) {
                        val point = document.getLong("point") ?: 0

                        // 유저 정보를 저장합니다
                        UserSession.username = username
                        UserSession.point = point

                        //로그인 성공시 메인 액티비티로 돌아간다
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "존재하지 않은 아이디입니다", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}