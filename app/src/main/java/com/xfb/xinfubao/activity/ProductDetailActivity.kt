package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.ProductDetailFragment
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : DefaultActivity() {
    var productId = 0L
    override fun getLayoutId(): Int {
        return R.layout.activity_product_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        productId = intent.getLongExtra("productId", 0L)
        val title = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        title.add("商品")
        title.add("详情")
        fragments.add(ProductDetailFragment())
        fragments.add(ProductDetailFragment())
        val pagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, title, this)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)

    }
}
