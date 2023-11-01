package com.help.bell.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.help.bell.databinding.ActivityLoginBinding
import com.help.bell.util.SharedManager

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val auth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        autoLogin()

        val emailSaved = SharedManager.getString(this, "email", "")
        val pwSaved = SharedManager.getString(this, "pw", "")

        if (emailSaved.isNotEmpty()) binding.etUsername.setText(emailSaved)
        if (pwSaved.isNotEmpty()) binding.etPassword.setText(pwSaved)

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            signIn(email, password)
        }

        // "회원가입하기" 클릭하면 SignupActivity로 이동
        binding.tvSignUpLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }

    fun autoLogin(){
        binding.autoLoginSwitch.isChecked = SharedManager.getInt(this@LoginActivity, "autologin", 0) == 1
        binding.autoLoginSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 자동 로그인을 활성화하는 코드
                SharedManager.putInt(this@LoginActivity, "autologin", 1)
            } else {
                // 자동 로그인을 비활성화하는 코드
                SharedManager.putInt(this@LoginActivity, "autologin", 0)
            }
        }

        if (auth.currentUser != null && SharedManager.getInt(this@LoginActivity, "autologin", 0) == 1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, "자동로그인 되었습니다!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //로그인에 성공한 경우 메인 화면으로 이동
                    SharedManager.putString(this, "email", email)
                    SharedManager.putString(this, "pw", password)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    //로그인에 실패한 경우 Toast 에러
                    Toast.makeText(this, "존재하지 않는 계정입니다. 아이디/비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                }
            }
    }

}