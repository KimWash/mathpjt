package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.app.Activity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.activity_store.gohome
import kotlinx.android.synthetic.main.activity_store.learn
import kotlinx.android.synthetic.main.activity_store.ranking
import kotlinx.android.synthetic.main.activity_store.solve
import kotlinx.android.synthetic.main.activity_store.store


class LearnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)
        val problemselect = Intent(this@LearnActivity, SelectActivity::class.java)
        val storepage = Intent(this@LearnActivity, StoreActivity::class.java)
        val home = Intent(this@LearnActivity, MainActivity::class.java)
        val rank = Intent(this@LearnActivity, RankActivity::class.java)
        val learning = Intent(this@LearnActivity, LearnActivity::class.java)
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