package com.yoonlab.mathproject

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.dclick.ads.AdManager
import io.dclick.ads.interstitial.InterstitialAd
import io.dclick.ads.listeners.InterstitialAdListener

class InterstitialAdsActivity : Activity() {
    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdListener: InterstitialAdListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        interstitialAdListener = object : InterstitialAdListener {
            override fun onAdSelected(interstitialAd: InterstitialAd) {
                // Ad Selected
                this@InterstitialAdsActivity.interstitialAd = interstitialAd
            }

            override fun onAdLoaded() {
                // Ad Load Success
                toast("안녕히 가십시오")
                interstitialAd!!.show()
                finishAffinity()
            }

            override fun onAdClosed() {
                AdManager.loadInterstitialAd(this@InterstitialAdsActivity, interstitialAdListener)
            }

            override fun onAdFailed() {
                // Ad Load Failed
                toast("개발자에게 오류를 보고하세요")
            }
        }
        AdManager.loadInterstitialAd(this, interstitialAdListener)
        interstitialAd!!.show()
    }

    override fun onDestroy() {
        if (interstitialAd != null) interstitialAd!!.onDestroy()
        super.onDestroy()
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}