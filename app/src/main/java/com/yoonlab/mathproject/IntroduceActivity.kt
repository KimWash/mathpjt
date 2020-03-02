package com.yoonlab.mathproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.IntroduceActivity

class IntroduceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce)
        var startbutton = findViewById<Button>(R.id.startbutton)
        val intIntent = Intent(this@IntroduceActivity, SignActivity::class.java)
        startbutton.setOnClickListener{View -> startActivity(intIntent)}


    }


}
