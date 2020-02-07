package com.xfb.xinfubao.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.*
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.EventUserInfo
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.BlurBitmap
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadUriNoError
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MineFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    @SuppressLint("SetTextI18n")
    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        getUserInfo()
        //资产互兑
        itemMoneyChange.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, MoneyExchangeActivity::class.java))
            }
        }
        tvMineyxg.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.YING_XING_GUO)
            }
        }
        tvMineyxy.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.YING_XING_YE)
            }
        }
        tvMinejfsc.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.JI_FEN_SHANG_CHENG)
            }
        }
        tvMineNAT.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.NAT)
            }
        }
        itemYxb.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, YinXingbaoActivity::class.java))
            }
        }
        itemYlz.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.YUAN_LI_ZHI)
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
        ivHeader.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, UserInfoActivity::class.java))
            }
        }
        ivSetting.setOnClickListener {
            activity?.let {
                startActivity(Intent(it, UserInfoActivity::class.java))
            }
//            val payModel = PayModel(
//                2,
//                "20200205191359384775487467328",
//                "alipay_sdk=alipay-sdk-java-4.7.12.ALL&app_id=2019020163174914&biz_content=%7B%22body%22%3A%22%25E7%25B2%2589%25E8%25B1%25A1%25E7%25BA%25A2%25E5%258C%25850.5%25E5%2585%2583%22%2C%22out_trade_no%22%3A%2220200205191359384775487467328%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%25E7%25B2%2589%25E8%25B1%25A1%25E7%25BA%25A2%25E5%258C%25850.5%25E5%2585%2583%22%2C%22timeout_express%22%3A%2210m%22%2C%22total_amount%22%3A%220.50%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.fenxianglife.com%2Fnjia%2Falipay%2Fnotify%2Fpayment%2FredEnvelope&sign=Gqk1UfJc09%2FZYXh1dLnTXVEGkpwOuxVNHbFu4uDNzkaes%2B3gxic35L2Pagb%2FI7CCu0MFVAhKKVa7WcxI3iInHr8BGQ14Z1MfRIxNt74s27aKxgyfTpSbEID8pXKDXiCLoGm6sZVBqiowB6%2FJTvmfGsHbTPYvF8KLO10o6sDFyUCp6C04LgdHdzetPzrm9A8%2F3UE02TylDpAAxBDxf7h2HUkr3CLSp92r166YBUSz%2FW2Kb%2FV0ShpK3uk4BkDEbznj4cU%2FHg2nO00GrFknMbc9gu8erVRo7%2BPdAJ9CDVPqTWG7qHwOzQ5WNXzyl5YcgXW9f5j0tEKj9wHeN29YpN3K6g%3D%3D&sign_type=RSA2&timestamp=2020-02-05+19%3A13%3A59&version=1.0",
//                null
//            )
//            payModel.payInfo = payModel.orderStr
//            PayUtils(activity).toPay(payModel, true)
        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            bindData(it.data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(data: UserInfo) {
        Glide.with(context)
            .load("${Constant.PIC_URL}${data.headIcon}")
            .placeholder(R.mipmap.touxiang)
            .error(R.mipmap.touxiang)
            .bitmapTransform(CropCircleTransformation(context))
            .into(object : SimpleTarget<GlideDrawable>() {
                override fun onResourceReady(
                    resource: GlideDrawable?,
                    glideAnimation: GlideAnimation<in GlideDrawable>?
                ) {
                    ivHeader.setImageDrawable(resource)
                    val bitmap = BlurBitmap.drawableToBitmap(resource)
                    ivBgMine.setImageBitmap(BlurBitmap.blur(activity!!, bitmap))
                }
            })
        tvName.text = data.name
        tvNikeName.text = data.nickName
        tvUserId.text = "NO.${data.userId}"
        ivVip.loadUriNoError(data.gradeIcon)
        ConfigUtils.saveUserInfo(data)
    }

    @Subscribe
    fun userInfoRequest(evnt: EventUserInfo) {
        getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}