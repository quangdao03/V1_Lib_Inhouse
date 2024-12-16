/**
 * 20290929 chek OK
 */

package com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads

class RemoteConfigAdNativeModel(

    var ad_name: String = "",

    //NativeAdvanManager0.ad_id
    var ad_id: String = "",

    //not using in NativeAdvanceNanager,
    //setting in AdCommon or in app
    var is_show: Boolean = true,

    //max preload native times
    //param in NativeAdvanceManager0.preloadedMaxTimes
    //for height show rate
    var max_preload: Int = 1,

    //Timer reload setting (default reload resume, trigger : yes)
    //setting in AdCommon
    //before using set NativeAdvanceManager1.isAutoReloadByTimer properties
    var is_auto_reload_timer: Boolean = true,

    //dung trong NativeAdvanceManager1.timerReloadInterval
    //setting in AdCommon
    //before using set NativeAdvanceManager1.ad_reload_timer_interval properties
    var ad_reload_timer_interval: Long = 0,

    //change button click only when Fan
    //setting in AdCommon
    //before using set NativeAdvanceManager2.is_change_button_click_only_when_fan
    var is_change_button_click_only_when_fan: Boolean = true,

    //Anh add 20241125
    //change all disable click when Fan
    //setting in AdCommon
    var is_change_disable_click_all_when_fan: Boolean = true,

    //auto reload when video end -chua implement
    //ANH dang nghien cuu spec reload cung voi Cuong
    //var is_auto_reload_when_media_finish: Boolean = true,

    //native ad layout file
    //setting in App when call loadAndShowNative function
    var ad_layout_file: String = "",

    //background color
    var bg_color: String = "",

    //border color layout
    var border_color: String = "",
)