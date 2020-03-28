package com.yoonlab.mathproject


import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
import com.yoonlab.mathproject.updateHeart.Companion.update_heart
import com.google.android.gms.ads.*
=======
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
>>>>>>> parent of ef6d33a... 하트자동충전(개발중)
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.back_press.*


var useruuid: SharedPreferences? = null
var uuidl: String? = null


class MainActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var mNativeExpressAdView :NativeExpressAdView


    fun nightMode() {
        if (nightModeCheck.isNightModeActive(this) == true) {
            setTheme(R.style.DarkTheme)
        } else if (nightModeCheck.isNightModeActive(this) == false) {
            setTheme(R.style.LightTheme)
        }
    }

    fun setThings(): Int {
        val getHeart = getInf(uuidl, 3)
        var hearts = Integer.parseInt(getHeart.execute().get() as String)
        if (hearts == 0) {
            heart1.visibility = View.INVISIBLE
            heart2.visibility = View.INVISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        } else if (hearts == 1) {
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.INVISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE
        } else if (hearts == 2) {
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        } else if (hearts == 3) {
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        } else if (hearts == 4) {
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.VISIBLE
            heart5.visibility = View.INVISIBLE

        } else if (hearts == 5) {
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.VISIBLE
            heart5.visibility = View.VISIBLE

        }
        return hearts
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl = useruuid?.getString("uuid", null)
        nightMode()
        setContentView(R.layout.activity_main)
        val getPoint = getInf(uuidl, 5)
        var points = getPoint.execute().get() as String
        point2.text = points
        val getNick = getInf(uuidl, 0)
        val name = getNick.execute().get() as String
        nickname.text = name


        //시그마 갯수 이미지로 띄우는 부분
        val hearts = setThings()
        val solvepage = Intent(this@MainActivity, SelectActivity::class.java)
        val storepage = Intent(this@MainActivity, StoreActivity::class.java)
        val settingActivity = Intent(this@MainActivity, SettingsActivity::class.java)
        heartplus.setOnClickListener {
            HeartPlusMain(hearts)
        }
        solve.setOnClickListener { View -> startActivity(solvepage) }
        store.setOnClickListener { View -> startActivity(storepage) }
        settingbutton.setOnClickListener { View -> startActivity(settingActivity) }
    }
    fun HeartPlusMain(hearts:Int){
        MobileAds.initialize(this, "ca-app-pub-4544671315865800/9374767616")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4544671315865800/9374767616"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        if (hearts >= 5) {
            Toast.makeText(this@MainActivity, "하트가 최대입니다!", Toast.LENGTH_LONG).show()
        } else {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                mInterstitialAd.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        val changeheart = editInf(uuidl2, 3, 1, 1)
                        val result = changeheart.execute().get()
                        if (result.toString() == "success") {
                            Toast.makeText(
                                this@MainActivity,
                                "하트가 충전됩니다",
                                Toast.LENGTH_LONG
                            ).show()
                            val refresh = Intent(this@MainActivity, MainActivity::class.java)
                            startActivity(refresh)
                        }

                    }

                    override fun onAdFailedToLoad(errorCode: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "광고 재생에 문제가 있습니다",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onAdClosed() {
                        mInterstitialAd.loadAd(AdRequest.Builder().build())
                    }
                }
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }
        }

    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        setContentView(R.layout.back_press)
        val adRequest =
            AdRequest.Builder().build()
        adView.loadAd(adRequest)
        builder.show()
        out_button.setOnClickListener() {
            finishAffinity()
        }
        out_button_cancel.setOnClickListener() {
            val goback = Intent(this@MainActivity,MainActivity::class.java)
            startActivity(goback)
        }
    }

}
