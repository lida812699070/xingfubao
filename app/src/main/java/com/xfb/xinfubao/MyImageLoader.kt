package com.xfb.xinfubao

import android.content.Context
import android.widget.ImageView
import com.xfb.xinfubao.model.ProductImg
import com.xfb.xinfubao.utils.loadBanner
import com.youth.banner.loader.ImageLoader

class MyImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        if (path is ProductImg) {
            imageView.loadBanner(path.imgUrl)
        } else {
            imageView.loadBanner(path.toString())
        }
    }
}
