package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.CommentUtils
import com.careagle.sdk.utils.MyBitmapUtils
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadUri
import kotlinx.android.synthetic.main.activity_cash_in_nat.*

/** 转入NAT */
class CashInNatActivity : DefaultActivity() {
    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, CashInNatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_in_nat
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        tvCashInAddressCopy.setOnClickListener {
            val primaryClip = ConfigUtils.getPrimaryClip(this)
            etCashInAddress.setText(primaryClip)
        }
        tvCashInCountCopy.setOnClickListener {
            val primaryClip = ConfigUtils.getPrimaryClip(this)
            etCashInCount.setText(primaryClip)
        }
        requestData()
    }

    private fun requestData() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).walletInfo(map)) {
            tvCashInAddress.text = it.data.walletAddress
            ivQRCode.loadUri(it.data.code)
            tvSaveQrCode.setOnClickListener {
                RxPermissionsUtil.requestPermission(
                    this@CashInNatActivity,
                    object : PermissionCallBack() {
                        override fun success() {
                            viewShot(clContent)
                            showMessage("图片保存成功")
                        }
                    },
                    Permission.WRITE_EXTERNAL_STORAGE
                )
            }
            tvCopyAddress.setOnClickListener {
                CommentUtils.copy(tvCashInAddress.text.toString())
                showMessage("已复制")
            }

            tvOk.setOnClickListener {
                cashin()
            }
        }
    }

    private fun cashin() {
        val strCashinAddress = etCashInAddress.text.toString()
        val strCashInCount = etCashInCount.text.toString()
        if (TextUtils.isEmpty(strCashinAddress)) {
            showMessage("请输入您的打币地址")
            return
        }
        if (TextUtils.isEmpty(strCashInCount)) {
            showMessage("请输入转入的NAT数量")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["moneyMakerAccount"] = strCashinAddress
        map["collectionAccount"] = tvCashInAddress.text.toString()
        map["money"] = strCashInCount
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).aForPayment(map)) {
            showMessage("转入成功")
            finish()
        }
    }


    /**
     * view截图
     * @return
     */
    fun viewShot(v: View) {
        val layoutParams = v.layoutParams
        // 核心代码start
        if (v.width == 0) return
        val bitmap = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(bitmap)
        v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
        v.draw(c)
        MyBitmapUtils.saveImageToGallery(this, bitmap)
        v.layoutParams = layoutParams
    }


}
