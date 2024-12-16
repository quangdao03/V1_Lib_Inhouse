package com.kt.lib_ads_inhouse.a8_app_utils.common.ex

import com.google.gson.Gson

inline fun <reified T> String.convertToObject(): T = Gson().fromJson(this, T::class.java)
