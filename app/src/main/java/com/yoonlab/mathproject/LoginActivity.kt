package com.yoonlab.mathproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button


import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.username
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import android.util.Base64
import java.net.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

var mContext_Login: Context? = null
class LoginActivity : AppCompatActivity() {


    fun nightMode(){
        if (nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.DarkTheme)
        } else if (!nightModeCheck.isNightModeActive(this)) {
            setTheme(R.style.LightTheme)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nightMode()
        setContentView(R.layout.activity_login)
        mContext_Login = this
        setSupportActionBar(toolbar)
        supportActionBar?.title = "로그인"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener{submitButton()}
    }

    fun submitButton(){
        val sId = username.text.toString()
        val sPw = password.text.toString()
        //Todo: 아이디
        if (sId == ""){
            JoinActivity.dispToast(this, "ID를 입력해주세요.")
        }
        else if (sPw == ""){
            JoinActivity.dispToast(this, "비밀번호를 입력해주세요.")
        }
        else{
            val rdb = loginDB(this, sId, sPw)
            val result = rdb.execute().get()
            if (result == 0){
                password.setText("")
            }
            else if (result == 3){
                username.setText("")
            }
            else {
                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(mainIntent)
                finish()
            }
        }
    }

    class loginDB(val context:Context, val sId: String,val sPw: String) : AsyncTask<Void, Int, Any>() {
        protected override fun doInBackground(vararg unused:Void): Any? {
            Log.i("gotId", sId)
            Log.i("getPw", sPw)
            //ID와 비번 암호화
            val cryptedPw = CryptoPw(sPw)
            val decodedPw = URLDecoder.decode(cryptedPw)
            Log.i("decodedPw", decodedPw)
            /* 인풋 파라메터값 생성 */
            val param = "u_id=$sId&u_pw=$decodedPw"
            try
            {
                /* 서버연결 */
                val url = URL(
                    "https://yoon-lab.xyz/mathpjt_login.php")
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
                val inn = BufferedReader(InputStreamReader(iss))
                val line = inn.readLine()
                Log.e("RECV DATA", line)
                if (line.equals("0")){
                    return 0
                }
                else if(line.equals("1064")){
                    return 1064
                }
                else if (line.equals("Error Code 3: ID Not Found")){
                    return 3
                }
                else {
                    return line
                }

            }
            catch (e: MalformedURLException) {
                e.printStackTrace()
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }


        override fun onPostExecute(code: Any?) {
            super.onPostExecute(code)
            if (code == 0){
                JoinActivity.dispToast(mContext_Login, "비밀번호가 일치하지 않습니다.")
                return
            }
            else if (code == 1064){
                JoinActivity.dispToast(mContext_Login, "로그인에 실패하였습니다. 에러코드: 1064 개발자에게 문의해주세요.")
                return
            }
            else if (code == 3){
                JoinActivity.dispToast(mContext_Login, "아이디를 찾을 수 없습니다.")
                return
            }
            else {
                JoinActivity.dispToast(mContext_Login, "로그인에 성공하였습니다!")
                val prefs: SharedPreferences = context.getSharedPreferences("Pref", MODE_PRIVATE)
                prefs.edit().putBoolean("isFirstRun", false).apply()
                val useruuid: SharedPreferences = context.getSharedPreferences("uuid", MODE_PRIVATE)
                val uuideditor: SharedPreferences.Editor = useruuid.edit()
                uuideditor.putString("uuid", code.toString())
                uuideditor.apply()
                return
            }


        }
        //AES128 암호화
        private fun CryptoPw(sPw: String): String {
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val keySpec = SecretKeySpec(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0) , "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val crypted = cipher.doFinal(sPw.toByteArray())

            val encodedByte = Base64.encode(crypted, Base64.DEFAULT)
            return String(encodedByte)
        }

    }
}
