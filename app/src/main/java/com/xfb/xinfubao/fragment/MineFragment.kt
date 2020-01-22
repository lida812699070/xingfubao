package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.*
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
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
                startActivity(Intent(it, HelpActivity::class.java))
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
    }

}