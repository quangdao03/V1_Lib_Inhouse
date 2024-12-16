package com.kt.lib_ads_inhouse.a8_app_utils.common.ex

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kt.lib_ads_inhouse.R
import java.io.File

fun ImageView.loadImageByBitmap(image: Bitmap?) {
    Glide.with(this.context)
        .load(image)
        .placeholder(R.drawable.image_default)
        .into(this)
}

fun ImageView.loadImageByBitmap(link: String) {
    Glide.with(this.context)
        .asBitmap()
        .load(link)
        .placeholder(R.drawable.image_default)
        .into(this)
}

fun ImageView.loadImageByDrawable(image: Drawable? = null) {
    Glide.with(this.context)
        .load(image)
        .placeholder(R.drawable.image_default)
        .into(this)
}

fun ImageView.loadImageByFile(image: File?) {
    Glide.with(this.context)
        .load(image)
        .placeholder(R.drawable.image_default)
        .into(this)
}

fun ImageView.loadImageByLink(link: String) {
    Glide.with(this.context)
        .load(link)
        .placeholder(R.drawable.image_default)
        .into(this)
}

fun Bitmap.share(context: Context) {
    val bitmapPath: String = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        this,
        "ImageResult",
        "share imageResult"
    );
    val bitmapUri: Uri = Uri.parse(bitmapPath)
    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("image/png")
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    context.startActivity(Intent.createChooser(intent, "Share"))
}

fun ImageView.loadImage(
    url: String,
    onImageLoaded: () -> Unit = {},
    onError: () -> Unit = {}
) {
    Glide.with(this.context)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache để tăng hiệu suất
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                onError()
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                onImageLoaded()
                return false
            }
        })
        .into(this)
}