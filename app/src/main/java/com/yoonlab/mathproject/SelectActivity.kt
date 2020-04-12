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



var sProblem:Int = 0

class SelectActivity : AppCompatActivity(){
    var items: MutableList<ProblemList> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectlevel)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl2 = useruuid?.getString("uuid", null)
        //어떤 문제를 불러옴??
        val pCount = HMP().execute().get() as Int
        items.add(ProblemList("번호","난이도", "푼 사람" ,"포인트"))
        for (i in 1 until pCount+1) {
            var problemInf = GetProblem(i).execute().get() as Array<String>
            val problemlevel = problemInf[2].toInt()
            when (problemlevel) {
                0 -> {
                    items.add(ProblemList("$i","Easy", problemInf[0] ,problemInf[1]))}
                1 -> {
                    items.add(ProblemList("$i","Middle",problemInf[0],problemInf[1]))}
                2 -> {
                    items.add(ProblemList("$i","Hard",problemInf[1],problemInf[2]))}
            }
        }
        Log.e("items", items.toString())
        val adapter = SelectAdapter(items)
        selecting.adapter = adapter
        selecting.layoutManager = LinearLayoutManager(this)

        //클릭리스너 등록
        adapter.setItemClickListener( object : SelectAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                sProblem = position+1
                val solvepage = Intent(this@SelectActivity, SolveActivity::class.java)
                startActivity(solvepage)
            }
        })
    }

}
