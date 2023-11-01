package com.help.bell.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.help.bell.databinding.ActivitySignupBinding
import com.help.bell.util.SharedManager

class SignupActivity : AppCompatActivity() {

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.etId.text.toString()
            val password = binding.etPw.text.toString()
            val name = binding.etName.text.toString()
            val sex = binding.rgSex.checkedRadioButtonId

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && sex != -1) {
                val sexString = findViewById<RadioButton>(sex).text.toString()
                signUp(email, password, name, sexString)

            } else {
                Toast.makeText(this, "빈칸이 있습니다!", Toast.LENGTH_LONG).show()
            }
        }
    }

    // 회원가입 함수
    fun signUp(email: String, password: String, name: String, sex: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        // 사용자의 UID를 가져옴
                        val userId = user.uid

                        saveUserInfoToDatabase(userId, name, sex, email)

                        SharedManager.putString(this, "email", email)
                        SharedManager.putString(this, "pw", password)
                        SharedManager.putString(this, "uid", userId)
                        SharedManager.putString(this, "name", name)

                        startActivity(Intent(this, LoginActivity::class.java))
                        Toast.makeText(this, "$name 님 환영합니다.", Toast.LENGTH_LONG).show()

                    }
                } else if (task.exception?.message.isNullOrEmpty()) {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "이미 회원가입 된 아이디입니다.", Toast.LENGTH_LONG).show()
                }
            }
    }
    fun saveUserInfoToDatabase(userId: String, name: String, sex: String, email: String) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        // 사용자 정보를 사용자의 UID로 저장
        usersRef.child(userId).child("name").setValue(name)
        usersRef.child(userId).child("sex").setValue(sex)
        usersRef.child(userId).child("email").setValue(email)

    }
}