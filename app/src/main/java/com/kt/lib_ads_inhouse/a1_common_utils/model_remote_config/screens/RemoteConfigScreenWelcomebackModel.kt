package com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.screens

data class RemoteConfigScreenWelcomebackModel (
    //
    val screen_name: String,
    val is_show_012: Int,
    val is_hide_navi_menu: Boolean,
    val layout_name: String,
    val next_screen_default: String,

    //custom properties
)