package com.yoonlab.mathproject


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
//import com.yoonlab.mathproject.updateHeart.Companion.update_heart
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.gohome
import kotlinx.android.synthetic.main.activity_main.learn
import kotlinx.android.synthetic.main.activity_main.ranking
import kotlinx.android.synthetic.main.activity_main.solve
import kotlinx.android.synthetic.main.activity_main.store
import kotlinx.android.synthetic.main.activity_store.*
import timber.log.Timber


var useruuid: SharedPreferences? = null
var uuidl: String? = null


class MainActivity : AppCompatActivity() {
    private lateinit var mRewardedAd: RewardedAd
    private var mIsLoading = false
    private lateinit var mInterstitialAd: InterstitialAd

    fun nightMode() {
        if (nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.DarkTheme)
        } else if (!nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.LightTheme)
        }
    }

    private fun setThings(): Int {
        val getHeart = GetInform(uuidl, 3)
        var mainsigma = Integer.parseInt(getHeart.execute().get() as String)
        when (mainsigma) {
            0 -> {
                mainheart1.visibility = View.INVISIBLE
                mainheart2.visibility = View.INVISIBLE
                mainheart3.visibility = View.INVISIBLE
                mainheart4.visibility = View.INVISIBLE
                mainheart5.visibility = View.INVISIBLE
            }
            1 -> {
                mainheart1.visibility = View.VISIBLE
                mainheart2.visibility = View.INVISIBLE
                mainheart3.visibility = View.INVISIBLE
                mainheart4.visibility = View.INVISIBLE
                mainheart5.visibility = View.INVISIBLE
            }
            2 -> {
                mainheart1.visibility = View.VISIBLE
                mainheart2.visibility = View.VISIBLE
                mainheart3.visibility = View.INVISIBLE
                mainheart4.visibility = View.INVISIBLE
                mainheart5.visibility = View.INVISIBLE
            }
            3 -> {
                mainheart1.visibility = View.VISIBLE
                mainheart2.visibility = View.VISIBLE
                mainheart3.visibility = View.VISIBLE
                mainheart4.visibility = View.INVISIBLE
                mainheart5.visibility = View.INVISIBLE
            }
            4 -> {
                mainheart1.visibility = View.VISIBLE
                mainheart2.visibility = View.VISIBLE
                mainheart3.visibility = View.VISIBLE
                mainheart4.visibility = View.VISIBLE
                mainheart5.visibility = View.INVISIBLE
            }
            5 -> {
                mainheart1.visibility = View.VISIBLE
                mainheart2.visibility = View.VISIBLE
                mainheart3.visibility = View.VISIBLE
                mainheart4.visibility = View.VISIBLE
                mainheart5.visibility = View.VISIBLE
            }
        }

        return mainsigma
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //플레이스토어 업데이트 확인
        checkForAppUpdate()
        //update_heart()
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl = useruuid?.getString("uuid", null)
        nightMode()
        setContentView(R.layout.activity_main)
        val getPoint = GetInform(uuidl, 5)
        val points = getPoint.execute().get() as String
        point2.text = points
        val getNick = GetInform(uuidl, 0)
        val name = getNick.execute().get() as String
        nickname.text = name

        //시그마 갯수 이미지로 띄우는 부분
        val mainheart = setThings()
        val problemselect = Intent(this@MainActivity, SelectActivity::class.java)
        val storepage = Intent(this@MainActivity, StoreActivity::class.java)
        val home = Intent(this@MainActivity, MainActivity::class.java)
        val rank = Intent(this@MainActivity, RankActivity::class.java)
        val learning = Intent(this@MainActivity, LearnActivity::class.java)
        val settingActivity = Intent(this@MainActivity, SettingsActivity::class.java)
        solve.setOnClickListener {
            finish()
            startActivity(problemselect)
        }
        store.setOnClickListener {
            finish()
            startActivity(storepage)
        }
        gohome.setOnClickListener {
            finish()
            startActivity(home)
        }
        learn.setOnClickListener {
            finish()
            startActivity(learning)
        }
        ranking.setOnClickListener {
            finish()
            startActivity(rank)
        }
        heartplus.setOnClickListener {
            MobileAds.initialize(this) {}
            loadRewardedAd()
        }
        solve.setOnClickListener { startActivity(problemselect) }
        store.setOnClickListener { startActivity(storepage) }
        settingbutton.setOnClickListener { startActivity(settingActivity) }
    }

    fun loadRewardedAd() {
        if (!(::mRewardedAd.isInitialized) || !mRewardedAd.isLoaded) {
            mIsLoading = true
            mRewardedAd = RewardedAd(this, "ca-app-pub-4544671315865800/1118034886")
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
                            setThings()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "광고 재생에 문제가 있습니다",
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


    override fun onBackPressed() {
        MobileAds.initialize(this, "ca-app-pub-4544671315865800/1289253832")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4544671315865800/1289253832"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.show()
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Toast.makeText(
                        this@MainActivity,
                        "감사합니다",
                        Toast.LENGTH_LONG
                    ).show()
                    finishAffinity()
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
        }
        else{
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }
    }

    //업데이트 체크
    val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(
            this
        )
    }
    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(installState: InstallState) {
                when {
                    installState.installStatus() == InstallStatus.DOWNLOADED -> popupSnackbarForCompleteUpdate()
                    installState.installStatus() == InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(
                        this
                    )
                    else -> Timber.d(
                        "InstallStateUpdatedListener: state: %s",
                        installState.installStatus()
                    )
                }
            }
        }
    }

    private fun checkForAppUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                try {
                    val installType = when {
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> AppUpdateType.FLEXIBLE
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> AppUpdateType.IMMEDIATE
                        else -> null
                    }
                    if (installType == AppUpdateType.FLEXIBLE) appUpdateManager.registerListener(
                        appUpdatedListener
                    )

                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        installType!!,
                        this,
                        APP_UPDATE_REQUEST_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "App Update failed, please try again on the next app launch.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            findViewById(R.id.drawer_layout),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("RESTART") { appUpdateManager.completeUpdate() }
        snackbar.setActionTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorAccent
            )
        )
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->

                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }

                //Check if Immediate update is required
                try {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            APP_UPDATE_REQUEST_CODE
                        )
                    }
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }

            }

    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1991
    }


}
