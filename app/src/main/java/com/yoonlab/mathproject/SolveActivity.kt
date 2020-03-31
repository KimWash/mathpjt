package com.yoonlab.mathproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_solve.*
import com.google.android.gms.ads.MobileAds


var problemView: ImageView? = null
var problemAns: Int? = null
var problempoint: Int = 0
var problemsolver: Int = 0
var mContext_Solve: Context? = null
var uuidl2: String? = null

class SolveActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd

    private fun setThings1(): Int { //UUID 불러오기
        val getHeart = getInf(uuidl2, 3)
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
            HeartPlus(heart)
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
        problempoint = problemInf[0] as Int
        problemsolver = problemInf[1] as Int
        problemAns = problemInf[3] as Int?

        //어떤 문제를 불러옴??

        problemView = findViewById<ImageView>(R.id.problems)
        submit.setOnClickListener {
            checkAnswer()
            editInf(uuidl2, 7, 0, sProblem)
        }

    }
    private fun HeartPlus(heart:Int){
        MobileAds.initialize(this, "ca-app-pub-4544671315865800/9374767616")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4544671315865800/9374767616"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        if (heart >= 5) {
            Toast.makeText(this@SolveActivity, "하트가 최대입니다!", Toast.LENGTH_LONG).show()
        } else {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
                mInterstitialAd.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        val changeheart = editInf(uuidl2, 3, 1, 1)
                        val result = changeheart.execute().get()
                        if (result.toString() == "success") {
                            Toast.makeText(
                                this@SolveActivity,
                                "하트가 충전됩니다",
                                Toast.LENGTH_LONG
                            ).show()
                            val refresh = Intent(this@SolveActivity, SolveActivity::class.java)
                            startActivity(refresh)
                        }

                    }
                    override fun onAdFailedToLoad(errorCode: Int) {
                        Toast.makeText(
                            this@SolveActivity,
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

    fun getPoints(): Int {
        val getPoint = getInf(uuidl2, 5)
        val points = getPoint.execute().get() as String
        val points1 = Integer.parseInt(points)
        pointView.setText(points1.toString())
        return points1
    }


    private fun checkAnswer() {
        val useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        val uuid = useruuid.getString("uuid", null)
        if (uuid != null) {
            val editHeart = editInf(uuid.toString(), 3, 0, 1) //UUID 불러오기
            val getHeart = getInf(uuid, 3)
            val heart = getHeart.execute().get() as Int
            if (heart > 0) {
                val result = editHeart.execute().get().toString()
                if (result == "success") { //하트를 정상적으로 수정했을 때
                    if (Integer.parseInt(
                            answer.getText().toString()
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
            } else if (heart <= 0) {
                JoinActivity.dispToast(this, "하트가 모자랍니다. 충전해주세요!")
            }
        } else {
            JoinActivity.dispToast(this, "유저 정보를 불러오지 못했습니다. 개발자에게 문의해주세요.")
            val homepage = Intent(this@SolveActivity, MainActivity::class.java)
            startActivity(homepage)
        }
    }



}