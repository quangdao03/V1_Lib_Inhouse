/**
 * native manager special defend on mediation adsource
 * adsource: Fan
 */

package com.lib.admob.nativeAds.advance.manager

import androidx.lifecycle.Lifecycle
import com.google.android.gms.ads.nativead.NativeAdView
import com.util.FirebaseLogEventUtils

class NativeAdvanceManager2(
    private var ad_id: String,
    lifecycle: Lifecycle? = null
) : NativeAdvanceManager1(ad_id, lifecycle) {

    //for Fan sourceID list
    //for test ad "7068401028668408324" (admob test)
    private var listMediationFanSourceID = mutableListOf<String>("10568273599589928883","11198165126854996598")

    //if source is FAN, is disable click in ad?
    private var isChangeButtonClickOnlyWhenFan = true

    //ANH add 20241125
    private var isChangeDisableClickAllWhenFan = true

    private fun isMediationFanSource(): Boolean{
        var strAdSourceID = this.nativeAd?.responseInfo?.loadedAdapterResponseInfo?.adSourceId?:"0"
        return strAdSourceID in listMediationFanSourceID
    }

    //disable click in adNativeView
    private fun changeCtrWhenFan(){

        //ANH edit 20221125
        //ad disable click all ad when fan

//        if((!isChangeButtonClickOnlyWhenFan) && (!isChangeDisableClickAllWhenFan))
//            return
//
//        if (isMediationFanSource()){
//            this.nativeAdView?.let {
//                setClickButtonOnly(it,true)
//                var context = nativeAdView?.context
//                FirebaseLogEventUtils.logEventAdNativeChangeButtonClickOnly(context, adName, ad_id)
//            }
//        }
        val isFan:Boolean =isMediationFanSource()

        //for test
        //confirm comment out when release
        //isFan = false


        var changeRegion = 0 //not change
        if (isFan && isChangeButtonClickOnlyWhenFan)
            changeRegion = 1 //button only

        if (isFan && isChangeDisableClickAllWhenFan)
            changeRegion = 2 //all ad

        if (changeRegion ==1){
            this.nativeAdView?.let { it ->
                setClickButtonOnly(it,true)
                callback?.onChangeCtrWhenFan(it)
                val context = nativeAdView?.context
                FirebaseLogEventUtils.logEventAdNativeChangeAdCtr(context, adName, ad_id)
            }
        }else if (changeRegion ==2){
            this.nativeAdView?.let {
                setClickDisableAll(it,true)
                callback?.onChangeCtrWhenFan(it)
                val context = nativeAdView?.context
                FirebaseLogEventUtils.logEventAdNativeChangeAdCtr(context, adName, ad_id)
            }
        }

        //end edit
    }

    private fun setClickButtonOnly(adView: NativeAdView, onlyClickBtn: Boolean = true) {
        val elements = listOf(
            adView.mediaView,
            adView.headlineView,
            adView.bodyView,
            adView.iconView,
            adView.priceView,
            adView.starRatingView,
            adView.storeView,
            adView.advertiserView
        )
        elements.forEach {
            it?.isClickable = onlyClickBtn.not()
        }
    }

    private fun setClickDisableAll(adView: NativeAdView, clickDisable: Boolean = true) {
        val elements = listOf(
            adView.mediaView,
            adView.headlineView,
            adView.bodyView,
            adView.iconView,
            adView.priceView,
            adView.starRatingView,
            adView.storeView,
            adView.advertiserView,
            adView.callToActionView,
        )
        elements.forEach {
            it?.isClickable = clickDisable.not()
        }
    }


    fun setChangeAdCtrWhenFan(isChangeAll: Boolean, isChangeButtonOnly: Boolean) {
        isChangeDisableClickAllWhenFan = isChangeAll
        isChangeButtonClickOnlyWhenFan = isChangeButtonOnly
    }

    /**
     * override here to change click enable status in adview
     */
    override fun showAndPreloadAd() {
        super.showAndPreloadAd()
        changeCtrWhenFan()
    }


}