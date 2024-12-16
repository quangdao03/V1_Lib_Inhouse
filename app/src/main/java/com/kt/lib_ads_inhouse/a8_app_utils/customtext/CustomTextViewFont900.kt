package  com.kt.lib_ads_inhouse.a8_app_utils.customtext

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.kt.lib_ads_inhouse.R


class CustomTextViewFont900 : AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
//        isSingleLine = true
        //focusable = FOCUSABLE
        //freezesText = true
        ellipsize = TextUtils.TruncateAt.MARQUEE

        marqueeRepeatLimit = -1
        isSelected = true
        val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.noto_black)
        if (typeface != null) {
            setTypeface(typeface)
        }
    }
}