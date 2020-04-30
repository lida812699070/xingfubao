package com.xfb.xinfubao.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.careagle.sdk.Config
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.utils.MyToast
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.PriceChangeUtils
import com.careagle.sdk.utils.RxPermissionsUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.UserInfoActivity
import com.xfb.xinfubao.model.NatActiveDetail
import com.xfb.xinfubao.model.NatUnlockPakeageModel
import com.xfb.xinfubao.model.NewsDetail
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.HttpUtils
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import com.xfb.xinfubao.wxapi.WXUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import java.io.File
import kotlin.concurrent.thread


class DialogUtils {

    companion object {
        /** NAT提币到钱包 */
        fun showBalanceDialog(
            context: Context,
            method: (strTokenAddress: String, strNATCount: String, strPayPassword: String) -> Unit
        ): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_balance, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            changeShopDialog?.show()
            val layoutParams = changeShopDialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            changeShopDialog?.window?.attributes = layoutParams
            changeShopDialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            changeShopDialog?.window?.setBackgroundDrawable(dw)
            val etTokenAddress = view.findViewById<EditText>(R.id.etTokenAddress)
            val tvCopy = view.findViewById<TextView>(R.id.tvCopy)
            val tvOkCashOut = view.findViewById<TextView>(R.id.tvOkCashOut)
            val etNATCount = view.findViewById<EditText>(R.id.etNATCount)
            val etPayPassword = view.findViewById<EditText>(R.id.etPayPassword)
            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                changeShopDialog.dismiss()
            }
            tvCopy.setOnClickListener {
                val primaryClip = ConfigUtils.getPrimaryClip(context)
                etTokenAddress.setText(primaryClip)
            }
            tvOkCashOut.setOnClickListener {
                val strTokenAddress = etTokenAddress.text.toString()
                val strNATCount = etNATCount.text.toString()
                val strPayPassword = etPayPassword.text.toString()
                method(strTokenAddress, strNATCount, strPayPassword)
            }
            return changeShopDialog
        }

        /** NAT提币到钱包 */
        fun showNATInputDialog(
            context: Context,
            data: NatActiveDetail,
            balance: Double,
            method: (strNATCount: String, strPayPassword: String) -> Unit
        ): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_input_nat_password, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            changeShopDialog?.show()
            val layoutParams = changeShopDialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            changeShopDialog?.window?.attributes = layoutParams
            changeShopDialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            changeShopDialog?.window?.setBackgroundDrawable(dw)
            val tvOkCashOut = view.findViewById<TextView>(R.id.tvOkCashOut)
            //起投金额
            val tvTextQiTouBiaoZhun = view.findViewById<TextView>(R.id.tvTextQiTouBiaoZhun)
            //标题
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            //Nat余额
            val tvNatBalance = view.findViewById<TextView>(R.id.tvNatBalance)
            val etNATCount = view.findViewById<EditText>(R.id.etNATCount)
            val etPayPassword = view.findViewById<EditText>(R.id.etPayPassword)

            var lessInputCount = 0.0
            if (data.inputType == 1) {//数量
                tvTitle.text = "同意投入"
                tvTextQiTouBiaoZhun.text = "起投标准：${data.natInputNum}"
                val color = context.resources.getColor(R.color.color_light_org)
                val strBalance = "NAT可以用余额：$balance"
                lessInputCount = data.natInputNum
                tvNatBalance.setColorText(
                    strBalance,
                    color,
                    9,
                    strBalance.length
                )
            } else {
                tvTitle.text = "同意按释放比例${data.natInputNum * 100}%投入"
                tvTextQiTouBiaoZhun.text = "起投标准：NAT释放量的${data.natInputNum * 100}%"
                val color = context.resources.getColor(R.color.color_light_org)
                val strBalance = "NAT可以用余额：$balance"
                lessInputCount = balance * data.natInputNum
                tvNatBalance.setColorText(
                    strBalance,
                    color,
                    9,
                    strBalance.length
                )
            }

            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                changeShopDialog.dismiss()
            }
            tvOkCashOut.setOnClickListener {
                val strNATCount = etNATCount.text.toString()
                val strPayPassword = etPayPassword.text.toString()
                if (TextUtils.isEmpty(strNATCount)) {
                    MyToast.toast("请输入Nat数量")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(strPayPassword)) {
                    MyToast.toast("请输入支付密码")
                    return@setOnClickListener
                }

                if (strNATCount.toDouble() > balance) {
                    MyToast.toast("投资金额大于所剩余额")
                    return@setOnClickListener
                }
                if (strNATCount.toDouble() < lessInputCount) {
                    MyToast.toast("投资金额小于起投数量")
                    return@setOnClickListener
                }
                method(strNATCount, strPayPassword)
            }

            return changeShopDialog
        }

        /** 抵押 */
        //state 0 抵押  1支付  2银杏宝余额转出 3质押 4使用 5同意释放
        fun showDiYaDialog(
            context: Context,
            state: Int = 0,
            redeemMoney: Double = 0.0,
            title: String? = "",
            method: (payPassword: String) -> Unit
        ): AlertDialog? {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_diya, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog?.show()
            val layoutParams = dialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            dialog?.window?.setBackgroundDrawable(dw)
            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                dialog?.dismiss()
            }
            if (state == 1) {
                view.findViewById<TextView>(R.id.tvTitle).text = "支付"
                view.findViewById<TextView>(R.id.tvOkCashOut).text = "支付"
            } else if (state == 2) {
                view.findViewById<TextView>(R.id.tvTitle).text = "转出余额"
                view.findViewById<TextView>(R.id.tvOkCashOut).text = "确认转出"
                view.findViewById<TextView>(R.id.tvOkCashOut).background =
                    context.getDrawable(R.drawable.shape_org_radius_8)
            } else if (state == 3) {
                val tvCanZhiyaCount = view.findViewById<TextView>(R.id.tvCanZhiyaCount)
                view.findViewById<TextView>(R.id.tvTitle).text = "质押"
                view.findViewById<TextView>(R.id.tvBg).setVisible(true)
                view.findViewById<TextView>(R.id.tvToZhiYaText).setVisible(true)
                tvCanZhiyaCount.setVisible(true)
                view.findViewById<TextView>(R.id.tvOkCashOut).text = "确认质押"
                tvCanZhiyaCount.text = "质押可获得NAT的数量：${PriceChangeUtils.getNumKb(redeemMoney)}"
            } else if (state == 4) {
                view.findViewById<TextView>(R.id.tvTitle).text = "使用"
                view.findViewById<TextView>(R.id.tvOkCashOut).text = "确认使用"
            } else if (state == 5) {
                view.findViewById<TextView>(R.id.tvTitle).text = title
                view.findViewById<TextView>(R.id.tvOkCashOut).text = "确认"
            }
            val etPayPassword = view.findViewById<EditText>(R.id.etPayPassword)
            view.findViewById<TextView>(R.id.tvOkCashOut).setOnClickListener {
                dialog.dismiss()
                method(etPayPassword.text.toString())
            }
            return dialog
        }

        /** 拍照 选择照片 */
        fun showTakePicDialog(context: Activity, takePic: (uri: Uri) -> Unit) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_take_pic, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog?.show()
            val layoutParams = dialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
                dialog?.dismiss()
            }
            view.findViewById<TextView>(R.id.tvSelectPic).setOnClickListener {
                RxPermissionsUtil.requestPermission(context, object : PermissionCallBack() {
                    override fun success() {
                        Matisse.from(context)
                            .choose(MimeType.ofImage())
                            .countable(true)
                            .imageEngine(GlideEngine())
                            .thumbnailScale(0.85f)
                            .forResult(UserInfoActivity.REQ_CHOICE_FROM_ALBUM)
                        dialog?.dismiss()
                    }
                }, Permission.WRITE_EXTERNAL_STORAGE)
            }
            view.findViewById<TextView>(R.id.tvTakePic).setOnClickListener {
                RxPermissionsUtil.requestPermission(context, object : PermissionCallBack() {
                    override fun success() {
                        // 步骤一：创建存储照片的文件
                        val path =
                            "${Config.getFileCacheDirPath()}${File.separator}images"
                        val file = File(path, "${System.currentTimeMillis()}.jpg")
                        if (!file.getParentFile().exists())
                            file.getParentFile().mkdirs()
                        val mUri: Uri?
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //步骤二：Android 7.0及以上获取文件 Uri
                            mUri =
                                FileProvider.getUriForFile(context, "com.jsf.piccompresstest", file)
                        } else {
                            //步骤三：获取文件Uri
                            mUri = Uri.fromFile(file)
                        }
                        //步骤四：调取系统拍照
                        val intent = Intent("android.media.action.IMAGE_CAPTURE")
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
                        context.startActivityForResult(
                            intent,
                            UserInfoActivity.REQUEST_TAKE_PHOTO_CODE
                        )
                        takePic(mUri)
                        dialog?.dismiss()
                    }
                }, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
            }
            dialog?.window?.setBackgroundDrawable(dw)
        }

        /** 拍照 选择照片 */
        fun showShareDialog(
            context: Activity,
            data: NewsDetail
        ) {
            if (TextUtils.isEmpty(data.iocurl)) {
                Toast.makeText(context, "没有图片数据", Toast.LENGTH_SHORT).show()
                return
            }
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_share_wx, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog?.show()
            val layoutParams = dialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            dialog?.window?.attributes = layoutParams
            dialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
                dialog?.dismiss()
            }
            val handler = Handler()
            view.findViewById<TextView>(R.id.tvShareWx).setOnClickListener {
                thread {
                    val netWorkBitmap =
                        HttpUtils.getNetWorkBitmap(data.iocurl)
                    handler.post {
                        //分享到微信
                        WXUtils.getInstance()
                            .shareWXUrl(
                                context,
                                data.url,
                                netWorkBitmap,
                                data.title,
                                data.content
                            )
                        dialog?.dismiss()
                    }
                }
            }
            view.findViewById<TextView>(R.id.tvShareWxCricler).setOnClickListener {
                thread {
                    val netWorkBitmap =
                        HttpUtils.getNetWorkBitmap(data.iocurl)
                    handler.post {
                        //分享到微信
                        WXUtils.getInstance()
                            .shareWXUrlCircle(
                                context,
                                data.url,
                                netWorkBitmap,
                                data.title,
                                data.content
                            )
                        dialog?.dismiss()
                    }
                }
            }
            dialog?.window?.setBackgroundDrawable(dw)
        }

        /**
         * 显示推荐样式的dialog
         */
        fun showSelect(
            context: Context,
            titles: String,
            strCancel: String = "取消",
            strOk: String = "确定",
            isCancel: Boolean = true,
            isNeedCancel: Boolean = true,
            subTitle: String? = null,
            cancel: () -> Unit = {},
            method: () -> Unit
        ): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_my_recommend_select, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            changeShopDialog?.show()
            changeShopDialog?.window?.setLayout(
                context.resources.getDimension(R.dimen.dp_360).toInt(),
                context.resources.getDimension(R.dimen.dp_174).toInt()
            )
            changeShopDialog?.window?.setDimAmount(0.2f)
            changeShopDialog?.window?.setBackgroundDrawable(context.resources.getDrawable(R.drawable.shape_white_radius_8))
            val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
            val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
            val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitle)
            val tvOk = view.findViewById<TextView>(R.id.tvOk)
            tvTitle.text = titles
            if (titles.length > 35) {
                tvTitle.textSize = 14f
            }
            tvCancel.text = strCancel
            tvOk.text = strOk
            tvCancel.setVisible(isCancel)
            if (!TextUtils.isEmpty(subTitle)) {
                tvSubTitle.setVisible(true)
                tvSubTitle.text = subTitle
            }
            tvCancel.setOnClickListener {
                cancel()
                changeShopDialog?.dismiss()
            }
            tvOk.setOnClickListener {
                method()
                if (isNeedCancel) {
                    changeShopDialog?.dismiss()
                }
            }
            return changeShopDialog
        }


        /**
         * 显示使用产品申请中
         */
        fun showUseApply(
            context: Context
        ): AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_apply_use, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            val ivClose = view.findViewById<ImageView>(R.id.ivClose)
            ivClose.setOnClickListener { changeShopDialog?.dismiss() }
            changeShopDialog?.show()
            changeShopDialog?.window?.setLayout(
                context.resources.getDimension(R.dimen.dp_360).toInt(),
                context.resources.getDimension(R.dimen.dp_251).toInt()
            )
            changeShopDialog?.window?.setDimAmount(0.2f)
            changeShopDialog?.window?.setBackgroundDrawable(context.resources.getDrawable(R.drawable.shape_white_radius_8))
            return changeShopDialog
        }

        fun showNatUnlockSelectDialog(
            context: Context,
            data: List<NatUnlockPakeageModel>,
            method: (item: NatUnlockPakeageModel) -> Unit
        ): AlertDialog? {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            val view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_nat_unlock, null)
            builder.setView(view)
            val changeShopDialog = builder.create()
            changeShopDialog?.show()
            val layoutParams = changeShopDialog?.window?.attributes
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams?.gravity = Gravity.BOTTOM
            changeShopDialog?.window?.attributes = layoutParams
            changeShopDialog?.window?.setDimAmount(0.4f)
            val dw = ColorDrawable(0x00)
            changeShopDialog?.window?.setBackgroundDrawable(dw)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewUnlock)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = object : BaseQuickAdapter<NatUnlockPakeageModel, BaseViewHolder>(
                R.layout.item_nat_unlock,
                data
            ) {
                override fun convert(helper: BaseViewHolder, item: NatUnlockPakeageModel) {
                    helper.setText(R.id.tvNATtUnLockTitle, item.name)
                        .setText(
                            R.id.tvNATtUnLockInfo,
                            "期数：${item.lockTime}个月  兑换比例：${PriceChangeUtils.getDoubleKb(item.unlockRatio)}"
                        )
                        .setVisible(R.id.ivNatClubRight, false)
                }
            }
            recyclerView.adapter = adapter
            adapter.setOnItemClickListener { adapter, view, position ->
                method(data[position])
            }
            return changeShopDialog
        }
    }
}