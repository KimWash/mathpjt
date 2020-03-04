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
            //prefs.edit().putBoolean("isFirstRun", false).apply()
        }
    }


    fun nightMode(){
        if (nightModeCheck.isNightModeActive(this) == true) {
            setTheme(R.style.DarkTheme)
        } else if (nightModeCheck.isNightModeActive(this) == false) {
            setTheme(R.style.LightTheme)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        checkFirstRun()
        nightMode()
        setContentView(R.layout.activity_main)


    }

}
