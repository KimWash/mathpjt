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
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.back_press.*
import timber.log.Timber




var useruuid: SharedPreferences? = null
var uuidl: String? = null


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd


    fun nightMode() {
        if (nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.DarkTheme)
        } else if (!nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.LightTheme)
        }
    }

    private fun setThings(): Int {
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
        setContentView(R.layout.activity_main)
        //플레이스토어 업데이트 확인
        checkForAppUpdate()
        //update_heart()
        //시작할때 필수함수 (첫실행감지, 야간모드 전환)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl = useruuid?.getString("uuid", null)
        nightMode()
        setContentView(R.layout.activity_main)
        val getPoint = getInf(uuidl, 5)
        val points = getPoint.execute().get() as String
        point2.text = points
        val getNick = getInf(uuidl, 0)
        val name = getNick.execute().get() as String
        nickname.text = name

        //시그마 갯수 이미지로 띄우는 부분
        val hearts = setThings()
        val selectpage = Intent(this@MainActivity, SelectActivity::class.java)
        val storepage = Intent(this@MainActivity, StoreActivity::class.java)
        val settingActivity = Intent(this@MainActivity, SettingsActivity::class.java)
        heartplus.setOnClickListener {
            plusmain(hearts)
        }
        solve.setOnClickListener { startActivity(selectpage) }
        store.setOnClickListener { startActivity(storepage) }
        settingbutton.setOnClickListener { startActivity(settingActivity) }
    }
    private fun plusmain(hearts:Int){
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
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
        builder.show()
        out_button.setOnClickListener() {
            finishAffinity()
        }
        out_button_cancel.setOnClickListener() {
            val goback = Intent(this@MainActivity,MainActivity::class.java)
            startActivity(goback)
        }
    }

    //업데이트 체크
    val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
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
