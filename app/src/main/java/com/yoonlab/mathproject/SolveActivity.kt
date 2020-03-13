package com.yoonlab.mathproject

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_solve.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd
import java.util.*


var problemView:ImageView? = null
var problemAns:Int? = null
var problempoint:Int = 0
var problemsolver:Int = 0

class SolveActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve)
        //어떤 문제를 불러옴??
        problemView = findViewById<ImageView>(R.id.problems)
        val HMP = howManyProblems()
        val pCount = HMP.execute().get()
        if (pCount is Int){
            Log.i("문제번호", pCount.toString())
            val random = Random()
            val num = random.nextInt(pCount)+1
            val gPb = getProblem(num)
            gPb.execute()
        }
        else{
            finish()
        }
        val submitButton = findViewById<Button>(R.id.submit)
        submitButton.setOnClickListener { checkAnswer() }
    }

    fun checkAnswer() {
        var useruuid: SharedPreferences = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        var uuid = useruuid.getString("uuid", null)
        Log.i("uuid", uuid.toString())
        if (uuid != null) {
            val editHeart = editHeart(uuid.toString(), 0, 1) //UUID 불러오기
            val getHeart = getHeart(uuid)
            val heart = getHeart.execute().get() as Int
            if (heart > 0) {
                val result = editHeart.execute().get().toString()
                if (result == "success") { //하트를 정상적으로 수정했을 때
                    if (Integer.parseInt(answer.getText().toString()) == problemAns) {
                        JoinActivity.dispToast(this, "정답입니다!")
                        if(problemsolver == 0){
                            editPoint(uuid, 1, problempoint)
                        }
                        else{
                            var totalpoint: Int = problempoint / problemsolver
                            editPoint(uuid, 1, totalpoint)
                        }
                    }
                } else {  //하트를 수정하지 못했을 때
                    JoinActivity.dispToast(this, "서버 오류입니다. 개발자에게 문의해주세요.")
                }
            } else if (heart <= 0) {
                JoinActivity.dispToast(this, "하트가 모자랍니다. 충전해주세요!")
            }
        } else if (uuid == null) {
            JoinActivity.dispToast(this, "유저 정보를 불러오지 못했습니다. 개발자에게 문의해주세요.")
        }
    }

    class howManyProblems() : AsyncTask<Void, Int, Any>() {
        protected override fun doInBackground(vararg unused: Void): Any? {
            //암호화
            /* 인풋 파라메터값 생성 */
            val param = ""
            try {
                /* 서버연결 */
                val url = URL(
                    "https://yoon-lab.xyz/mathpjt_HMP.php"
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
                Log.e("RECV DATA", line)
                if (line.matches("\\d+".toRegex())) { //숫자만 있는게 확인되면
                    return line.toInt()
                }
                else{
                    return line
                }
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
                JoinActivity.dispToast(mContext, "오류가 발생했습니다. 에러코드: 4 개발자에게 연락바랍니다.")
                return
            }
            return
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
                Log.e("RECV DATA", line)

                if (line == "Error 4: No Data") {
                    return line
                }
                val prob = line.split(",".toRegex())
                problemAns = prob[0].toInt()
                problempoint = prob[3].toInt()
                problemsolver = prob[4].toInt()
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
        //문제 포인트 계산
        fun pointcalc(problempoint: Int, problemsolver: Int) = problempoint / problemsolver

        protected override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
            if (result == "Error 4: No Data") {
                JoinActivity.dispToast(mContext, "오류가 발생했습니다. 에러코드: 4 개발자에게 연락바랍니다.")
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



