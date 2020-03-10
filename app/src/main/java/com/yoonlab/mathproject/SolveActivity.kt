package com.yoonlab.mathproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.io.IOException
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardedVideoAd



var problemView:ImageView? = null
class SolveActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve)
        //어떤 문제를 불러옴??
        problemView = findViewById<ImageView>(R.id.problems)
        val gPb = getProblem(1)// 문제번호 삽입 요망
        gPb.execute()

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

                if (line == "Error 4: No Data"){
                    return 4
                }
                val prob = line.split(",".toRegex())
                var bmImg:Bitmap? = null
                try {
                    val myFileUrl:URL = URL(prob[1])
                    val conn:HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
                    conn.setDoInput(true)
                    conn.connect()
                    val is2:InputStream = conn.getInputStream()
                    bmImg = BitmapFactory.decodeStream(is2)
                }catch (e:IOException){
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
            if (result == 4) {
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

