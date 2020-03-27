package com.yoonlab.mathproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.instacart.library.truetime.TrueTime

class SplashActivity: AppCompatActivity()   {
    private val SPLASH_TIME = 1500


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread(Runnable {
            TrueTime.build().initialize();
        }).start()

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val status = networkInfo != null && networkInfo.isConnected
        if (status == false) {
            val alert_confirm =
                AlertDialog.Builder(this)
            alert_confirm.setMessage("인터넷에 연결되어있지 않습니다. 확인 후 다시 이용 바랍니다.").setCancelable(false)
                .setPositiveButton(
                    "확인"
                ) { dialog, which ->
                    // 'YES'
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
            val alert = alert_confirm.create()
            alert.show()
        }
        else{
            var prefs: SharedPreferences = getSharedPreferences("Pref", MODE_PRIVATE)
            var isFirstRun = prefs.getBoolean("isFirstRun", true)
            if(isFirstRun){
                val hander = Handler()
                hander.postDelayed({
                    startActivity(Intent(application, IntroduceActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    this@SplashActivity.finish()

                }, SPLASH_TIME.toLong())
            }
            else if(!isFirstRun){
                val hander = Handler()
                hander.postDelayed({
                    startActivity(Intent(application, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    this@SplashActivity.finish()

                }, SPLASH_TIME.toLong())
            }

        }



    }


    override fun onBackPressed(){
    }
}