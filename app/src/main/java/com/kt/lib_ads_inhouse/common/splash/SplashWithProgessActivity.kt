package com.kt.lib_ads_inhouse.common.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.lib.admob.bannerAds.collapsible.BannerGravity
import com.lib.admob.interstitialAds.InterManager
import com.lib.admob.resumeAds.AppOpenResumeManager
import com.lib.admob.splashAds.SplashAdManager
import com.lib.admob.splashAds.callback.SplashCallback
import com.lib.ump.AdsConsentManager
//import com.lib.ump.AdsConsentManager2
import com.util.CheckInternet
import com.util.FirebaseLogEventUtils
import com.util.RemoteConfig
import com.util.SystemSharePreferenceUtils
import com.util.appupdate.AppUpdateManager
import com.util.appupdate.RemoteConfigVersionUpdateModel
import com.util.utm.CampaignInfo
import com.v1.chat.anything.BuildConfig
import com.v1.chat.anything.R
import com.v1.chat.anything.a1_common_utils.RemoteConfigKey
import com.kt.lib_ads_inhouse.a1_common_utils.ad.AdCommon
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdAllModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdAppopenResumeModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdBannerModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdSplashModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.screens.RemoteConfigScreenSplashModel
import com.v1.chat.anything.a8_app_utils.Constants
import com.v1.chat.anything.a8_app_utils.SystemUtil
import com.v1.chat.anything.databinding.ActivitySplashBinding
import com.kt.lib_ads_inhouse.common.language.LanguageStartActivity
import com.kt.lib_ads_inhouse.common.no_internet.NoInternetActivity

class SplashWithProgessActivity : AppCompatActivity() {

    private val TAG ="SplashWithProgessActivity"

    private lateinit var binding: ActivitySplashBinding

    //ad and remote config model
    var remoteConfigScreenSplashModel: RemoteConfigScreenSplashModel? = null
    var remoteConfigAdSplashBanner: RemoteConfigAdBannerModel? = null
    private var appopen_resumeAdModel: RemoteConfigAdAppopenResumeModel? = null
    lateinit var splashAdManager: SplashAdManager

    //app version
    var remoteConfigVersionUpdateModel: RemoteConfigVersionUpdateModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        //VIET ANH fix show logo on splash
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //Truong fix bug crash splash product
        //setContentView(R.layout.activity_splash)

        //binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //disable resume ads de khong show resume o splash
        AppOpenResumeManager.setEnableAdsResume(false)

        //log campaign info
        CampaignInfo.getCampaignInfo(this, null)

        //Truong 20241008 add check internet vi check intenet o duoi khong chay duoc
        if (!CheckInternet.haveNetworkConnection(this@SplashWithProgessActivity)){
            startActivity(Intent(this,NoInternetActivity::class.java))
            finishAffinity()
        }

        //init ad lib and process splash, remote config...
        MobileAds.initialize(this@SplashWithProgessActivity) {
            InterManager.init(System.currentTimeMillis())

            //testUMP here
            splashProcessing()
        }

    }

    override fun onResume() {
        super.onResume()

        //khong hien thi resume o splash
        AppOpenResumeManager.setEnableAdsResume(false)

    }


    private fun splashProcessing() {

        // Required ump để cho người dùng cấp
        //ngoai EU khong hien ra popup
        //trong EU lan 1 hien ra popup
        //trong EU lan 2 khong hien ra pupup mac du lan truoc la true hay false
        AdsConsentManager.getInstance(this).gatherConsent(
            this@SplashWithProgessActivity,
            Constants.UMP_TEST_DEVICE_ID)
        { result ->
            getRemoteConfigAndInitAds()
            Log.v("AdsConsentManager", "canRequestAds: "+AdsConsentManager.getInstance(this).canRequestAds)
        }
    }

    private fun getRemoteConfigAndInitAds() {
        // Khởi tạo firebase
        //fix move init firebase in application
        //FirebaseApp.initializeApp(baseContext)

        // Lấy config từ firebase
        RemoteConfig.initRemoteConfig(R.xml.remote_config_defaults) { task ->
            if (task.isSuccessful) {
                val update = task.result
                if (update == true) {
                    RemoteConfig.saveAllRemoteConfigString(
                        this@SplashWithProgessActivity,
                        RemoteConfigKey.getRemoteConfigStringKeyList()
                    )

                    RemoteConfig.saveAllRemoteConfigLong(
                        this@SplashWithProgessActivity,
                        RemoteConfigKey.getRemoteConfigLongKeyList()
                    )

                    RemoteConfig.saveAllRemoteConfigBoolean(
                        this@SplashWithProgessActivity,
                        RemoteConfigKey.getRemoteConfigBooleanKeyList()
                    )
                }
            }

            //get screen config
            remoteConfigScreenSplashModel = RemoteConfig.getConfigObject(
                this,
                RemoteConfigKey.screen_splash,
                RemoteConfigScreenSplashModel::class.java
            )

            remoteConfigAdSplashBanner = RemoteConfig.getConfigObject(
                this,
                RemoteConfigKey.ad_banner_splash,
                RemoteConfigAdBannerModel::class.java
            )

            //load banner
            //confirm banner lib call callback
            loadAndShowBanner()


            // update version
            setUpdateVersion()

            //init inter ad
            initInterAd()

            //init open resume ad
            initOpenResumeAd()

            //load and show splash
            //loadAndShowAdSplash()
            //delay 3s show ad splash
            //anh 20241117 add condition and get remote config delay value
            if ((remoteConfigAdSplashBanner?.is_show == true) and
                (remoteConfigScreenSplashModel?.is_show_banner_splash == true) and
                ((remoteConfigScreenSplashModel?.banner_splash_show_delay_time ?: 0L) > 0)) {
                val delayTimeMiniSecond = remoteConfigScreenSplashModel?.banner_splash_show_delay_time!! * 1000
                Handler(Looper.getMainLooper()).postDelayed({
                    loadAndShowAdSplash()
                }, delayTimeMiniSecond)
            }else
                loadAndShowAdSplash()

            //log event
            FirebaseLogEventUtils.logEventScreenView(this@SplashWithProgessActivity,"onCreat" )

            //increase app open count and log firebase
            SystemSharePreferenceUtils.updateAppOpenCount(this)
            val bundleAppOpen=Bundle()
            val count = SystemSharePreferenceUtils.getAppOpenCount(this)
            bundleAppOpen.putInt("v1_app_open_count", count)
            FirebaseLogEventUtils.logEventCommonWithName(this, "user_open_app", bundleAppOpen)
        }
    }

    private fun loadAndShowBanner(){
        //anh 20241117 ad condition
        if ((remoteConfigAdSplashBanner?.is_show == true) &&
            (remoteConfigScreenSplashModel?.is_show_banner_splash == true) &&
            ((remoteConfigScreenSplashModel?.banner_splash_show_delay_time ?: 0L) > 0)
        ) {
            AdCommon.loadAndShowAdBannerAdaptive(lifecycle,
                RemoteConfigKey.ad_banner_splash,
                binding.frAdBottom,
                BannerGravity.BOTTOM,
                null)
        }
    }

    private fun loadAndShowAdSplash() {
        val remoteConfigAdSplashModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.ad_splash,
            RemoteConfigAdSplashModel::class.java
        )

        if (remoteConfigAdSplashModel != null) {
            splashAdManager = SplashAdManager(
                this@SplashWithProgessActivity,
                remoteConfigAdSplashModel.ad_id_appopen,
                remoteConfigAdSplashModel.ad_id_inter,
                remoteConfigAdSplashModel.is_show_app_open,
                remoteConfigAdSplashModel.is_show_inter,
                remoteConfigAdSplashModel.getMaxLoadingTime(),
                remoteConfigAdSplashModel.appopen_rate,
                lifecycle,
                object : SplashCallback {
                    override fun onNextAction(isAppOpen: Boolean) {
                        super.onNextAction(isAppOpen)
                        nextActivity()
                    }
                }
            )
            splashAdManager.loadAndShowAdSplash()
        } else {
            nextActivity()
        }
    }

    private fun initInterAd() {
        val remoteConfigAdAllModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.ad_config_all,
            RemoteConfigAdAllModel::class.java
        )

        remoteConfigAdAllModel?.let {
            InterManager?.setTimeInterval(
                remoteConfigAdAllModel.getInterInterval(),
                remoteConfigAdAllModel.getInterStartInterval()
            )
        }
    }

    private fun initOpenResumeAd() {

        // Lấy config từ SharedPreferences
        appopen_resumeAdModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.ad_appopen_resume,
            RemoteConfigAdAppopenResumeModel::class.java
        )

        if ((appopen_resumeAdModel?.ad_id ?: "").isEmpty())
            return

        //set properties and preload for 1st ad
        AppOpenResumeManager.setId(appopen_resumeAdModel?.ad_id ?: "")
        AppOpenResumeManager.setIsShow(appopen_resumeAdModel?.is_show ?: true)
        AppOpenResumeManager.loadAdResume(this)

        //setting false for splash screen
        AppOpenResumeManager.setEnableAdsResume(false)
    }

    fun nextActivity() {

        //set appopen resume
        appopen_resumeAdModel?.let {
            AppOpenResumeManager.setEnableAdsResume(it.is_show)
        }

        //check next screen
        var nextScreen = SystemUtil.getActivityClass(
            remoteConfigScreenSplashModel?.next_screen_default,
            LanguageStartActivity::class.java
        )

        if (!CheckInternet.haveNetworkConnection(this@SplashWithProgessActivity)) {
            startActivity(Intent(this, NoInternetActivity::class.java))
        } else {
            startActivity(Intent(this, nextScreen))
            finishAffinity()
        }

    }

    private fun setUpdateVersion(){
        remoteConfigVersionUpdateModel = RemoteConfig.getConfigObject(
            this,
            RemoteConfigKey.version_update,
            RemoteConfigVersionUpdateModel::class.java
        )
        if (remoteConfigVersionUpdateModel != null) {
            AppUpdateManager.checkUpdate =
                AppUpdateManager.checkUpdate(BuildConfig.VERSION_NAME, remoteConfigVersionUpdateModel!!)

        }
    }
}