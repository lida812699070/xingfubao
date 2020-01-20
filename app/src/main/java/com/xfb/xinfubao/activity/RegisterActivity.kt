package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.RegisterFragment
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : DefaultActivity(), ViewPager.OnPageChangeListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView(savedInstanceState: Bundle?) {
        val fragments = arrayListOf<Fragment>()
        val titles = arrayListOf("手机注册", "邮箱注册")
        fragments.add(RegisterFragment.newInstance(true))
        fragments.add(RegisterFragment.newInstance(false))
        vpContent.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        tabLayout.setupWithViewPager(vpContent)
        vpContent.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(p0: Int) {

    }

}
