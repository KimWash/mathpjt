package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.selectlevel.*
import androidx.recyclerview.widget.RecyclerView



class SelectActivity : AppCompatActivity(){
    private val adapter = SelectAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectlevel)

        initLayout()
    }
    fun initLayout(){
        selecting.adapter = adapter
        selecting.layoutManager = LinearLayoutManager(this)

        //클릭리스너 등록
        adapter.setItemClickListener( object : SelectAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int) {
                val solvepage = Intent(this@SelectActivity, SolveActivity::class.java)
                startActivity(solvepage)
            }
        })
    }
}
