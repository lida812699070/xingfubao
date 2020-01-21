package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.BalanceFragment
import kotlinx.android.synthetic.main.activity_yin_xingbao.*

/** 银杏宝 */
class YinXingbaoActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_yin_xingbao
    }

    override fun initView(savedInstanceState: Bundle?) {
        val titles = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        titles.add("全部")
        titles.add("全部")
        titles.add("全部")
        fragments.add(BalanceFragment())
        fragments.add(BalanceFragment())
        fragments.add(BalanceFragment())
        vpContent.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        tabLayout.setupWithViewPager(vpContent)

        tvToCashOut.setOnClickListener {
            startActivity(Intent(this, ApplyCashOutActivity::class.java))
        }
    }

}
