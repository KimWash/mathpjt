package com.yoonlab.mathproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_store.*


class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        val intIntent = Intent(this@StoreActivity, MainActivity::class.java)
        home.setOnClickListener{View -> startActivity(intIntent)}
    }


}
