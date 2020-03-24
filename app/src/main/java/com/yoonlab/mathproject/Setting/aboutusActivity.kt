package com.yoonlab.mathproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class aboutusActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)

        val website = findViewById<TextView>(R.id.website)
        website.setOnClickListener {
            val websiteIntent = Intent(Intent.ACTION_VIEW)
            websiteIntent.setData(Uri.parse("https://yoon-lab.xyz"))
            startActivity(websiteIntent)
        }
        val email = findViewById<TextView>(R.id.contractus)
        email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.setType("message/rfc822")
            val emails:Array<String> = arrayOf("support@yoon-lab.xyz")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emails)
            startActivity(emailIntent)
        }
    }
}