package com.yoonlab.mathproject


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_main.*


var useruuid: SharedPreferences? = null
var uuidl: String? = null
var mBackWait: Long = 0


class MainActivity : AppCompatActivity() {
    private lateinit var mRewardedAd: RewardedAd
    private var mIsLoading = false
    private lateinit var mInterstitialAd: InterstitialAd


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
            var title = "수포자"
            var content = "하트가 가득찼습니다"
            var builder =
                NotificationCompat.Builder(this, "Notification")
                    .setSmallIcon(R.mipmap.fbion)
                    .setContentTitle(title).setContentText(content).setAutoCancel(true)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            NotificationManagerCompat. from (this).notify(10, builder.build())
            heart1.visibility = View.VISIBLE
            heart2.visibility = View.VISIBLE
            heart3.visibility = View.VISIBLE
            heart4.visibility = View.VISIBLE
            heart5.visibility = View.VISIBLE

        }
        return hearts
    }


    override fun onBackPressed() {
        // 뒤로가기 버튼 클릭
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                System.exit(0)
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }
        } else {
            finish() //액티비티 종료
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl = useruuid?.getString("uuid", null)
        nightMode()
        setContentView(R.layout.activity_main)
        val getPoint = getInf(uuidl,5)
        var points = getPoint.execute().get() as String
        point2.setText(points)
        val getNick = getInf(uuidl, 0)
        val name = getNick.execute().get() as String
        nickname.setText(name)
        //전면광고
        MobileAds.initialize(this) { "ca-app-pub-4544671315865800/6106624378" }
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4544671315865800/6106624378"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        //시그마 갯수 이미지로 띄우는 부분
        val hearts = setThings()
        //val solvepage = Intent(this@MainActivity, SelectActivity::class.java)
        val storepage = Intent(this@MainActivity, StoreActivity::class.java)
        val settingActivity = Intent(this@MainActivity, SettingsActivity::class.java)
        heartplus.setOnClickListener {
            if (hearts >= 5) {
                Toast.makeText(this@MainActivity, "하트가 최대입니다!", Toast.LENGTH_LONG).show()
            } else {
                MobileAds.initialize(this) {}
                loadRewardedAd()
            }
        }
        //solve.setOnClickListener { View -> startActivity(solvepage) }
        store.setOnClickListener { View -> startActivity(storepage) }
        settingbutton.setOnClickListener { View -> startActivity(settingActivity) }
    }

    fun loadRewardedAd() {
        if (!(::mRewardedAd.isInitialized) || !mRewardedAd.isLoaded) {
            mIsLoading = true
            mRewardedAd = RewardedAd(this, "ca-app-pub-4544671315865800/1491510376")
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
                        val changeheart = editInf(uuidl, 3, 1, 1)
                        val result = changeheart.execute().get()
                        if (result.toString() == "success") {
                            Toast.makeText(
                                this@MainActivity,
                                "하트가 충전됩니다",
                                Toast.LENGTH_LONG
                            ).show()
                            val refresh = Intent(this@MainActivity, JoinActivity::class.java)
                            startActivity(refresh)
                            setThings()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "이런, 하트 충전에 오류가 있는 것 같아요. ",
                                Toast.LENGTH_LONG
                            ).show()
                        }

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
