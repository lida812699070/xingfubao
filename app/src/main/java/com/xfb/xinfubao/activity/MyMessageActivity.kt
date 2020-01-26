package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.fragment.PublishFragment
import com.xfb.xinfubao.model.FindNewsTab
import kotlinx.android.synthetic.main.activity_my_message.*

/** 公告 站内信 */
class MyMessageActivity : DefaultActivity() {
    val fragments = arrayListOf<Fragment>()
    val titles = arrayListOf<String>()
    override fun getLayoutId(): Int {
        return R.layout.activity_my_message
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        val map = hashMapOf<String, String>()
        map["deviceID"] = Constant.REQUEST_ID
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).noticexfb(map)) {
            initData(it.data)
        }
    }

    private fun initData(data: List<FindNewsTab>) {
        data.forEach {
            titles.add(it.name)
            fragments.add(PublishFragment.newInstance(it.typeid))
        }

        val pagerAdapter =
            MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)
    }
}
