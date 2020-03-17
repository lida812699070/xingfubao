package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.careagle.sdk.utils.SPUtils
import com.xfb.xinfubao.BuildConfig
import com.xfb.xinfubao.MyApplication
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.VersionInfoModel
import com.xfb.xinfubao.model.event.EventExitApp
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.UpdateUtils
import kotlinx.android.synthetic.main.activity_about.*
import org.greenrobot.eventbus.EventBus

class AboutActivity : DefaultActivity() {
    var showSelectDialog: AlertDialog? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setOnClickListener { finish() }
        tvServerProtocol.setOnClickListener {
            WebviewActivity.newInstanceUrl(this, Constant.PRIVITE_SERVER, "服务及隐私条款")
        }
        tvVersion.text = "当前版本号:${BuildConfig.VERSION_NAME}"
        tvExitApp.setOnClickListener {
            DialogUtils.showSelect(this, "是否退出应用？") {
                SPUtils.clear(MyApplication.getInstance())
                ConfigUtils.mUserInfo = null
                EventBus.getDefault().post(EventExitApp())
                finish()
            }
        }
        tvCheckUpdate.setOnClickListener {
            val map = hashMapOf<String, String>()
            map["appID"] = "1"
            showProgress("请稍候")
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).getEdition(map)) {
                if (it.data.editionvalue > BuildConfig.VERSION_CODE) {
                    //有新版本
                    toUpDateApp(it.data)
                } else {
                    showMessage("您已是最新版本")
                }
            }
        }
    }

    //去更新
    private fun toUpDateApp(data: VersionInfoModel) {
        if (TextUtils.isEmpty(data.downloadpath)) {
            showMessage("未找到新版本下载地址")
            return
        }
        RxPermissionsUtil.requestPermission(this, object : PermissionCallBack() {
            override fun success() {
                showSelectDialog =
                    DialogUtils.showSelect(this@AboutActivity, "发现新版本是否更新?", method = {
                        showSelectDialog?.dismiss()
                        UpdateUtils.downloadFile(this@AboutActivity, data.downloadpath)
                    })
            }
        }, Permission.WRITE_EXTERNAL_STORAGE)
    }
}
