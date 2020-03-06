package com.yoonlab.mathproject

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_solve.*

import java.net.HttpURLConnection
import java.net.URL
import java.io.IOException



class SolveActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solve)

        fetchJson()

    }
    fun fetchJson(){
        val url = URL("https://yoon-lab.xyz/mathpjt_solve.php")
        val conn = url.openConnection() as HttpURLConnection
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        conn.setDoInput(true)
        conn.connect()



    }


}