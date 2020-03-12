package com.yoonlab.mathproject

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.yoonlab.mathproject.RewardActivity
import kotlinx.android.synthetic.main.activity_main.*


const val AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

class RewardActivity : AppCompatActivity() {
    private lateinit var mRewardedAd: RewardedAd
    private var mIsLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        var uuid = useruuid.getString("uuid", null)
        val heart = getHeart(uuid)
        var heartnumber = heart.execute().get() as Int
        if (heartnumber > 5) {
            Toast.makeText(this@RewardActivity, "하트가 너무 많습니다", Toast.LENGTH_LONG).show()
        } else {
            MobileAds.initialize(this) {}
            loadRewardedAd()
        }
    }

    fun loadRewardedAd() {
        if (!(::mRewardedAd.isInitialized) || !mRewardedAd.isLoaded) {
            mIsLoading = true
            mRewardedAd = RewardedAd(this, AD_UNIT_ID)
            mRewardedAd.loadAd(
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onRewardedAdLoaded() {
                        mIsLoading = false
                        Toast.makeText(
                            this@RewardActivity,
                            "광고가 재생 됩니다",
                            Toast.LENGTH_LONG
                        ).show()
                        showRewardedVideo()
                    }

                    override fun onRewardedAdFailedToLoad(errorCode: Int) {
                        mIsLoading = false
                        Toast.makeText(
                            this@RewardActivity,
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
                        Toast.makeText(
                            this@RewardActivity,
                            "하트가 충전됩니다",
                            Toast.LENGTH_LONG
                        ).show()
                        val editheart = editHeart(uuid, 1, 1)
                        editheart.execute()
                            .get()
                    }

                    override fun onRewardedAdClosed() {
                        loadRewardedAd()
                    }

                    override fun onRewardedAdFailedToShow(errorCode: Int) {
                        Toast.makeText(
                            this@RewardActivity,
                            "오류",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onRewardedAdOpened() {
                    }
                })

        }
}}






