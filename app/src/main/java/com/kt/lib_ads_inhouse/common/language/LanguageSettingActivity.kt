package com.kt.lib_ads_inhouse.common.language

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.v1.chat.anything.R
import com.v1.chat.anything.a1_common_utils.base.BaseActivity
import com.v1.chat.anything.a1_common_utils.base.BaseViewModel
import com.v1.chat.anything.databinding.ActivityLanguageSettingBinding
import com.v1.chat.anything.ui.main.MainActivity
import com.v1.chat.anything.a8_app_utils.SystemUtil
import java.util.Locale

class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding, BaseViewModel>() {
  
    private var languageAdapter: LanguageAdapter? = null
    private var lang: String = ""

    override fun createBinding(): ActivityLanguageSettingBinding {
        return ActivityLanguageSettingBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()

        val currentLocale = Locale.getDefault().language
        binding!!.ivCheck.setOnClickListener {
            //set reload native

            //other process
            SystemUtil.saveLocale(this@LanguageSettingActivity, lang)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        binding!!.ivBack.setOnClickListener {
            finish()
        }

        val listLanguage = mutableListOf<LanguageModel>()

        val listLang = listOf(
            LanguageModel("Hindi", "hi", false, R.drawable.ic_language_hi),
            LanguageModel("Spanish", "es", false, R.drawable.ic_language_es),
            LanguageModel("French", "fr", false, R.drawable.ic_language_fr),
            LanguageModel("Portuguese", "pt", false, R.drawable.ic_language_pt),
            LanguageModel("German", "de", false, R.drawable.ic_language_de),
            LanguageModel("Japanese", "ja", false, R.drawable.ic_language_jp),
            LanguageModel("Korea", "ko", false, R.drawable.ic_language_ko),
            LanguageModel("English", "en", false, R.drawable.ic_language_en),
        )

        listLanguage.addAll(listLang)
        val linearLayoutManager = LinearLayoutManager(this)

        for (i in listLang.indices) {
            val languageModel = listLang[i]
            if (languageModel.code.contains(SystemUtil.getPreLanguage(this).toString())) {
                listLanguage.remove(languageModel)
                listLanguage.add(0, languageModel)
                break
            }
        }

        languageAdapter =
            LanguageAdapter(this, listLanguage, object : LanguageAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    lang = listLanguage[position].code
                }

            })
        binding!!.rvLanguage.layoutManager = linearLayoutManager
        binding!!.rvLanguage.adapter = languageAdapter


        languageAdapter?.selectedItemPosition = 0

    }
}