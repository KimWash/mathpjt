package com.yoonlab.mathproject.ui.login

import android.app.Activity
import android.os.AsyncTask
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.yoonlab.mathproject.*
import com.yoonlab.mathproject.registDB

import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.username
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import android.util.Base64
import java.io.UnsupportedEncodingException;
import java.security.CryptoPrimitive
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    fun nightMode(){
        if (nightModeCheck.isNightModeActive(this) == true) {
            setTheme(R.style.DarkTheme)
        } else if (nightModeCheck.isNightModeActive(this) == false) {
            setTheme(R.style.LightTheme)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nightMode()
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        getSupportActionBar()?.title = "로그인"
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener{submitButton()}
    }
    fun submitButton(){
        val sId = username.getText().toString()
        val sPw = password.getText().toString()
        //Todo: 아이디
        if (sId == ""){
            JoinActivity.dispToast(this, "ID를 입력해주세요.")
        }
        else if (sPw == ""){
            JoinActivity.dispToast(this, "비밀번호를 입력해주세요.")
        }
        else{
            val rdb = loginDB(sId, sPw)
            rdb.execute()
        }
    }


}

class loginDB(val sId: String,val sPw: String) : AsyncTask<Void, Int, Int>() {
    protected override fun doInBackground(vararg unused:Void): Int? {
        //ID와 비번 암호화
        val cryptedsPw = CryptoPw(sPw)
        /* 인풋 파라메터값 생성 */
        val param = "u_id=" + sId + "&u_pw=" + cryptedsPw + ""
        try
        {
            /* 서버연결 */
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_login.php")
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

        }
        catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    protected override fun onPostExecute(code:Int){
        super.onPostExecute(code)
        if (code == 0){
            JoinActivity.dispToast(mContext, "회원가입이 완료되었습니다.")
        }
        else if (code == 1){
            JoinActivity.dispToast(mContext, "서버와의 연결에 실패했습니다. Error Code: 1")
        }
        else if (code == 2){
            JoinActivity.dispToast(mContext, "회원가입에 실패했습니다. Error Code: 2")
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