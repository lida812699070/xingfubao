package com.xfb.xinfubao.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.*
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadUriCircle
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    @SuppressLint("SetTextI18n")
    override fun initUI(view: View?, savedInstanceState: Bundle?) {

        ivHeader.loadUriCircle(ConfigUtils.mUserInfo?.headIcon)
        tvName.text = "${ConfigUtils.mUserInfo?.name}"
        tvNikeName.text = "${ConfigUtils.mUserInfo?.nickName}"
        tvUserId.text = "NO.${ConfigUtils.mUserInfo?.userId}"
        ivVip.setVisible(!TextUtils.isEmpty(ConfigUtils.mUserInfo?.grade))

        //资产互兑
        itemMoneyChange.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MoneyExchangeActivity::class.java))
            }
        }
        tvMineyxg.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, BalanceActivity::class.java))
            }
        }
        tvMineyxy.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, BalanceActivity::class.java))
            }
        }
        tvMinejfsc.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, BalanceActivity::class.java))
            }
        }
        tvMineNAT.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, BalanceActivity::class.java))
            }
        }
        itemYxb.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, YinXingbaoActivity::class.java))
            }
        }
        itemYlz.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, YinXingbaoActivity::class.java))
            }
        }
        itemNATJj.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, NATClubActivity::class.java))
            }
        }
        itemMyTeam.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyTeamActivity::class.java))
            }
        }
        ivMessage.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyMessageActivity::class.java))
            }
        }
        itemHelp.setOnClickListener {
            activity?.let {
                WebviewActivity.newInstanceUrl(it, Constant.HELP_CENTER, "帮助中心", "反馈")
            }
        }
        itemSafeCenter.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, SafeCenterActivity::class.java))
            }
        }
        itemAbout.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, AboutActivity::class.java))
            }
        }
        itemInvite.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, InviteFriendActivity::class.java))
            }
        }
        itemMarketOrder.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MyOrderActivity::class.java))
            }
        }
    }

}