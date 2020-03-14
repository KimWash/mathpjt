package com.yoonlab.mathproject

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*


class SettingsActivity : AppCompatActivity() {
    var useruuid_Setting: SharedPreferences? = null
    var uuidl_Setting:String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar2)
        supportActionBar?.title = "설정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        useruuid_Setting = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl_Setting= useruuid_Setting?.getString("uuid", null)

        if (uuidl_Setting != null){ //UUID가 빈칸이 아니면
            loginout.setText("로그아웃")
        }
        else{
            loginout.setText("로그인")
        }
        var loginout = findViewById<Button>(R.id.loginout)
        loginout.setOnClickListener { loginoutClick() }

    }

    private fun loginoutClick() {
        if (uuidl_Setting != null){ //UUID가 빈칸이 아니면 -> 로그인상태
            var uuideditor_Setting: SharedPreferences.Editor = useruuid_Setting!!.edit()
            uuideditor_Setting.putString("uuid", "")
            uuideditor_Setting.commit()
            var IntAct = Intent(this@SettingsActivity, IntroduceActivity::class.java)
            IntAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            IntAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(IntAct)
        }
        else{
           var IntAct = Intent(this@SettingsActivity, IntroduceActivity::class.java)
           IntAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            IntAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(IntAct)
        }
    }

}