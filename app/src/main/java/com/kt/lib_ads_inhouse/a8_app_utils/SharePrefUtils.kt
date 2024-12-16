package com.kt.lib_ads_inhouse.a8_app_utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null

    fun init(context: Context?) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }


    fun isRated(context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("rated", false)
    }


    fun forceRated(context: Context) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("rated", true)
        editor.commit()
    }

    fun isAppClosed (context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("isAppClosed", false)
    }


    fun foreAppClosed (context: Context, boolean: Boolean) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("isAppClosed", boolean)
        editor.commit()
    }

    fun countChat(context: Context): Int {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getInt("countChat", 0)
    }

    fun increaseCountChat(context: Context){
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putInt("countChat", countChat(context) + 1)
        editor.commit()
    }

}