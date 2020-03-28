package com.yoonlab.mathproject

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.yoonlab.mathproject.Setting.NoticeActivity
import kotlinx.android.synthetic.main.activity_setting.*
import org.w3c.dom.Text


class SettingsActivity : AppCompatActivity() {
    var useruuid_Setting: SharedPreferences? = null
    var uuidl_Setting:String? = null

    fun getVersionInfo(context: Context) : String{
        val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val version = info.versionName
        return version
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
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
        var aboutus = findViewById<Button>(R.id.about_us)
        aboutus.setOnClickListener {
            var aboutusAct = Intent(this@SettingsActivity, aboutusActivity::class.java)
            startActivity(aboutusAct)
        }
        notice.setOnClickListener{
            var noticepage = Intent(this@SettingsActivity,NoticeActivity::class.java)
            startActivity(noticepage)
        }
        var sendreport = findViewById<TextView>(R.id.sendReport)
        sendreport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.setType("message/rfc822")
            val emails:Array<String> = arrayOf("support@yoon-lab.xyz")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emails)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "수포자 앱 오류 신고")
            var getInf = getInf(uuidl, 0)
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                "어플리케이션 버전: " + getVersionInfo(this) +
                        "\n기기 제조사: " + Build.BRAND +
                        "\n기기 모델명: " + Build.MODEL +
                        "\n소프트웨어 빌드번호: " + Build.VERSION.INCREMENTAL +
                        "\n안드로이드 버전: " + Build.VERSION.RELEASE + " (API LEVEL: " + Build.VERSION.SDK_INT +
                        ")\nUUID: " + uuidl.toString() +
                        "\nID: " + getInf.execute().get() as String +
                        "\n문의 내용: "
            )
            startActivity(emailIntent)
        }
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