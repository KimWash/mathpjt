package com.yoonlab.mathproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.app.Activity
import kotlinx.android.synthetic.main.activity_store.*


class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        val problemselect = Intent(this@StoreActivity, SelectActivity::class.java)
        val storepage = Intent(this@StoreActivity, StoreActivity::class.java)
        val home = Intent(this@StoreActivity, MainActivity::class.java)
        solve.setOnClickListener { startActivity(problemselect) }
        store.setOnClickListener { startActivity(storepage) }
        gohome.setOnClickListener { startActivity(home)}
    }

}
