package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.PublishFragment
import com.xfb.xinfubao.fragment.WebviewFragment
import kotlinx.android.synthetic.main.activity_my_message.*

/** 公告 站内信 */
class MyMessageActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_my_message
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        val title = arrayListOf<String>()
        title.add("公告")
        title.add("站内信")
        val fragments = arrayListOf<Fragment>()
        fragments.add(PublishFragment())
        fragments.add(WebviewFragment.newInstanceUrl("http://www.baidu.com"))
        val pagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, title, this)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)


    }
}
