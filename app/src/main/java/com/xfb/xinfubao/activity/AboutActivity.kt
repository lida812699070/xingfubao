package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.TextView
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.careagle.sdk.utils.SPUtils
import com.xfb.xinfubao.BuildConfig
import com.xfb.xinfubao.MyApplication
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.callback.DownloadCallBack
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils.Companion.showSelect
import com.xfb.xinfubao.model.event.EventExitApp
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.UpdateUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_about.*
import org.greenrobot.eventbus.EventBus

class AboutActivity : DefaultActivity() {
    var showSelectDialog: AlertDialog? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        tvServerProtocol.setOnClickListener {
            WebviewActivity.newInstanceUrl(this, Constant.PRIVITE_SERVER, "服务及隐私条款")
        }
        tvVersion.text = "当前版本号:${BuildConfig.VERSION_NAME}"
        tvExitApp.setOnClickListener {
            showSelect(this, "是否退出应用？") {
                SPUtils.clear(MyApplication.getInstance())
                ConfigUtils.mUserInfo = null
                EventBus.getDefault().post(EventExitApp())
                finish()
            }
        }
        tvCheckUpdate.setOnClickListener {
            RxPermissionsUtil.requestPermission(this, object : PermissionCallBack() {
                override fun success() {
                    requestVersionInfo()
                }
            }, Permission.WRITE_EXTERNAL_STORAGE)

        }
    }

    private fun requestVersionInfo() {
        val map = hashMapOf<String, String>()
        map["appID"] = "1"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getEdition(map)) {
            if (it.data.editionvalue > BuildConfig.VERSION_CODE) {
                //有新版本
                toUpDateApp(it.data.downloadpath)
            } else {
                showMessage("您已是最新版本")
            }
        }
    }

    //去更新
    @SuppressLint("SetTextI18n")
    private fun toUpDateApp(downloadpath: String?) {
        if (TextUtils.isEmpty(downloadpath)) {
            showMessage("未找到新版本下载地址")
            return
        }
        showSelectDialog =
            showSelect(
                this@AboutActivity,
                "发现新版本是否更新?",
                isNeedCancel = false,
                method = {
                    val tvSubTitle = showSelectDialog?.findViewById<TextView>(R.id.tvSubTitle)
                    tvSubTitle?.setVisible(true)
                    tvSubTitle?.text = "当前下载进度：0%"
                    UpdateUtils.downloadFile(
                        this@AboutActivity,
                        downloadpath!!,
                        object : DownloadCallBack {
                            override fun inProgress(progress: Int) {
                                tvSubTitle?.text = "当前下载进度：$progress%"
                            }

                            override fun finish(msg: String) {
                                showSelectDialog?.dismiss()
                                showMessage(msg)
                            }
                        })
                })

    }
}
