package com.yoonlab.mathproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_solve.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


var problemView: ImageView? = null
var problemAns: Int? = null
var problempoint: Int = 0
var problemsolver: Int = 0
public var mContext_Solve: Context? = null
var uuidl2: String? = null

class SolveActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    private val problemnum =  sProblem

    fun setThings1(): Int { //UUID 불러오기
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
        getProblem(problemnum)

        //어떤 문제를 불러옴??

        problemView = findViewById<ImageView>(R.id.problems)
        submit.setOnClickListener {
            checkAnswer()
            editInf(uuidl2, 7, 0, problemnum!!)
        }

    }
    fun HeartPlus(heart:Int){
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
        var points = getPoint.execute().get() as String
        var points1 = Integer.parseInt(points)
        pointView.setText(points1.toString())
        return points1
    }


    fun checkAnswer() {
        var useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        var uuid = useruuid.getString("uuid", null)
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
                        var solvedProb = editInf(uuid, 7, 0, problemnum)
                        var result = solvedProb.execute().get()
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
        } else if (uuid == null) {
            JoinActivity.dispToast(this, "유저 정보를 불러오지 못했습니다. 개발자에게 문의해주세요.")
            val homepage = Intent(this@SolveActivity, MainActivity::class.java)
            startActivity(homepage)
        }
    }




    class getProblem(val problem: Int) : AsyncTask<Void, Int, Any>() {
        protected override fun doInBackground(vararg unused: Void): Any? {
            //암호화
            /* 인풋 파라메터값 생성 */
            val param = "u_problem=" + problem
            try {
                /* 서버연결 */
                val url = URL(
                    "https://yoon-lab.xyz/mathpjt_solve.php"
                )
                val conn = url.openConnection() as HttpURLConnection
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.setRequestMethod("POST")
                conn.setDoInput(true)
                conn.connect()
                /* 안드로이드 -> 서버 파라메터값 전달 */
                val outs = conn.getOutputStream()
                outs.write(param.toByteArray(charset("UTF-8")))
                outs.flush()
                outs.close()
                /* 서버 -> 안드로이드 파라메터값 전달 */
                val iss = conn.getInputStream()
                var inn = BufferedReader(InputStreamReader(iss))
                val line = inn.readLine()
                Log.e("RECV DATA*", line)
                if (line == "Error 4: No Data") {
                    return line
                }
                val prob = line.split(",".toRegex())
                problemAns = prob[0].toInt()
                problempoint = prob[2].toInt()
                problemsolver = prob[3].toInt()
                var bmImg: Bitmap? = null
                try {
                    val myFileUrl: URL = URL(prob[1])
                    val conn: HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
                    conn.setDoInput(true)
                    conn.connect()
                    val is2: InputStream = conn.getInputStream()
                    bmImg = BitmapFactory.decodeStream(is2)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return bmImg

            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        protected override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
            if (result == "Error 4: No Data") {
                JoinActivity.dispToast(mContext_Solve, "오류가 발생했습니다. 에러코드: 4 개발자에게 연락바랍니다.")
                return
            }
            try {
                problemView?.setImageBitmap(result as Bitmap?)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}