package com.lib.admob.bannerAds.base

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.lib.R
import com.lib.databinding.AdsBannerLayoutLibBinding
import com.util.FirebaseLogEventUtils
import com.util.SystemSharePreferenceUtils


open class BannerManager(

    //remote config
    private val id: String,

    // Kiểm soát reload quảng cáo theo vòng đời
    lifecycle: Lifecycle? = null,

    // callback: thực hiện các hành động tại các thời điểm khác nhau của ads
    private val callback: BannerCallback? = null

) {
    //properties for debug log
    private val tag = "BannerManager"

    //properties for log event
    protected var adName: String ="banner"

    // Quảng cáo banner
    protected var adView: AdView? = null

    //banner layout binding
    //layout in lib ads_banner_layout_lib.xml
    //BannerLayoutBinding liên kết xml bằng cách co che android studio
    //BannerLayoutBinding = layout name + binding
    private var bannerLayoutBinding: AdsBannerLayoutLibBinding? = null

    //banner loading view
    //layout in lib ad_banner_layout_lib_shimmer.xml
    private var loadingView: View? = null

    // Frame chứa quảng cáo
    //child in containerLayout
    //layout in lib ad_banner_layout_lib.xml
    private var containerAd: FrameLayout? = null

    //parent containerLayout in app
    protected var containerLayout: FrameLayout? = null

    //reload va xu ly theo lifecycle cua activity
    init {
        lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when(event) {
                    Lifecycle.Event.ON_PAUSE -> adView?.pause()
                    Lifecycle.Event.ON_RESUME -> adView?.resume()
                    Lifecycle.Event.ON_DESTROY -> adView?.destroy()
                    else -> {}
                }
            }
        })
    }

    //set adName for log event
    fun setBannerAdName(value: String){
        this.adName=value
    }

    protected fun loadingLayout(containerLayout: FrameLayout, @ColorInt lineColor: Int = Color.BLACK) {

        //parent container
        this.containerLayout = containerLayout
        val context = containerLayout.context
        val layoutInflater = LayoutInflater.from(context)

        //get and set layout binding in lib
        val binding = AdsBannerLayoutLibBinding.inflate(layoutInflater, null, false)
        binding.lineTop.setBackgroundColor(lineColor)
        binding.lineBottom.setBackgroundColor(lineColor)
        bannerLayoutBinding = binding

        //set child container to parent.ad_area
        containerAd = binding.container

        //set loading view before load ads
        loadingView = LayoutInflater.from(context).inflate(R.layout.ads_banner_layout_lib_shimmer, null)
        containerAd?.addView(loadingView)
        this.containerLayout?.addView(binding.root)
    }


    protected fun showLoading() {
        loadingView?.isVisible = true
    }

    protected fun hideLoading() {
        loadingView?.isVisible = false
    }

    // Tạo ra adView mới
    protected fun createAdview(context: Context, adType:String) {
        adView = AdView(context)

        adView?.adUnitId = id
        val adSize = getAdSize(context)
        adView?.setAdSize(adSize)
        adView?.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.v(tag, "onAdFailedToLoad")
                containerLayout?.isVisible = false
                callback?.onAdFailToLoad()

                //log event firebase
                FirebaseLogEventUtils.logEventAdFailedToLoad(context, adName, id, p0.message)
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.v(tag, "onAdLoaded. isCollapse: ${adView?.isCollapsible} $adView")
                containerAd?.removeAllViews()
                containerAd?.addView(adView)
                adView?.setOnPaidEventListener {
                    callback?.onEarnRevenue(it)
                    Log.v(tag, "onEarnRevenue")

                    //increase user ltv
                    SystemSharePreferenceUtils.updateUserLTV(context,
                        (it.valueMicros / 1000000.0).toFloat()
                    )

                    //event firebase log
                    FirebaseLogEventUtils.logEventAdPaidRevenue(context, adName, id, it)
                }
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.v(tag, "onAdClicked")
                callback?.onAdClicked()

                //log event firebase
                FirebaseLogEventUtils.logEventAdClicked(context, adName, id)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.v(tag, "onAdImpression")

                //callback
                callback?.onAdImpression()

                //log event
                FirebaseLogEventUtils.logEventAdImpression(context, adName, id)

            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.v(tag, "onAdOpened")
                callback?.onAdOpened()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.v(tag, "onAdClosed")
                callback?.onAdClosed()
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
                Log.v(tag, "onAdSwipeGestureClicked")
                callback?.onAdSwipeGestureClicked()
            }
        }
    }

    //dung cho 2 banner adaptive va collapsible
    fun setVisibleLineTop(isVisible: Boolean) {
        bannerLayoutBinding?.lineTop?.isVisible = isVisible
    }

    //dung cho 2 banner adaptive va collapsible
    fun setVisibleLineBottom(isVisible: Boolean) {
        bannerLayoutBinding?.lineBottom?.isVisible = isVisible
    }

    // Tính kích thước của AdView
    private fun getAdSize(context: Context): AdSize {
        val displayMetrics = context.resources.displayMetrics
        displayMetrics.widthPixels
        val density = displayMetrics.density
        val adWidth = (displayMetrics.widthPixels / density).toInt()
        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        return adSize
    }
}
