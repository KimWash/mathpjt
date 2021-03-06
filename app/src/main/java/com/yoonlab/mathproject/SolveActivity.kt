package com.yoonlab.mathproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.android.synthetic.main.activity_solve.*
import java.io.InputStream
import java.net.URL


var problemAns: Int? = null
var problempoint: Int = 0
var problemsolver: Int = 0
var mContext_Solve: Context? = null
var uuidl2: String? = null

class SolveActivity : AppCompatActivity() {
    private lateinit var mRewardedAd: RewardedAd
    private var mIsLoading = false

    private fun setThings1(): Int { //UUID 불러오기
        val getHeart = GetInform(uuidl2, 3)
        val heart = getHeart.execute().get() as String
        val heart10 = Integer.parseInt(heart)
        if (heart10 == 0) {
            heart11.visibility = View.INVISIBLE
            heart12.visibility = View.INVISIBLE
            heart13.visibility = View.INVISIBLE
            heart14.visibility = View.INVISIBLE
            heart15.visibility = View.INVISIBLE

        } else if (heart10 == 1) {
            heart11.visibility = View.VISIBLE
            heart12.visibility = View.INVISIBLE
            heart13.visibility = View.INVISIBLE
            heart14.visibility = View.INVISIBLE
            heart15.visibility = View.INVISIBLE
        } else if (heart10 == 2) {
            heart11.visibility = View.VISIBLE
            heart12.visibility = View.VISIBLE
            heart13.visibility = View.INVISIBLE
            heart14.visibility = View.INVISIBLE
            heart15.visibility = View.INVISIBLE

        } else if (heart10 == 3) {
            heart11.visibility = View.VISIBLE
            heart12.visibility = View.VISIBLE
            heart13.visibility = View.VISIBLE
            heart14.visibility = View.INVISIBLE
            heart15.visibility = View.INVISIBLE

        } else if (heart10 == 4) {
            heart11.visibility = View.VISIBLE
            heart12.visibility = View.VISIBLE
            heart13.visibility = View.VISIBLE
            heart14.visibility = View.VISIBLE
            heart15.visibility = View.INVISIBLE

        } else if (heart10 == 5) {
            heart11.visibility = View.VISIBLE
            heart12.visibility = View.VISIBLE
            heart13.visibility = View.VISIBLE
            heart14.visibility = View.VISIBLE
            heart15.visibility = View.VISIBLE
        }
        return heart10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl2 = useruuid?.getString("uuid", null)
        val heart = setThings1()
        getPoints()
        heartplus1.setOnClickListener {
            MobileAds.initialize(this) {}
            loadRewardedAd()
        }
        answer.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                checkAnswer()
                return@OnKeyListener true
            }
            false
        })
        val problemInf = GetProblem(sProblem).execute().get() as Array<*>
        problempoint = (problemInf[0] as String).toInt()
        problemsolver = (problemInf[1] as String).toInt()
        problemAns = (problemInf[3] as String).toInt()
        val problemView = findViewById<ImageView>(R.id.problems)
        val problemUrl = problemInf[4] as String

        val imgExe = setImgaebyUrl(problemUrl, problemView)
        imgExe.execute()
        //어떤 문제를 불러옴??


        submit.setOnClickListener {
            checkAnswer()
            editInf(uuidl2, 7, 0, sProblem)
        }

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
                            this@SolveActivity,
                            "광고가 재생 됩니다",
                            Toast.LENGTH_LONG
                        ).show()
                        showRewardedVideo()
                    }

                    override fun onRewardedAdFailedToLoad(errorCode: Int) {
                        mIsLoading = false
                        Toast.makeText(
                            this@SolveActivity,
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
                                this@SolveActivity,
                                "하트가 충전됩니다",
                                Toast.LENGTH_LONG
                            ).show()
                            setThings1()
                        } else {
                            Toast.makeText(
                                this@SolveActivity,
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
                            this@SolveActivity,
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

    fun getPoints(): Int {
        val getPoint = GetInform(uuidl2, 5)
        val points = getPoint.execute().get() as String
        val points1 = Integer.parseInt(points)
        pointView.text = points1.toString()
        return points1
    }

    class setImgaebyUrl(var url: String, var probView: ImageView): AsyncTask<Void, Void, Bitmap>() {
        override fun doInBackground(vararg params: Void?): Bitmap? {
            val input: InputStream = URL(url).openStream()
            return BitmapFactory.decodeStream(input)
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            probView.setImageBitmap(result)
        }
    }


    private fun checkAnswer() {
        val useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        val uuid = useruuid.getString("uuid", null)
        if (uuid != null) {
            val editHeart = editInf(uuid.toString(), 3, 0, 1) //UUID 불러오기
            val getHeart = GetInform(uuid, 3)
            val heart = getHeart.execute().get() as String
            val newheart = Integer.parseInt(heart)
            if (newheart > 0) {
                val result = editHeart.execute().get().toString()
                if (result == "success") { //하트를 정상적으로 수정했을 때
                    if (Integer.parseInt(
                            answer.text.toString()
                        ) == problemAns
                    ) { //DB상의 답과 입력한 답이 일치할때
                        JoinActivity.dispToast(this, "정답입니다!")
                        val solvedProb = editInf(uuid, 7, 0, sProblem)
                        val result = solvedProb.execute().get()
                        if (result == "success") {

                        } else {
                            JoinActivity.dispToast(
                                this,
                                "무언가 잘못되어 소중한 답정보를 전송하지 못했습니다.. :( 개발자에게 문의해주세요!"
                            )
                        }
                        val homepage = Intent(this@SolveActivity, MainActivity::class.java)
                        startActivity(homepage)
                    } else {
                        JoinActivity.dispToast(this, "답이 틀렸네요..")
                        finish()
                        val homepage = Intent(this@SolveActivity, MainActivity::class.java)
                        startActivity(homepage)
                    }
                } else {  //하트를 수정하지 못했을 때
                    JoinActivity.dispToast(this, "서버 오류입니다. 개발자에게 문의해주세요.")
                    val homepage = Intent(this@SolveActivity, MainActivity::class.java)
                    startActivity(homepage)
                }
            } else if (newheart <= 0) {
                JoinActivity.dispToast(this, "하트가 모자랍니다. 충전해주세요!")
            }
        } else {
            JoinActivity.dispToast(this, "유저 정보를 불러오지 못했습니다. 개발자에게 문의해주세요.")
            val homepage = Intent(this@SolveActivity, MainActivity::class.java)
            startActivity(homepage)
        }
    }



}