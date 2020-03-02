package com.yoonlab.mathproject

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class nightModeCheck {
    companion object {
        fun isNightModeActive(context: Context): Boolean {
            val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
            if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                return true
            }
            if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
                return false
            }
            val currentNightMode = (context.resources.configuration.uiMode
                    and Configuration.UI_MODE_NIGHT_MASK)
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> return false
                Configuration.UI_MODE_NIGHT_YES -> return true
                Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
            }
            return false
        }
    }

}