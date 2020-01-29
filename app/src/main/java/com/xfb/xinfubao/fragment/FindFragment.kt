package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant.REQUEST_ID
import com.xfb.xinfubao.model.FindNewsTab
import kotlinx.android.synthetic.main.fragment_find.*

class FindFragment : BaseFragment() {
    val fragments = arrayListOf<Fragment>()
    val titles = arrayListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_find
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        val map = hashMapOf<String, String>()
        map["id"] = REQUEST_ID
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).newsxfb(map)) {
            loadFragments(it.data)
        }
    }

    private fun loadFragments(data: List<FindNewsTab>?) {
        data?.forEach {
            titles.add(it.name)
            fragments.add(NewsFragment.newInstance("${it.typeid}"))
        }
        val pagerAdapter =
            MyFragmentPagerAdapter(childFragmentManager, fragments, titles, activity)
        vpContent.adapter = pagerAdapter
        tabLayout.setupWithViewPager(vpContent)
    }

}