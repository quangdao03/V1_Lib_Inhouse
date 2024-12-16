package com.kt.lib_ads_inhouse.a8_app_utils.common.ex

import android.webkit.MimeTypeMap
import java.io.File
import java.net.URLEncoder
import java.util.Locale

fun File.getMimeType(): String {
    val encoded: String = try {
        URLEncoder.encode(name, "UTF-8").replace("+", "%20")
    } catch (e: Exception) {
        name
    }

    return MimeTypeMap.getFileExtensionFromUrl(encoded).lowercase(Locale.getDefault())
}