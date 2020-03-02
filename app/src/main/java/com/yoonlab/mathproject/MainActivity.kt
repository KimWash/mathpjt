package com.yoonlab.mathproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    fun checkFirstRun(){
        var prefs:SharedPreferences = getSharedPreferences("Pref", MODE_PRIVATE)
        var isFirstRun = prefs.getBoolean("isFirstRun", true)
        if(isFirstRun){
            val intIntent = Intent(this@MainActivity, IntroduceActivity::class.java)
            startActivity(intIntent)
            prefs.edit().putBoolean("isFirstRun", false).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkFirstRun()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loginButton = findViewById<Button>(R.id.loginButton) // 버튼 선언
        loginButton.setOnClickListener { View -> val loginIntent = Intent(this@MainActivity, LoginActivity::class.java); startActivity(loginIntent) } //버튼클릭 리스너 선언
    }

}
