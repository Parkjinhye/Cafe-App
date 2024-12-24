package com.example.cafeapp.ui.myPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeapp.databinding.ActivitySignUpBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                signUp(username, password)
            } else {
                Toast.makeText(this, "입력칸을 모두 채워주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(username: String, password: String) {
        val user = hashMapOf(
            "password" to password,
            "point" to 0 // 초기 포인트 값 설정
        )

        db.collection("User")
            .document(username)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "아이디가 중복되었습니다", Toast.LENGTH_SHORT).show()
                } else {
                    // 신규 사용자 추가
                    db.collection("User")
                        .document(username)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "회원가입 성공! 로그인을 해주세요", Toast.LENGTH_SHORT).show()
                            // 회원가입 성공 후 로그인 화면으로 이동
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}