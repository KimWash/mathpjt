package com.yoonlab.mathproject

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.yoonlab.mathproject.ui.login.LoginActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

//광고
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {


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
        nightMode()
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        bannerad.loadAd(AdRequest.Builder().build())
        var solve = findViewById<Button>(R.id.solve)

        val solvepage = Intent(this@MainActivity, SolveActivity::class.java)
        solve.setOnClickListener{View -> startActivity(solvepage)}
    }



}
