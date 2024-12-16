package com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.app

data class RemoteConfigAppAllModel (
    val is_show_wellcomeback_screen: Boolean,
    val welcomeback_tittle: String,
    val app_update_diaglog_or_activity: String,

    //test mode
    //config in remote config
    //test_mode ="fan_mediation" -> fan win mediation
    //val test_mode:String="",

    //config in remote config
    val api_url: String,
    val api_token: String,
)
