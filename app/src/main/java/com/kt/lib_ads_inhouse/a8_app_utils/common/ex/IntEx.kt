package com.kt.lib_ads_inhouse.a8_app_utils.common.ex

fun Int.divideToPercent(divideTo: Int): Int {
    return if (divideTo == 0) 0
    else {
        val percent = this / divideTo.toFloat() * 100
        if (percent >= 90) return 90
        else return percent.toInt()
    }
}