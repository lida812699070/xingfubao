package com.xfb.xinfubao

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.xfb.xinfubao.activity.ProductDetailActivity
import com.xfb.xinfubao.model.BannerModel
import com.xfb.xinfubao.model.ProductImg
import com.xfb.xinfubao.utils.loadBanner
import com.youth.banner.loader.ImageLoader

class MyImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        if (path is ProductImg) {
            imageView.loadBanner(path.imgUrl)
        } else if (path is BannerModel) {
            imageView.loadBanner(path.imgUrl)
            imageView.setOnClickListener {
                context.startActivity(
                    Intent(context, ProductDetailActivity::class.java)
                        .putExtra("productId", path.linkUrl)
                )
            }
        } else {
            imageView.loadBanner(path.toString())
        }
    }
}
