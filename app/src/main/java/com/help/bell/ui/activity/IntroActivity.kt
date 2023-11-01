package com.help.bell.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.help.bell.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        findViewById<Button>(R.id.btn_intro).setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
            finish()
        }
    }
}