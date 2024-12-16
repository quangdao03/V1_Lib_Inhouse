package com.kt.lib_ads_inhouse.a1_common_utils.ad

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdBannerModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdInterModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.ads.RemoteConfigAdNativeModel
import com.lib.admob.bannerAds.adaptive.AdaptiveBannerManager
import com.lib.admob.bannerAds.base.BannerCallback
import com.lib.admob.bannerAds.collapsible.BannerGravity
import com.lib.admob.bannerAds.collapsible.CollapseBannerManager
import com.lib.admob.interstitialAds.InterManager
import com.lib.admob.interstitialAds.base.InterCallback
import com.lib.admob.nativeAds.advance.base.NativeCallback
import com.lib.admob.nativeAds.advance.manager.NativeAdvanceManager2
import com.util.RemoteConfig

object AdCommon {
    private val tag ="AdCommon"

    /**
     * for banner ad
     */
    fun loadAndShowAdBannerAdaptive(lifecycle: Lifecycle?,
                                    rmcfKey: String,
                                    frAd: FrameLayout,
                                    gravity: BannerGravity,
                                    bannerCallback: BannerCallback?= null):AdaptiveBannerManager?{

        // Lấy remote config
        var bannerConfigModel = RemoteConfig.getConfigObject(
            frAd.context,
            rmcfKey,
            RemoteConfigAdBannerModel::class.java
        )

        //check not show
        if  (bannerConfigModel == null || !bannerConfigModel.is_show) {
            return null
        }

        // Khởi tạo Manager
        var bannerAdaptiveManager: AdaptiveBannerManager? = null
        bannerConfigModel?.let { bannerConfigModel ->
             bannerAdaptiveManager = AdaptiveBannerManager(bannerConfigModel.ad_id, lifecycle, bannerCallback)
        }

        //set adInfo for log firebase
        var screenName:String =frAd.context.javaClass.simpleName
        bannerAdaptiveManager?.setBannerAdName(screenName+"_"+rmcfKey)

        //show ads
        bannerAdaptiveManager?.loadAndShowAdaptiveBanner(frAd)

        //hidden line
        //TOP thi hide line TOP, BOTTOM thi hide line botom
        if (gravity == BannerGravity.TOP)
            bannerAdaptiveManager?.setVisibleLineTop(false)
        else if (gravity == BannerGravity.BOTTOM)
            bannerAdaptiveManager?.setVisibleLineBottom(false)

        return bannerAdaptiveManager
    }

    //collapsible banner
    fun loadAndShowAdBannerCollapse(lifecycle: Lifecycle?,
                                    rmcfKey: String,
                                    frAd: FrameLayout,
                                    gravity: BannerGravity,
                                    bannerCallback: BannerCallback?= null): CollapseBannerManager?{

        // Lấy remote config
        var bannerConfigModel = RemoteConfig.getConfigObject(
            frAd.context,
            rmcfKey,
            RemoteConfigAdBannerModel::class.java
        )

        //check not show
        if  (bannerConfigModel == null || !bannerConfigModel.is_show) {
            return null
        }

        // Khởi tạo BannerManager
        var bannerCollapseManager: CollapseBannerManager? = null
        bannerConfigModel?.let { bannerConfigModel ->
            bannerCollapseManager = CollapseBannerManager(bannerConfigModel.ad_id, lifecycle, bannerCallback)
        }

        //set adInfo for log firebase
        var screenName:String =frAd.context.javaClass.simpleName
        bannerCollapseManager?.setBannerAdName(screenName+"_"+rmcfKey)

        //show ads
        bannerCollapseManager?.loadAndShowCollapBanner(frAd, gravity =  gravity)

        //hidden line
        //TOP thi hide line TOP, BOTTOM thi hide line botom
        if (gravity == BannerGravity.TOP)
            bannerCollapseManager?.setVisibleLineTop(false)
        else if (gravity == BannerGravity.BOTTOM)
            bannerCollapseManager?.setVisibleLineBottom(false)

        return bannerCollapseManager

    }

    /**
     * for native ad
     * layout in app
     */

    fun loadAndShowAdNativeAdvance(lifecycle: Lifecycle?,
                                    rmcfKey: String,
                                    frAd: FrameLayout,
                                    nativeCallback: NativeCallback?= null):NativeAdvanceManager2?{

        Log.v(tag,"loadAndShowAdNativeAdvance, ${nativeCallback} ")

        var nativeAdvanceManager2: NativeAdvanceManager2? = null

        // Lấy remote config
        var nativeModel = RemoteConfig.getConfigObject(
            frAd.context,
            rmcfKey,
            RemoteConfigAdNativeModel::class.java
        )

        //check not show
        if  (nativeModel == null || !nativeModel.is_show) {
            return nativeAdvanceManager2
        }

        // Khởi tạo nativeManager
        nativeModel?.let { nativeModel ->
            nativeAdvanceManager2 = NativeAdvanceManager2(nativeModel.ad_id, lifecycle)
        }

        //set adInfo for log firebase
        var screenName:String =frAd.context.javaClass.simpleName
        nativeAdvanceManager2?.setNativeInfo(screenName+"_"+rmcfKey,
            nativeModel.max_preload)

        //get layout in app
        var native_layout_file = nativeModel?.ad_layout_file
        val nativeLayout: NativeLayoutInapp? = native_layout_file?.let {
            NativeAdvanceLayoutManager.getNativeLayoutInApp(
                it
            )
        }

       //adjust layout bg_color and border_color
        var drawable = getDrawableFile(nativeModel)

        //show ad
        nativeLayout?.let {
            nativeAdvanceManager2?.loadAndShowNative(
                frAd,
                it.layoutNativeId,
                it.shimmerLayoutId,
                drawable,
                nativeCallback
            )
        }

        //setting auto reload timer
        nativeModel?.let {
            nativeAdvanceManager2?.setReloadTimerInfo(it.is_auto_reload_timer, it.ad_reload_timer_interval)
        }

        //setting for fan
        nativeModel?.let {
            nativeAdvanceManager2?.setChangeAdCtrWhenFan(
                it.is_change_disable_click_all_when_fan,
                it.is_change_button_click_only_when_fan
            )
        }

        return nativeAdvanceManager2

    }

    fun loadAndShowNativeOrBanner(lifecycle: Lifecycle?,
                                  adType: String?,
                                  frAd: FrameLayout?,
                                  rmcfKeyNativeAdvance: String?=null,
                                  rmcfKeyBannerCollap: String?=null,
                                  rmcfKeyBannerAdaptive: String?=null,
                                  bannerGravity: BannerGravity? = null,
                                  nativeCallback: NativeCallback?= null,
                                  bannerCallback: BannerCallback?= null): Any?{

        if ((adType.isNullOrEmpty()) ||(frAd == null))
            return null

        return when (adType) {
            "native_advance" -> {
                loadAndShowAdNativeAdvance(lifecycle,rmcfKeyNativeAdvance!!,frAd!!,nativeCallback)

            }
            "banner_collapsible" -> {
                this.loadAndShowAdBannerCollapse(lifecycle,rmcfKeyBannerCollap!!,frAd!!,bannerGravity!!,bannerCallback)

            }
            "banner_adaptive" -> {
                this.loadAndShowAdBannerAdaptive(lifecycle,rmcfKeyBannerAdaptive!!,frAd!!,bannerGravity!!,bannerCallback)
            }
            else -> null
        }

        return null
    }
    /**
     * for interstitial ad
     */
    fun loadInterAd(context: Context, rmcfKey: String, interCallback: InterCallback?= null) {
        //inter ad loading
        val interModel = RemoteConfig.getConfigObject(
            context,
            rmcfKey,
            RemoteConfigAdInterModel::class.java
        )
        if ((interModel == null) or (InterManager==null))
            return

        if (interModel?.ad_id.isNullOrBlank() or interModel?.ad_id.isNullOrEmpty())
            return

        if (interModel?.is_show == false)
            return

        interModel?.let {
            InterManager.loadInter(
                context,
                rmcfKey,
                it.ad_id,
                interModel.is_show,
                interModel.is_preload_after_show_ad,
                interModel.is_show_next_activity_before_ad,
                interCallback
            )
        }
    }

    fun showInterAd(activity: Activity, rmcfKey: String, interCallback: InterCallback?= null) {

        InterManager.showInter(activity, rmcfKey, interCallback)
    }

    /**
     * get drawable from remote config
     * using for adjust layout ad native
     */
    private fun getDrawableFile(remoteConfigAdNativeModel: RemoteConfigAdNativeModel):GradientDrawable?{

        try {
            var drawable :GradientDrawable?= null
            drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 16f
                setColor(Color.parseColor(remoteConfigAdNativeModel?.bg_color))
                setStroke(1, Color.parseColor(remoteConfigAdNativeModel?.border_color))
            }
            return drawable
        }catch (e:Exception){
            return null
        }
    }

}