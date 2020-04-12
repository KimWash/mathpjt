package com.yoonlab.mathproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_store.*

class RankActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        val problemselect = Intent(this@RankActivity, SelectActivity::class.java)
        val storepage = Intent(this@RankActivity, StoreActivity::class.java)
        val home = Intent(this@RankActivity, MainActivity::class.java)
        val rank = Intent(this@RankActivity, RankActivity::class.java)
        val learning = Intent(this@RankActivity, LearnActivity::class.java)
        solve.setOnClickListener {
            finish()
            startActivity(problemselect) }
        store.setOnClickListener {
            finish()
            startActivity(storepage) }
        gohome.setOnClickListener {
            finish()
            startActivity(home)}
        learn.setOnClickListener{
            finish()
            startActivity(learning)}
        ranking.setOnClickListener{
            finish()
            startActivity(rank)}
    }
}