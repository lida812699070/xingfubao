package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.MyOrderFragment
import kotlinx.android.synthetic.main.activity_my_order.*

/** 我的订单 */
class MyOrderActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_my_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        val titles = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        titles.add("全部")
        titles.add("待付款")
        titles.add("待发货")
        titles.add("待收货")
        titles.add("已完成")
        fragments.add(MyOrderFragment.newInstance())
        fragments.add(MyOrderFragment.newInstance(100))
        fragments.add(MyOrderFragment.newInstance(102))
        fragments.add(MyOrderFragment.newInstance(112))
        fragments.add(MyOrderFragment.newInstance(106))
        val pagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)
    }
}
