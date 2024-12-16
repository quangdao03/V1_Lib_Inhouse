package com.kt.lib_ads_inhouse.a8_app_utils.common

enum class AlertType {
    MESSAGE, ERROR, TOAST
}

data class AlertData(val message: String, val title: String? = null)