package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.xfb.xinfubao.MyApplication
import com.xfb.xinfubao.R
import com.xfb.xinfubao.fragment.*
import com.xfb.xinfubao.model.event.EventChangeTab
import com.xfb.xinfubao.model.event.EventExitApp
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : DefaultActivity() {

    private var findFragment: FindFragment? = null
    private var homeFragment: HomeFragment? = null
    private var natFragment: NatFragment? = null
    private var mineFragment: MineFragment? = null
    private var yxbFragment: YXBFragment? = null
    private var currentFragment: Fragment? = null
    private var exitTime = 0L

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun isNeedPaddingTop(): Boolean {
        return false
    }

    override fun isWhiteStatusBar(): Boolean {
        return false
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        tvTabLiulan.setOnClickListener {
            if (it.isSelected) return@setOnClickListener
            setSelectTab(it)
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (findFragment == null) {
                findFragment = FindFragment()
                beginTransaction.add(R.id.flContent, findFragment!!, "findFragment")
            }
            currentFragment?.let { beginTransaction.hide(it) }
            beginTransaction.show(findFragment!!)
            currentFragment = findFragment
            beginTransaction.commitAllowingStateLoss()
        }
        tvTabMine.setOnClickListener {
            setSelectTab(it)
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (mineFragment == null) {
                mineFragment = MineFragment()
                beginTransaction.add(R.id.flContent, mineFragment!!, "mineFragment")
            }
            currentFragment?.let { beginTransaction.hide(it) }
            beginTransaction.show(mineFragment!!)
            currentFragment = mineFragment
            beginTransaction.commitAllowingStateLoss()
        }
        tvTabMall.setOnClickListener {
            setSelectTab(it)
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (homeFragment == null) {
                homeFragment = HomeFragment()
                beginTransaction.add(R.id.flContent, homeFragment!!, "homeFragment")
            }
            currentFragment?.let { beginTransaction.hide(it) }
            beginTransaction.show(homeFragment!!)
            currentFragment = homeFragment
            beginTransaction.commitAllowingStateLoss()
        }

        tvTabYXB.setOnClickListener {
            setSelectTab(it)
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (yxbFragment == null) {
                yxbFragment = YXBFragment()
                beginTransaction.add(R.id.flContent, yxbFragment!!, "yxbFragment")
            }
            currentFragment?.let { beginTransaction.hide(it) }
            beginTransaction.show(yxbFragment!!)
            currentFragment = yxbFragment
            beginTransaction.commitAllowingStateLoss()
        }

        tvTabNat.setOnClickListener {
            setSelectTab(it)
            val beginTransaction = supportFragmentManager.beginTransaction()
            if (natFragment == null) {
                natFragment = NatFragment()
                beginTransaction.add(R.id.flContent, natFragment!!, "natFragment")
            }
            currentFragment?.let { beginTransaction.hide(it) }
            beginTransaction.show(natFragment!!)
            currentFragment = natFragment
            beginTransaction.commitAllowingStateLoss()
        }

        tvTabMall.performClick()
    }

    private fun setSelectTab(it: View) {
        tvTabLiulan.isSelected = false
        tvTabMine.isSelected = false
        tvTabMall.isSelected = false
        tvTabYXB.isSelected = false
        tvTabNat.isSelected = false
        it.isSelected = true
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            showMessage("再按一次，退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    @Subscribe
    fun exitApp(event: EventExitApp) {
        finish()
    }

    @Subscribe
    fun exitApp(event: EventChangeTab) {
        tvTabMall.performClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.isPopNatHist = false
        EventBus.getDefault().unregister(this)
    }
}