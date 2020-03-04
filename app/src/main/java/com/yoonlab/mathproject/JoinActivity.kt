package com.yoonlab.mathproject

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import com.yoonlab.mathproject.ui.login.registDB
import kotlinx.android.synthetic.main.activity_join.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern


public var mContext:Context? = null
class JoinActivity : AppCompatActivity() {
    fun checkEmail(email: String): Boolean {
        val regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(email)
        return m.matches()
    }
    fun checkPw(pw:String):Boolean{
        if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pw)){
            return false
        }
        return true
    }

    companion object{
        fun dispToast(context:Context?, str:String){
            val toast = Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        mContext = this
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener{submitButton()}
    }

    fun submitButton(){
        Log.i("정보", username.getText().toString()+email.getText().toString()+password.getText().toString())
        val sId = username.getText().toString()
        val sEmail = email.getText().toString()
        val sPw = password.getText().toString()
        val sPw_chk = password_chk.getText().toString()
        //Todo: 아이디 중복확인, 비밀번호 생성규칙, 암호화(SHA-256)
        if (sId == ""){
            dispToast(this, "ID를 입력해주세요.")
        }
        else if (sEmail == ""){
            dispToast(this, "이메일을 입력해주세요.")
        }
        else if (sPw == ""){
            dispToast(this, "비밀번호를 입력해주세요.")
        }
        else if (sPw_chk == ""){
            dispToast(this, "비밀번호를 한번 더 입력해주세요.")
        }
        else if (!checkEmail(sEmail)){
            dispToast(this, "올바른 이메일 형식이 아닙니다.")
        }
        else if (sPw != sPw_chk){
            dispToast(this, "비밀번호가 일치하지 않습니다.")
        }
        else if (!checkPw(sPw)){
            dispToast(this, "비밀번호는 영어, 숫자, 특수문자를 포함하여 8자에서 20자 사이로 지어주세요.")
        }
        else {
            val rdb = registDB(sId, sEmail, sPw)
            rdb.execute()

        }

    }


}



class registDB(val sId: String,val sEmail: String, val sPw: String) : AsyncTask<Void, Int, Int>() {
    protected override fun doInBackground(vararg unused:Void): Int? {
        /* 인풋 파라메터값 생성 */
        val param = "u_id=" + sId + "&u_email=" + sEmail + "&u_pw=" + sPw + ""
        try
        {
            /* 서버연결 */
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_join.php")
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
            if (line == "success"){
                return 0
            }
            else if (line == "Error 1: Server Error"){
                return 1
            }
            else if (line == "Error 2: SQL Error"){
                return 2
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
}




