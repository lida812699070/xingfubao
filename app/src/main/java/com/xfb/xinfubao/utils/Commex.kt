package com.xfb.xinfubao.utils

import android.content.Context
import android.graphics.Paint
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.TextView
import com.careagle.sdk.Config
import java.io.File


/**
 *  是否需要给父布局设置padding
 */

/*
    扩展点击事件，参数为方法 防止快速点击
 */
fun View.onClick(method: () -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= 500) {
            flag = false
        }
        lastClickTime = currentClickTime
        if (!flag) {
            method()
        }
    }
}

/**
 * 下划线
 */
fun TextView.downLine() {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    paint.isAntiAlias = true
}

/**
 * 中划线
 */
fun TextView.centerLine() {
    paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
    paint.isAntiAlias = true
}

fun TextView.setDrawLeft(resourceId: Int) {
    val drawable = getResources().getDrawable(resourceId)
/// 这一步必须要做,否则不会显示.
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    setCompoundDrawables(drawable, null, null, null)
}

/*
    扩展视图可见性
 */
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setInVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 * 带动画的隐藏显示s
 */
fun View.setVisibleWithAnim(visible: Boolean) {
    val translateAnimation = TranslateAnimation(
        0f, 0f, 0f, 0f
    )
    translateAnimation.duration = 200
    this.startAnimation(translateAnimation)
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

//fun Activity.checkPermission(permissions: Array<String>, method: () -> Unit) {
//    AndPermission.with(this)
//            .permission(permissions)
//            .rationale { _, _, executor -> executor.execute() }
//            .onGranted { method() }
//            .onDenied { ToastUtil.showToast(this, "请检查手机储存权限") }.start()
//}


fun TextView.setColorText(text: String, color: Int, start: Int, end: Int) {
    val spannableStringCoupon = SpannableString(text)
    spannableStringCoupon.setSpan(
        ForegroundColorSpan(color),
        start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = spannableStringCoupon
}

fun TextView.setColorText(color: Int, start: Int, end: Int) {
    setColorText(text.toString(), color, start, end)
}

fun EditText.indexEnd() {
    setSelection(text.toString().length)
}

/** 切换密码可见 */
fun EditText.changePasswordState(isHidden: Boolean) {
    if (isHidden) {
        //设置EditText文本为可见的
        transformationMethod = HideReturnsTransformationMethod.getInstance()
    } else {
        //设置EditText文本为隐藏的
        transformationMethod = PasswordTransformationMethod.getInstance()
    }
    postInvalidate()
    //切换后将EditText光标置于末尾
    val charSequence = text
    if (charSequence is Spannable) {
        val spanText = charSequence as Spannable
        Selection.setSelection(spanText, charSequence.length)
    }
}

fun Context.getFile(): File {
    val path =
        "${Config.getFileCacheDirPath()}${File.separator}images"
    val file = File(path, "${System.currentTimeMillis()}.jpg")
    if (!file.getParentFile().exists())
        file.getParentFile().mkdirs()
    return file
}