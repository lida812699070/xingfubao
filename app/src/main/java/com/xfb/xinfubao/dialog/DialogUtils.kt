package com.xfb.xinfubao.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.Config
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.UserInfoActivity
import com.xfb.xinfubao.utils.setVisible
import com.xfb.xinfubao.wxapi.WXUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import java.io.File


class DialogUtils {

    companion object {
        /** NAT提币到钱包 */
        fun showBalanceDialog(context: Context): AlertDialog {
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
            view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
                changeShopDialog.dismiss()
            }
            return changeShopDialog
        }

        /** 抵押 */
        //state 0 抵押  1支付
        fun showDiYaDialog(
            context: Context,
            state: Int = 0,
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
            }
            val etPayPassword = view.findViewById<EditText>(R.id.etPayPassword)
            view.findViewById<TextView>(R.id.tvOkCashOut).setOnClickListener {
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
        fun showShareDialog(context: Activity) {
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
            view.findViewById<TextView>(R.id.tvShareWx).setOnClickListener {
                //分享到微信
                WXUtils.getInstance()
                    .shareWXUrl(
                        context,
                        "http://www.baidu.com",
                        WXUtils.resourceToByte(R.mipmap.logo, context),
                        "标题",
                        "详细描述xxx"
                    )
            }
            view.findViewById<TextView>(R.id.tvShareWxCricler).setOnClickListener {
                //分享到微信朋友圈
                WXUtils.getInstance()
                    .shareWXUrl(
                        context,
                        "http://www.baidu.com",
                        WXUtils.resourceToByte(R.mipmap.logo, context),
                        "标题",
                        "详细描述xxx"
                    )

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
                changeShopDialog?.dismiss()
            }
            return changeShopDialog
        }
    }
}