package com.yoonlab.mathproject

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.SignActivity
import com.yoonlab.mathproject.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        var register = findViewById<Button>(R.id.resister)
        var signin = findViewById<Button>(R.id.signbutton)
        val registerpage = Intent(this@SignActivity, JoinActivity::class.java)
        register.setOnClickListener{View -> startActivity(registerpage)}
        val signpage = Intent(this@SignActivity, LoginActivity::class.java)
        signin.setOnClickListener{View -> startActivity(signpage)}




    }


}