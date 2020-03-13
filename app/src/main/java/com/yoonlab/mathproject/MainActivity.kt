package com.yoonlab.mathproject

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

//광고
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem

class MainActivity : AppCompatActivity() {
    private lateinit var mRewardedAd: RewardedAd
    private var mIsLoading = false

    fun setHeart(hearts:Int){
        if (hearts == 0){
            heart1.visibility = View.INVISIBLE
            heart2.visibility = View.INVISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        }
        else if(hearts == 1){
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.INVISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE
        }
        else if(hearts ==2){
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.INVISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        }
        else if(hearts == 3){
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.INVISIBLE
            heart5.visibility = View.INVISIBLE

        }
        else if(hearts == 4){
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.VISIBLE
            heart5.visibility = View.INVISIBLE

        }
        else if(hearts == 5){
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.VISIBLE
            heart5.visibility = View.VISIBLE

        }
    }

    fun nightMode(){
        if (nightModeCheck.isNightModeActive(this) == true) {
            setTheme(R.style.DarkTheme)
        } else if (nightModeCheck.isNightModeActive(this) == false) {
            setTheme(R.style.LightTheme)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        nightMode()
        setContentView(R.layout.activity_main)
        //시그마 갯수 이미지로 띄우는 부분
        var useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        var uuid = useruuid.getString("uuid", null)
        val getPoint = getPoint(uuid)
        var points = getPoint.execute().get() as Int
        point.setText(points.toString())
        val getHeart = getHeart(uuid)
        var hearts = getHeart.execute().get() as Int
        Log.i("hearts", hearts.toString())
        setHeart(hearts)

        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111")
        bannerad.loadAd(AdRequest.Builder().build())
        settingbutton.setOnClickListener{showSettingPop()}
        val solvepage = Intent(this@MainActivity, SolveActivity::class.java)
        val storepage = Intent(this@MainActivity, StoreActivity::class.java)
        heartplus.setOnClickListener{
            if (hearts > 5) {
                Toast.makeText(this@MainActivity, "하트가 너무 많습니다", Toast.LENGTH_LONG).show()
            }
            else {
                MobileAds.initialize(this) {}
                loadRewardedAd()
            }
        }
        solve.setOnClickListener{View -> startActivity(solvepage)}
        store.setOnClickListener{View -> startActivity(storepage)}
    }
    fun showSettingPop(){
        val pop= getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = pop.inflate(R.layout.activity_setpop, null)
        Toast.makeText(
            this@MainActivity,
            "설정입니다",
            Toast.LENGTH_LONG
        ).show()
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("설정")
            .setNeutralButton("close",null)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
    }
    fun loadRewardedAd() {
        if (!(::mRewardedAd.isInitialized) || !mRewardedAd.isLoaded) {
            mIsLoading = true
            mRewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
            mRewardedAd.loadAd(
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onRewardedAdLoaded() {
                        mIsLoading = false
                        Toast.makeText(
                            this@MainActivity,
                            "광고가 재생 됩니다",
                            Toast.LENGTH_LONG
                        ).show()
                        showRewardedVideo()
                    }

                    override fun onRewardedAdFailedToLoad(errorCode: Int) {
                        mIsLoading = false
                        Toast.makeText(
                            this@MainActivity,
                            "오류가 났습니다",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            )

        }


    }
    fun showRewardedVideo() {
        if (mRewardedAd.isLoaded) {
            mRewardedAd.show(
                this,
                object : RewardedAdCallback() {
                    override fun onUserEarnedReward(
                        rewardItem: RewardItem
                    ) {
                        val changeheart = editHeart(uuid, 1, 1)
                        changeheart.execute().get()
                        Toast.makeText(
                            this@MainActivity,
                            "하트가 충전됩니다",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    override fun onRewardedAdClosed() {
                        Log.d("SSS", "onRewardVideoAdClosed()")
                    }

                    override fun onRewardedAdFailedToShow(errorCode: Int) {
                        Toast.makeText(
                            this@MainActivity,
                            "오류",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onRewardedAdOpened() {
                    }
                }

            )


        }
    }



}
