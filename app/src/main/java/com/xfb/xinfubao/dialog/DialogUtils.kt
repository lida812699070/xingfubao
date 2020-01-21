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
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import java.io.File


class DialogUtils {

    companion object {
        /** 银杏宝 */
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
        fun showDiYaDialog(context: Context, method: (payPassword: String) -> Unit): AlertDialog? {
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
    }
}