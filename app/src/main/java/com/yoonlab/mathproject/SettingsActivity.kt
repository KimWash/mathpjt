package com.yoonlab.mathproject

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
class SettingsActivity : AppCompatActivity() {
    var useruuid: SharedPreferences? = null
    var uuidl:String? = null
    var loggedinStatus: SharedPreferences? = null
    var loggedin:Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        useruuid = getSharedPreferences("uuid", Activity.MODE_PRIVATE)
        uuidl = useruuid?.getString("uuid", null)
        loggedinStatus = getSharedPreferences("loggedIn", Activity.MODE_PRIVATE)
        loggedin = loggedinStatus?.getBoolean("loggedIn", true) //Todo:수정
    }

}