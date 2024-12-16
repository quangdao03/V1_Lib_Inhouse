package com.kt.lib_ads_inhouse.a8_app_utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object EventTracking {
    //event name
    const val splash_open = "splash_open"
    const val language_fo_open = "language_fo_open"
    const val language_fo_save_click = "language_fo_save_click"
    const val v1_event_intro1_view = "v1_event_intro1_view"
    const val v1_event_intro1_next = "v1_event_intro1_next"
    const val v1_event_intro2_view = "v1_event_intro2_view"
    const val v1_event_intro2_next = "v1_event_intro2_next"
    const val v1_event_intro3_view = "v1_event_intro3_view"
    const val v1_event_intro3_next = "v1_event_intro3_next"
    const val v1_event_intro4_view = "v1_event_intro4_view"
    const val v1_event_intro4_next = "v1_event_intro4_next"
    const val v1_event_permission_view = "v1_event_permission_view"
    const val v1_event_permission_allow_continue = "v1_event_permission_allow_continue"
    const val v1_event_rate_show = "v1_event_rate_show"
    const val v1_event_rate_not_now = "v1_event_rate_not_now"
    const val v1_event_rate_submit = "v1_event_rate_submit"
    const val language_view = "language_view"
    const val language_save_click = "language_save_click"
    const val home_view = "home_view"
    const val home_makeup_try_now_click = "home_makeup_try_now_click"
    const val home_changehair_try_now_click = "home_changehair_try_now_click"
    const val home_clothify_try_now_click = "home_clothify_try_now_click"
    const val home_makeup_see_all_click = "home_makeup_see_all_click"
    const val home_changehair_see_all_click = "home_changehair_see_all_click"
    const val home_clothify_see_all_click = "home_clothify_see_all_click"
    const val makeup_realistic_view = "makeup_realistic_view"
    const val makeup_chibi_view = "makeup_chibi_view"
    const val makeup_back_click = "makeup_back_click"
    const val changehair_women_view = "changehair_women_view"
    const val changehair_men_view = "changehair_men_view"
    const val changehair_back_click = "changehair_back_click"
    const val clothify_vintage_view = "clothify_vintage_view"
    const val clothify_summer_view = "clothify_summer_view"
    const val clothify_cool_view = "clothify_cool_view"
    const val clothify_back_click = "clothify_back_click"
    const val setting_view = "setting_view"
    const val setting_language_click = "setting_language_click"
    const val setting_rate_click = "setting_rate_click"
    const val setting_privacy_policy_click = "setting_privacy_policy_click"
    const val setting_share_click = "setting_share_click"

    //params
    const val position = "position"
    const val rate_star = "rate_star"

    //value


    // event inter_splash
    const val v1_event_ads_request_load_inter_splash = "v1_event_ads_request_load_inter_splash"
    const val v1_event_ads_load_inter_splash_success = "v1_event_ads_load_inter_splash_success"
    const val v1_event_ads_request_show_inter_splash = "v1_event_ads_request_show_inter_splash"
    const val v1_event_ads_show_inter_splash_success = "v1_event_ads_show_inter_splash_success"

    // event native_language
    const val v1_event_ads_request_load_native_language = "v1_event_ads_request_load_native_language"
    const val v1_event_ads_load_native_language_success = "v1_event_ads_load_native_language_success"
    const val v1_event_ads_request_show_native_language = "v1_event_ads_request_show_native_language"
    const val v1_event_ads_show_native_language_success = "v1_event_ads_show_native_language_success"

    // event native_intro
    const val v1_event_ads_request_load_native_intro = "v1_event_ads_request_load_native_intro"
    const val v1_event_ads_load_native_intro_success = "v1_event_ads_load_native_intro_success"
    const val v1_event_ads_request_show_native_intro = "v1_event_ads_request_show_native_intro"
    const val v1_event_ads_show_native_intro_success = "v1_event_ads_show_native_intro_success"

    // event inter_intro
    const val v1_event_ads_request_load_inter_intro = "v1_event_ads_request_load_inter_intro"
    const val v1_event_ads_load_inter_intro_success = "v1_event_ads_load_inter_intro_success"
    const val v1_event_ads_request_show_inter_intro = "v1_event_ads_request_show_inter_intro"
    const val v1_event_ads_show_inter_intro_success = "v1_event_ads_show_inter_intro_success"

    // event native_permission
    const val v1_event_ads_request_load_native_permission = "v1_event_ads_request_load_native_permission"
    const val v1_event_ads_load_native_permission_success = "v1_event_ads_load_native_permission_success"
    const val v1_event_ads_request_show_native_permission = "v1_event_ads_request_show_native_permission"
    const val v1_event_ads_show_native_permission_success = "v1_event_ads_show_native_permission_success"

    // event open resume
    const val v1_event_ads_request_load_open_resume = "v1_event_ads_request_load_open_resume"
    const val v1_event_ads_load_open_resume_success = "v1_event_ads_load_open_resume_success"
    const val v1_event_ads_request_show_open_resume = "v1_event_ads_request_show_open_resume"
    const val v1_event_ads_show_open_resume_success = "v1_event_ads_show_open_resume_success"

    // event native full
    const val v1_event_ads_request_load_native_full = "v1_event_ads_request_load_native_full"
    const val v1_event_ads_load_native_full_success = "v1_event_ads_load_native_full_success"
    const val v1_event_ads_request_show_native_full = "v1_event_ads_request_show_native_full"
    const val v1_event_ads_show_native_full_success = "v1_event_ads_show_native_full_success"

    fun logEvent(context: Context, nameEvent: String) {
        val bundle = Bundle()
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }

    fun logEvent(context: Context, nameEvent: String, param: String, value: String) {
        val bundle = Bundle()
        bundle.putString(param, value)
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }

    fun logEvent(context: Context, nameEvent: String, param: String, value: Long) {
        val bundle = Bundle()
        bundle.putLong(param, value)
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }
}