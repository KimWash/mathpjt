package com.yoonlab.mathproject

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class getHeart(val uuid: String?) : AsyncTask<Void, Int, Any>() {
    protected override fun doInBackground(vararg unused: Void): Any? {
        //암호화
        /* 인풋 파라메터값 생성 */
        val param = "u_uuid=" + uuid
        try {
            /* 서버연결 */
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_getheart.php"
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
            return line.toInt()

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