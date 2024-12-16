package com.kt.lib_ads_inhouse.common.intro


import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kt.lib_ads_inhouse.R
import com.kt.lib_ads_inhouse.a1_common_utils.RemoteConfigKey
import com.kt.lib_ads_inhouse.a1_common_utils.ad.AdCommon
import com.kt.lib_ads_inhouse.a1_common_utils.base.BaseActivity
import com.kt.lib_ads_inhouse.a1_common_utils.base.BaseViewModel
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.app.RemoteConfigAppAllModel
import com.lib.admob.interstitialAds.base.InterCallback
import com.lib.admob.nativeAds.advance.manager.NativeAdvanceManager2
import com.util.RemoteConfig
import com.kt.lib_ads_inhouse.a1_common_utils.model_remote_config.screens.RemoteConfigScreenIntroModel
import com.kt.lib_ads_inhouse.a8_app_utils.EventTracking

import com.kt.lib_ads_inhouse.common.intro.adapter.IntroAdapter
import com.kt.lib_ads_inhouse.common.intro.model.IntroModel
import com.kt.lib_ads_inhouse.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity<ActivityIntroBinding, BaseViewModel>() {

    private var nativeAdvanceManager2: NativeAdvanceManager2? = null
    //native full
    private var nativeFullScreenManager2: NativeAdvanceManager2? = null
    private var remoteConfigIntroModel: RemoteConfigScreenIntroModel? = null
    private var introAdapter: IntroAdapter? = null
    private var remoteConfigAppAllModel: RemoteConfigAppAllModel? = null
    override fun createBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        val text = getString(R.string.page_0_text)
        val spannable = SpannableString(text)
        AdCommon.loadInterAd(
            this, RemoteConfigKey.ad_inter_intro, null
        )
        binding.tvTitle.text = spannable
        remoteConfigAppAllModel = RemoteConfig.getConfigObject(
            this, RemoteConfigKey.app_config, RemoteConfigAppAllModel::class.java
        )

        remoteConfigIntroModel = RemoteConfig.getConfigObject(
            this, RemoteConfigKey.screen_intro, RemoteConfigScreenIntroModel::class.java
        )

        //list slide
        val listIntroSlide = mutableListOf<IntroModel>()

        //list ad show status
        val listAdShowStatus = mutableListOf<Boolean>()

        if (remoteConfigIntroModel?.is_slide1_show == true) {
            listIntroSlide.add(IntroModel(R.drawable.intro1_new))
            listAdShowStatus.add(remoteConfigIntroModel?.is_slide1_ad_show ?: true)
        }

        if (remoteConfigIntroModel?.is_slide2_show == true) {
            listIntroSlide.add(IntroModel(R.drawable.intro2_new))
            listAdShowStatus.add(remoteConfigIntroModel?.is_slide2_ad_show ?: true)
        }

        if (remoteConfigIntroModel?.is_slide3_show == true) {
            listIntroSlide.add(IntroModel(R.drawable.intro3_new))
            listAdShowStatus.add(remoteConfigIntroModel?.is_slide3_ad_show ?: true)
        }
        if (remoteConfigIntroModel?.is_slide4_show == true) {
            listIntroSlide.add(IntroModel(R.drawable.intro4_new))
            listAdShowStatus.add(remoteConfigIntroModel?.is_slide4_ad_show ?: true)
        }
        //ad native full to list
        val adNativeFullPos1 = (remoteConfigIntroModel?.ad_native_full_pos1?:0)-1
        if ((adNativeFullPos1>=0) and (adNativeFullPos1<=listIntroSlide.size)){
            listIntroSlide.add(adNativeFullPos1, IntroModel(0))
            listAdShowStatus.add(adNativeFullPos1, false)
        }

        val adNativeFullPos2 = (remoteConfigIntroModel?.ad_native_full_pos2?:0)-1
        if ((adNativeFullPos2 >=0) and (adNativeFullPos2<=listIntroSlide.size)){
            listIntroSlide.add(adNativeFullPos2, IntroModel(0))
            listAdShowStatus.add(adNativeFullPos2, false)
        }

        //continue processing
        introAdapter = IntroAdapter(this@IntroActivity, listIntroSlide)
        introAdapter?.list?.addAll(listIntroSlide)
        binding.vp2Intro.adapter = introAdapter

        binding.indicator.attachTo(binding.vp2Intro)
        binding.vp2Intro.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


        binding.vp2Intro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //ad processing
                processAdInSlide(listIntroSlide, listAdShowStatus, position)

                //get slide image position
                var imageNumber = 0
                val pos1= (remoteConfigIntroModel?.ad_native_full_pos1?:0)-1
                val pos2= (remoteConfigIntroModel?.ad_native_full_pos2?:0) -1
                if ((position == pos1) or (position == pos2))
                    return

                //convert from slide index 0-5 to slide image only index 0-3
                if (position<pos1)
                    imageNumber = position
                else if ((position>pos1) and (position<pos2))
                    imageNumber = position -1
                else if (position > pos2)
                    imageNumber = position - 2
                else
                    imageNumber = position
                when (position) {
                    0 -> {
                        EventTracking.logEvent(this@IntroActivity, EventTracking.v1_event_intro1_view)
                        binding.tvTitle.text = getString(R.string.welcome_to_beautify)
                        binding.tvContent.text =
                            getString(R.string.let_ai_help_you_discover_your_potential_beauty)
                    }
                    1 -> {
                        EventTracking.logEvent(this@IntroActivity, EventTracking.v1_event_intro2_view)
                        binding.tvTitle.text = getString(R.string.page_0_text)
                        binding.tvContent.text = getString(R.string.page_0_content)
                    }

                    2 -> {
                        EventTracking.logEvent(this@IntroActivity, EventTracking.v1_event_intro3_view)
                        binding.tvTitle.text = getString(R.string.page_1_text)
                        binding.tvContent.text = getString(R.string.page_1_content)
                    }

                    3 -> {
                        EventTracking.logEvent(this@IntroActivity, EventTracking.v1_event_intro4_view)
                        binding.tvTitle.text = getString(R.string.page_2_text)
                        binding.tvContent.text = getString(R.string.page_2_content)
                    }
                }
//
            }
        })
        binding.tvNext.tap {
            val currentPosition = binding.vp2Intro.currentItem
            when(binding.vp2Intro.currentItem){
                1 -> {
                    EventTracking.logEvent(this, EventTracking.v1_event_intro1_next)
                }
                2 -> {
                    EventTracking.logEvent(this, EventTracking.v1_event_intro2_next)
                }
                3 -> {
                    EventTracking.logEvent(this, EventTracking.v1_event_intro3_next)
                }
                4 -> {
                    EventTracking.logEvent(this, EventTracking.v1_event_intro4_next)
                }
            }
            binding.vp2Intro.setCurrentItem(currentPosition + 1, true)
            if (currentPosition + 1 == listIntroSlide.size) {
                AdCommon.showInterAd(this, RemoteConfigKey.ad_inter_intro, object : InterCallback {
                    override fun onNextAction() {

                        nextAction()
                    }
                })
            }
        }
        binding.ivCloseNativeFull.tap {
            processShowIntroImage()
            binding.vp2Intro.currentItem += 1
            lastSlideProcess(binding.vp2Intro.currentItem, listIntroSlide)
        }
    }

    override fun initAd() {
        nativeAdvanceManager2 = AdCommon.loadAndShowAdNativeAdvance(
            lifecycle, RemoteConfigKey.ad_native_intro, binding.frAdBottom, null
        )
        //ad native fullscreen
        nativeFullScreenManager2 = AdCommon.loadAndShowAdNativeAdvance(
            lifecycle,
            RemoteConfigKey.native_intro_full,
            binding.frAdFull,
            null
        )
    }

    override fun hideNavigationBar() {
        if (remoteConfigIntroModel?.is_hide_navi_menu == true) return super.hideNavigationBar()
        else return
    }

    private fun processAdInSlide(listIntroSlide:MutableList<IntroModel>,
                                 listAdShowStatus: MutableList<Boolean>,
                                 position: Int) {
        val isShowNativeFull = (listIntroSlide[position].image == 0)

        //show native full
        if (isShowNativeFull) {
            processShowAdNativeFull()

            //reload if not slide 0
            if (position != 0)
                nativeFullScreenManager2?.reloadNow()
        }
        //show native bottom if show_ad = true
        else if (listAdShowStatus[position]) {
            processShowIntroImage()
            binding.frAdBottom.visibility = View.VISIBLE

            //reload ad if not first slide because ad loaded and reload ok in remote config
            if ((position != 0) && (remoteConfigIntroModel?.is_reload_ad_when_change_slide==true))
                nativeAdvanceManager2?.reloadNow()

            //do not show native bottom
        }else{
            processShowIntroImage()
            binding.frAdBottom.visibility = View.GONE
        }


//        //show ad frame if show_ad = true
//        if (listAdShowStatus[position]) {
//            binding.frAdBottom.visibility = View.VISIBLE
//            //reload ad if not first slide because ad loaded
//            //if first slide no reload
//            if (position != 0)
//                nativeAdvanceManager2?.reloadNow()
//        } else
//            binding.frAdBottom.visibility = View.GONE
    }

    //hidden native bottom and image, show native full
    private fun processShowAdNativeFull(){
        binding.frAdFull.visibility = View.VISIBLE
        binding.ivCloseNativeFull.visibility = View.VISIBLE
        binding.frAdBottom.visibility= View.GONE
        binding.layoutIndicator.visibility= View.GONE
        binding.nsvIntro.visibility= View.GONE
        binding.ivCloseNativeFull.bringToFront()
    }

    //hidden native full, show image and native bottom
    private fun processShowIntroImage(){
        binding.frAdFull.visibility = View.GONE
        binding.ivCloseNativeFull.visibility = View.GONE
        binding.frAdBottom.visibility= View.VISIBLE
        binding.layoutIndicator.visibility= View.VISIBLE
        binding.nsvIntro.visibility= View.VISIBLE
    }


    override fun adjustLayout() {
        val bottomMargin: Int = remoteConfigIntroModel?.distance_button_next_vs_ad ?: 0

        val paramsTextView = binding.tvNext.layoutParams as MarginLayoutParams
        val paramsDotsIndicator = binding.indicator.layoutParams as MarginLayoutParams

        //textview button
        paramsTextView.setMargins(
            paramsTextView.leftMargin,
            paramsTextView.topMargin,
            paramsTextView.rightMargin,
            bottomMargin
        )

        //dot indicator
        paramsDotsIndicator.setMargins(
            paramsDotsIndicator.leftMargin,
            paramsDotsIndicator.topMargin,
            paramsDotsIndicator.rightMargin,
            bottomMargin
        )

        //font size
        var textSize:Float = remoteConfigIntroModel?.font_size ?: 20F
        binding.tvNext.textSize =textSize
    }

    private fun lastSlideProcess(currentPosition: Int, listIntroSlide: MutableList<IntroModel>){
        //last slide
        if (currentPosition + 1 == listIntroSlide.size) {
            //show inter and next screen
            AdCommon.showInterAd(this,
                RemoteConfigKey.ad_inter_intro,
                object : InterCallback {
                    override fun onNextAction() {
                        nextAction()
                    }
                }
            )
        }

    }

    private fun nextAction() {
        val nextScreen = SystemUtil.getActivityClass(
            remoteConfigIntroModel?.next_screen_default,
            HomeActivity::class.java
        )
        if (SharePrefUtils.countPermission(this) == 0) {
            startActivity(Intent(this@IntroActivity, PermissionActivity::class.java))
            finishAffinity()
        } else {
            startActivity(Intent(this@IntroActivity, nextScreen))
            finishAffinity()
        }

    }


}