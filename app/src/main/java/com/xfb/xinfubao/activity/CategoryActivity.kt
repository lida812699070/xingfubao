package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.fragment.SearchFragment
import kotlinx.android.synthetic.main.activity_category.*

/** 搜索 分类 */
class CategoryActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_category
    }

    override fun initView(savedInstanceState: Bundle?) {
        val titles = arrayListOf<String>()
        titles.add("玉翠珠宝")
        titles.add("工艺作品")
        titles.add("文玩杂项")
        titles.add("紫砂陶瓷")
        titles.forEach {
            tabLayout.addTab(tabLayout.newTab().setText(it))
        }
        val beginTransaction = supportFragmentManager.beginTransaction()
        val searchFragment = SearchFragment()
        beginTransaction.add(R.id.flContent, searchFragment, "searchFragment")
        beginTransaction.show(searchFragment)
        beginTransaction.commitAllowingStateLoss()
    }

}
