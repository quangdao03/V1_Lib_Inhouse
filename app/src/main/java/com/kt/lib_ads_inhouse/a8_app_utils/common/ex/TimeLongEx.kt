package com.kt.lib_ads_inhouse.a8_app_utils.common.ex

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

const val HOUR_FORMAT = "HH:mm"
const val DAY_FORMAT = "dd-MM-yyyy"
const val HOUR_MINUTE_SECOND= "hh:mm:ss"

@SuppressLint("SimpleDateFormat")
fun Long.convertTime(format: String = DAY_FORMAT) : String {
    val date = Date(this)
    return SimpleDateFormat(format).format(date)
}