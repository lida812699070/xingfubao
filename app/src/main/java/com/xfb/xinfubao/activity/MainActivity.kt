package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.xfb.xinfubao.R
import com.xfb.xinfubao.fragment.FindFragment
import com.xfb.xinfubao.fragment.HomeFragment
import com.xfb.xinfubao.fragment.MineFragment
import com.xfb.xinfubao.model.event.EventExitApp
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : DefaultActivity() {

    private var findFragment: FindFragment? = null
    private var homeFragment: HomeFragment? = null
    private var mineFragment: MineFragment? = null
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
        tvTabFind.setOnClickListener {
            tvTabFind.isSelected = true
            tvTabMine.isSelected = false
            ivTabHome.isSelected = false
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
            tvTabFind.isSelected = false
            tvTabMine.isSelected = true
            ivTabHome.isSelected = false
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
        ivTabHome.setOnClickListener {
            tvTabFind.isSelected = false
            tvTabMine.isSelected = false
            ivTabHome.isSelected = true
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

        ivTabHome.performClick()
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}