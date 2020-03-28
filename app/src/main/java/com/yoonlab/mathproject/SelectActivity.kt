package com.yoonlab.mathproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.selectlevel.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_solve.*
import java.io.BufferedReader
import java.io.IOException
import android.os.NetworkOnMainThreadException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


var problemlevel: String = ""
var problempoint1: String = ""
var problemsolver1: String = ""
var sProblem:Int = 0

class SelectActivity : AppCompatActivity(){
    var items: MutableList<ProblemList> = mutableListOf()
    private val adapter = SelectAdapter(items)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectlevel)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl2 = useruuid?.getString("uuid", null)
        //어떤 문제를 불러옴??
        problemView = findViewById<ImageView>(R.id.problems)
        val pCount = howManyProblems().execute().get() as Int
        for (i in 1 until pCount+1) {
            var problemInf = GetProblem1(i).execute().get() as Array<String>
            problempoint1 = problemInf[0]
            problemsolver1 = problemInf[1]
            problemlevel = problemInf[2]
            when (problemlevel) {
                "0" -> {
                    items.add(ProblemList("$i","Easy","$problempoint1","$problemsolver1"))}
                "1" -> {
                    items.add(ProblemList("$i","Middle","$problempoint1","$problemsolver1"))}
                "2" -> {
                    items.add(ProblemList("$i","Hard","$problempoint1","$problemsolver1"))}
            }
        }
        initLayout()
    }
    fun initLayout(){
        selecting.adapter = adapter
        selecting.layoutManager = LinearLayoutManager(this)

        //클릭리스너 등록
        adapter.setItemClickListener( object : SelectAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                sProblem = position
                val solvepage = Intent(this@SelectActivity, SolveActivity::class.java)
                startActivity(solvepage)
            }
        })
    }
    class GetProblem1(var problem: Int) : AsyncTask<Void, Int, Any>() {
        protected override fun doInBackground(vararg unused: Void): Any? {
            //암호화
            /* 인풋 파라메터값 생성 */
            var param = "u_problem=" + problem
            try {
                /* 서버연결 */
                val url = URL(
                    "https://yoon-lab.xyz/mathpjt_solve.php"
                )
                var conn = url.openConnection() as HttpURLConnection
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                conn.setRequestMethod("POST")
                conn.setDoInput(true)
                conn.connect()
                /* 안드로이드 -> 서버 파라메터값 전달 */
                var outs = conn.getOutputStream()
                outs.write(param.toByteArray(charset("UTF-8")))
                outs.flush()
                outs.close()
                /* 서버 -> 안드로이드 파라메터값 전달 */
                var iss = conn.getInputStream()
                var inn = BufferedReader(InputStreamReader(iss))
                var line = inn.readLine()
                Log.e("RECV DATA*", line)
                if (line == "Error 4: No Data") {
                    return line
                }
                var prob = line.split(",".toRegex())
                problempoint1 = prob[2]
                problemsolver1 = prob[3]
                var problemstring = prob[1].split("_".toRegex())
                problemlevel = problemstring[2]
                var problemInf:Array<String> = arrayOf(problempoint1, problemsolver1, problemlevel)
                return problemInf
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
            return
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
                } else {
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
                JoinActivity.dispToast(mContext_Solve, "오류가 발생했습니다. 에러코드: 4 개발자에게 연락바랍니다.")
                return
            }
            return
        }
    }
}
