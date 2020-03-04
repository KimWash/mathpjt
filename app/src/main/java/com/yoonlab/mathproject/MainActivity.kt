package com.yoonlab.mathproject

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.yoonlab.mathproject.ui.login.LoginActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun checkFirstRun(){
        var prefs:SharedPreferences = getSharedPreferences("Pref", MODE_PRIVATE)
        var isFirstRun = prefs.getBoolean("isFirstRun", true)
        if(isFirstRun){
            val intIntent = Intent(this@MainActivity, IntroduceActivity::class.java)
            startActivity(intIntent)
            //prefs.edit().putBoolean("isFirstRun", false).apply() Todo:첫실행 후 가입/로그인 완료되면 isFirstRun을 false로 적용
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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
        settingbutton.setOnClickListener{
            showingSettingPop()
        }

    }
    private fun showingSettingPop(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_setpop,null)
        val textView: TextView = view.findViewById(R.id.settingmenu)
        textView.text = "Test중입니다"

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Setting")
            .setPositiveButton("저장"){dialog, which ->
                Toast.makeText(applicationContext, "Save", Toast.LENGTH_SHORT).show()
            }
        alertDialog.setView(view)
        alertDialog.show()

    }

}
