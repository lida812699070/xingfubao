package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.BalanceFragment
import kotlinx.android.synthetic.main.activity_my_team.*

/** 我的团队 */
class MyTeamActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_my_team
    }

    override fun initView(savedInstanceState: Bundle?) {
        val titles = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        titles.add("直推明细")
        fragments.add(BalanceFragment())
        vpContent.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        tabLayout.setupWithViewPager(vpContent)

    }

}
