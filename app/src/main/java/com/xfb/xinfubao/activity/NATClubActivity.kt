package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.MyFragmentPagerAdapter
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.fragment.BalanceFragment
import kotlinx.android.synthetic.main.activity_natclub.*

/** NAT基金会 */
class NATClubActivity : DefaultActivity() {
    var showDiYaDialog: AlertDialog? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_natclub
    }

    override fun initView(savedInstanceState: Bundle?) {
        val titles = arrayListOf<String>()
        val fragments = arrayListOf<Fragment>()
        titles.add("请选择产品")
        fragments.add(BalanceFragment())
        vpContent.adapter = MyFragmentPagerAdapter(supportFragmentManager, fragments, titles, this)
        tabLayout.setupWithViewPager(vpContent)

        ivFinish.setOnClickListener { finish() }
        //抵押记录
        tvDiYaRecord.setOnClickListener {
            startActivity(Intent(this, CashOutRecordActivity::class.java))
        }
        //去抵押
        tvToDiYa.setOnClickListener {
            showDiYaDialog = DialogUtils.showDiYaDialog(this) {
                startActivity(Intent(this, ApplyCashOutResultActivity::class.java))
                showDiYaDialog?.dismiss()
            }
        }
    }

}
