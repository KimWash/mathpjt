package com.yoonlab.mathproject

//import com.yoonlab.mathproject.ui.login.registDB

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
public var invalidId = false
public var invalidChecked = false

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
        val invalidButton = findViewById<Button>(R.id.checkInvalid)
        invalidButton.setOnClickListener{checkInvalid()}
        val edit = findViewById<EditText>(R.id.username)
        edit.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { // 입력되는 텍스트에 변화가 있을 때
                invalidChecked = false
            }
            override fun afterTextChanged(arg0: Editable) { // 입력이 끝났을 때
                invalidChecked = false
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { // 입력하기 전에
                invalidChecked = false
            }
        })
    }
    fun checkInvalid(){
        val sId = username.getText().toString()
        val cI = checkInvalid(sId)
        val result = cI.execute().get()
        if (result == 1){
            dispToast(this, "이미 존재하는 ID입니다.")
            invalidId = true
            username.setText("")
        }
        else if (result == 2){
            dispToast(this, "사용 가능한 ID입니다.")
            invalidId = false
        }
        invalidChecked = true
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
        else if (!invalidChecked){
            dispToast(this, "아이디 중복확인을 해주세요.")
        }
        else {
            val rdb = registDB(sId, sEmail, sPw)
            val result = rdb.execute().get()
            if (result == 0) {  // 회원가입이 완료되면?
                val MainIntent = Intent(this@JoinActivity, MainActivity::class.java)
                startActivity(MainIntent)
                var prefs: SharedPreferences = getSharedPreferences("Pref", AppCompatActivity.MODE_PRIVATE)
                prefs.edit().putBoolean("isFirstRun", false).apply()
            }
        }
    }
}

class checkInvalid(val sId: String) : AsyncTask<Void, Int, Int>() {
    protected override fun doInBackground(vararg unused:Void): Int? {
        /* 인풋 파라메터값 생성 */
        val param = "u_id=" + sId
        try
        {
            /* 서버연결 */
            val url = URL(
                "https://yoon-lab.xyz/mathpjt_validid.php")
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
            if (line == "Code 1: Same ID Exist"){
                return 1
            }
            else if(line == "Code 2: No Same ID"){
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
        return

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
            return
        }
        else if (code == 1){
            JoinActivity.dispToast(mContext, "서버와의 연결에 실패했습니다. Error Code: 1")
            return
        }
        else if (code == 2){
            JoinActivity.dispToast(mContext, "회원가입에 실패했습니다. Error Code: 2")
            return
        }


    }
}





