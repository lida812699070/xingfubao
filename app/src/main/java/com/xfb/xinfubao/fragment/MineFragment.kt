package com.xfb.xinfubao.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.BlurBitmap
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    @SuppressLint("SetTextI18n")
    override fun initUI(view: View?, savedInstanceState: Bundle?) {
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
        ivVip.setVisible(!TextUtils.isEmpty(data.grade))
        ConfigUtils.saveUserInfo(data)
    }
}