/**
 * CUONG
 * reload resume thi chua xu ly
 * reload by timer GG tu xu ly, fix time
 */
package com.lib.admob.bannerAds.adaptive

import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.AdRequest
import com.lib.admob.bannerAds.base.BannerManager
import com.lib.admob.bannerAds.base.BannerCallback
import com.lib.ump.AdsConsentManager
import com.util.CheckInternet

class AdaptiveBannerManager(
    id: String,
    lifecycle: Lifecycle? = null,

    //comfirm sao ko de callback ra ngoai nhu native
    callback: BannerCallback? = null
) : BannerManager(id, lifecycle, callback) {


    fun loadAndShowAdaptiveBanner(
        container: FrameLayout,
        @ColorInt lineColor: Int = Color.GRAY
    ) {
        loadingLayout(container, lineColor)
        showLoading()
        loadBanner(container.context)
    }

    // load banner
    private fun loadBanner(context: Context) {
        if (CheckInternet.haveNetworkConnection(context).not()
             || AdsConsentManager.getInstance(context).canRequestAds.not()
        ) {
            return
        }
        createAdview(context,"adaptive")
        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }
}
