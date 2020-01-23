package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.callback.MyClickCallBack
import com.xfb.xinfubao.fragment.ProductDetailFragment
import com.xfb.xinfubao.fragment.WebviewFragment
import com.xfb.xinfubao.model.ProductDetail
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : DefaultActivity() {
    var productId: String? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_product_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        productId = intent.getStringExtra("productId")
        val map = hashMapOf<String, String>()
        map["productId"] = "$productId"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getProductInfo(map)) {
            initFragments(it.data)
        }
    }

    private fun initFragments(data: ProductDetail) {
        val title = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        title.add("商品")
        val productDetailFragment = ProductDetailFragment.newInstance(data)
        fragments.add(productDetailFragment)
        title.add("详情")
        fragments.add(WebviewFragment.newInstanceHtml(data.productDetails))
        val pagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, title, this)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)

        productDetailFragment.myClickCallBack = object : MyClickCallBack {
            override fun myClick() {
                vpContent.setCurrentItem(1, true)
            }
        }
    }
}
