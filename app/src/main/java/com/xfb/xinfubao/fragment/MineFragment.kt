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
import com.xfb.xinfubao.model.event.EventUserInfo
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.*
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
        itemYKT.setOnClickListener {
            activity?.let {
                BalanceActivity.toActivity(it, BalanceEnum.YI_KA_TONG)
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

    private fun changeUrl(uri: String?): String? {
        var url = uri
        if (uri != null && !uri.startsWith("http")) {
            url = Constant.PIC_URL + url
        }
        return url
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(data: UserInfo) {
        val changeUrl = changeUrl("${data.headIcon}")
        Glide.with(context)
            .load(changeUrl)
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
        ivKuangzhu.loadUri(data.minersLevelIcon)
        ivKuangzhu.setVisible(!TextUtils.isEmpty(data.minersLevelIcon))
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