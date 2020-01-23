package com.xfb.xinfubao

import android.content.Context
import android.widget.ImageView
import com.xfb.xinfubao.model.ProductImg
import com.xfb.xinfubao.utils.loadUri

import com.youth.banner.loader.ImageLoader

class MyImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        if (path is ProductImg) {
            imageView.loadUri(path.imgUrl)
        } else {
            imageView.loadUri(path.toString())
        }
    }
}
