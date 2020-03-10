package com.yoonlab.mathproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.IntroduceActivity

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*

class IntroduceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce)
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111")
        bannerad.loadAd(AdRequest.Builder().build())
        var startbutton = findViewById<Button>(R.id.startbutton)
        val intIntent = Intent(this@IntroduceActivity, SignActivity::class.java)
        startbutton.setOnClickListener{View -> startActivity(intIntent)}


    }


}
