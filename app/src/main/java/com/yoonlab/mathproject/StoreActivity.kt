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


class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        val problemselect = Intent(this@StoreActivity, SelectActivity::class.java)
        val storepage = Intent(this@StoreActivity, StoreActivity::class.java)
        val home = Intent(this@StoreActivity, MainActivity::class.java)
        val rank = Intent(this@StoreActivity, RankActivity::class.java)
        val learning = Intent(this@StoreActivity, LearnActivity::class.java)
        val chshop = Intent(this@StoreActivity, ChshopAcitivity::class.java) // 캐릭터샵으로 가기
        val realshop = Intent(this@StoreActivity, RealShopActivity::class.java) // 기프티콘 샵으로 가기
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
        chshopbutton.setOnClickListener{
            finish()
            startActivity(chshop)
        }
        realshopbutton.setOnClickListener{
            finish()
            startActivity(realshop)
        }
    }

}
