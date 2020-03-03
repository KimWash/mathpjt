package com.yoonlab.mathproject

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yoonlab.mathproject.ui.login.registDB
import kotlinx.android.synthetic.main.activity_join.*



class JoinActivity : AppCompatActivity() {

    fun dispToast(str:String){
        val toast = Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener{
            var userInf = Array<String>(4) {username.getText(); email.getText(); password.getText(); password_chk.getText().toString()}
            val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
            if (userInf[0] != null && userInf[1] != null && userInf[2] != null && userInf[3] != null) {
                if (userInf[2].equals(userInf[3])) {
                    val rdb = registDB(userInf[0], userInf[1], userInf[2])
                    rdb.execute()
                }
            }
            else {
                dispToast("빈칸을 채우세요.")
            }

            }
        }
    }







