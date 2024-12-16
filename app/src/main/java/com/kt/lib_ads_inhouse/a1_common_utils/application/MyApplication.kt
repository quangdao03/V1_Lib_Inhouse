package com.kt.lib_ads_inhouse.a1_common_utils.application

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.FirebaseApp
import com.kt.lib_ads_inhouse.a1_common_utils.RemoteConfigKey
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.app.RemoteConfigAppAllModel
import com.kt.lib_ads_inhouse.a8_app_utils.SharePrefUtils
import com.kt.lib_ads_inhouse.common.welcomeback.WelcomeBackActivity
import com.lib.admob.B9AdApplication
import com.lib.admob.resumeAds.callback.OpenCallback
import com.util.FirebaseLogEventUtils
import com.util.RemoteConfig
import com.util.SystemSharePreferenceUtils

class MyApplication: B9AdApplication() {
    override fun onCreate() {
        super.onCreate()
        SharePrefUtils.init(this)
        FirebaseApp.initializeApp(this)
    }
    var isRunningWelcomeback:Boolean=false
    override fun getCallback(): OpenCallback {
        return object : OpenCallback {
            override fun onNextAction() {

                var remoteConfigAppAllModel: RemoteConfigAppAllModel? = null
                remoteConfigAppAllModel = RemoteConfig.getConfigObject(
                    this@MyApplication,
                    RemoteConfigKey.app_config,
                    RemoteConfigAppAllModel::class.java
                )

                if (remoteConfigAppAllModel ==null)
                    return

                //if using is_show_wellcomeback_screen
                //for Welcomeback activity not display 2 times double
                if ((remoteConfigAppAllModel?.is_show_wellcomeback_screen == true) &&
                    (!isRunningWelcomeback)) {
                    val intent = Intent(this@MyApplication, WelcomeBackActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        //save firebase log event
        val bundleUserLTV= Bundle()
        val ltv = SystemSharePreferenceUtils.getUserLTV(baseContext)
        bundleUserLTV.putFloat("v1_ltv", ltv)
        bundleUserLTV.putString("v1_logged_at_e", "app_onStop")
        bundleUserLTV.putString("v1_logged_at_s", currentActivity?.javaClass?.simpleName)
        FirebaseLogEventUtils.logEventCommonWithName(baseContext, "user_ltv", bundleUserLTV)

    }
    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)

        //for Welcomeback activity not display 2 times double
        if (activity is WelcomeBackActivity)
            isRunningWelcomeback=true
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)

        //for Welcomeback activity not display 2 times double
        if (activity is WelcomeBackActivity)
            isRunningWelcomeback=false
    }
}
