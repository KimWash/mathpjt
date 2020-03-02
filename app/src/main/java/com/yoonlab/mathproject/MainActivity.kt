package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var loginButton = findViewById<Button>(R.id.loginButton) // 버튼 선언
        loginButton.setOnClickListener { View -> val loginIntent = Intent(this@MainActivity, LoginActivity::class.java); startActivity(loginIntent) } //버튼클릭 리스너 선언
    }

}
