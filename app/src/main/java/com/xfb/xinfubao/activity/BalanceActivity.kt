package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.fragment.BalanceFragment
import com.xfb.xinfubao.utils.onClick
import kotlinx.android.synthetic.main.activity_balancel.*

class BalanceActivity : DefaultActivity() {
    var showBalanceDialog: AlertDialog? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_balancel
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

        tvCash.onClick {
            //            showBalanceDialog = DialogUtils.showBalanceDialog(this)
            startActivity(Intent(this, TransferActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showBalanceDialog?.dismiss()
    }
}
