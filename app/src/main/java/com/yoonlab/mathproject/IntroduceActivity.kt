package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class IntroduceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce)
        val startbutton = findViewById<Button>(R.id.startbutton)
        val intIntent = Intent(this@IntroduceActivity, SignActivity::class.java)
        startbutton.setOnClickListener{ startActivity(intIntent)}


    }


}
