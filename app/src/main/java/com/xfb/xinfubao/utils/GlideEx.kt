package com.xfb.xinfubao.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.careagle.sdk.utils.DisplayUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


fun ImageView.loadUri(uri: String?) {
    val url = changeUrl(uri)
    Glide.with(context)
        .load(url)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .into(this)
}

private fun changeUrl(uri: String?): String? {
    var url = uri
    if (uri != null && !uri.startsWith("http")) {
        url = Constant.PIC_URL + url
    }
    return url
}

fun ImageView.loadUriNoError(uri: String?) {
    val url = changeUrl(uri)
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadBanner(uri: String?) {
    val url = changeUrl(uri)
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadUri(uri: String?, width: Int, height: Int) {
    Glide.with(context)
        .load(uri)
        .override(width, height)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .into(this)
}

fun ImageView.loadUriCircle(uri: String?) {
    val url = changeUrl(uri)
    Glide.with(context)
        .load(url)
        .placeholder(R.mipmap.touxiang)
        .error(R.mipmap.touxiang)
        .bitmapTransform(CropCircleTransformation(context))
        .into(this)
}

fun ImageView.loadLocalUriCircle(uri: Uri?) {
    Glide.with(context)
        .load(uri)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .bitmapTransform(CropCircleTransformation(context))
        .into(this)
}

fun ImageView.loadRound(uri: String?, radius: Float = 4f) {
    val url = changeUrl(uri)
    Glide.with(context)
        .load(url)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .bitmapTransform(
            RoundedCornersTransformation(
                context,
                DisplayUtil.dip2px(context, radius),
                0
            )
        )
        .into(this)
}


fun ImageView.loadResCircle(res: Int?) {
    if (res == null) return
    Glide.with(context)
        .load(res)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .bitmapTransform(CropCircleTransformation(context))
        .into(this)
}

fun ImageView.loadResCircle(uri: String?) {
    val url = changeUrl(uri)
    if (uri == null) return
    Glide.with(context)
        .load(url)
        .placeholder(R.mipmap.icon_loading)
        .error(R.mipmap.icon_loading)
        .bitmapTransform(CropCircleTransformation(context))
        .into(this)
}