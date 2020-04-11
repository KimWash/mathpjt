package com.yoonlab.mathproject

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class GetProblem(private var problem: Int) : AsyncTask<Void, Int, Array<String>>() {
    override fun doInBackground(vararg unused: Void): Array<String>? {
        //암호화
        /* 인풋 파라메터값 생성 */
        val param = "u_problem=$problem"
        try {
            // 서버연결
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_solve.php"
            )
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.connect()
            /* 안드로이드 -> 서버 파라메터값 전달 */
            var outs = conn.outputStream
            outs.write(param.toByteArray(charset("UTF-8")))
            outs.flush()
            outs.close()
            /* 서버 -> 안드로이드 파라메터값 전달 */
            val iss = conn.inputStream
            val inn = BufferedReader(InputStreamReader(iss))
            val line = inn.readLine()
            Log.e("RECV DATA*", line)
            if (line == "Error 4: No Data") {
                return arrayOf<String>(line)
            }
            val prob = line.split(",".toRegex())
            var problempoint1 = prob[2]
            var problemsolver1 = prob[3]
            var problemlevel = prob[1].split("_".toRegex())[2].split("\\.".toRegex())[0]
            var problemAns = prob[0]
            return arrayOf<String>(problempoint1, problemsolver1, problemlevel, problemAns, prob[1])
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    override fun onPostExecute(result: Array<String>) {
        super.onPostExecute(result)
        if (result[0] == "Error 4: No Data") {
            JoinActivity.dispToast(mContext_Solve, "오류가 발생했습니다. 에러코드: 4 개발자에게 연락바랍니다.")
            return
        }
        return
    }
}