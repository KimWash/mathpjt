package com.yoonlab.mathproject

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.io.InputStreamReader as InputStreamReader1

class GetInform(private val uuid: String?, private val type:Int) : AsyncTask<Void, Int, Any>() {
    protected override fun doInBackground(vararg unused: Void): Any? {
        //암호화
        /* 인풋 파라메터값 생성 */
        //컬럼 번호수 (0: id, 1:pw, 2:email, 3:heart, 4:uuid(UUID는고정.수정불가), 5:point, 6:grade, 7:solvingproblem)
        val param = "u_uuid=$uuid&type=$type"
        try {
            /* 서버연결 */
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_getInf.php"
            )
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.connect()
            /* 안드로이드 -> 서버 파라메터값 전달 */
            val outs = conn.outputStream
            outs.write(param.toByteArray(charset("UTF-8")))
            outs.flush()
            outs.close()
            /* 서버 -> 안드로이드 파라메터값 전달 */
            val iss = conn.inputStream
            val inn = BufferedReader(InputStreamReader1(iss))
            val line = inn.readLine()
            Log.e("RECV DATA", line)
            return line

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    protected override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        return

    }
}