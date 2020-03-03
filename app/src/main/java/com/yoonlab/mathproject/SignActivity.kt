package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yoonlab.mathproject.ui.login.LoginActivity

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        var register = findViewById<Button>(R.id.resister)
        var signin = findViewById<Button>(R.id.signbutton)
        var home = findViewById<Button>(R.id.home)
        val registerpage = Intent(this@SignActivity, JoinActivity::class.java)
        register.setOnClickListener{View -> startActivity(registerpage)}
        val signpage = Intent(this@SignActivity, LoginActivity::class.java)
        signin.setOnClickListener{View -> startActivity(signpage)}
        val homebutton = Intent(this@SignActivity, MainActivity::class.java)
        home.setOnClickListener{View -> startActivity(homebutton)}





    }


}