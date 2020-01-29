package com.xfb.xinfubao.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.FileCallBack
import okhttp3.Call
import java.io.File

class UpdateUtils {

    companion object {
        fun downloadFile(context: Context, url: String) {
            val file = context.getApkFile()
            OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(object : FileCallBack(
                    file.parent,
                    file.name
                ) {

                    @SuppressLint("SetTextI18n")
                    override fun inProgress(progress: Float, currentSize: Long, total: Long) {
//                    tvUpdateProgress.setVisible(true)
//                    tvUpdateProgress.text =
//                        CommonUtils.getFormatSize(currentSize.toDouble()) + "/" + CommonUtils.getFormatSize(
//                            total.toDouble()
//                        )
////                        tvUpdateProgress.text = (100 * progress).toInt().toString() + "%"
//                    //移动进度文字的  进度*（屏幕-左右边距和40dp）
////                        tvUpdateProgress.translationX = (progress * (layoutContent.width - resources.getDimension(R.dimen.dp_40))) - resources.getDimension(R.dimen.dp_25)
//                    updateProgress.setProgress((100 * progress).toInt())
                        Log.e("tag", "${(100 * progress).toInt()}")
                    }

                    override fun onError(call: Call, e: Exception) {
//                    tvToUpdate.text = "更新"
//                    tvToUpdate.isSelected = false
//                    ToastMaker.showLongToast("请检查网络")
                    }

                    override fun onResponse(response: File) {
                        installationAPK(context, file)
//                    tvToUpdate.text = "更新"
//                    tvToUpdate.isSelected = false
                    }
                })
        }

        /**
         * @return void
         * @Description: 自动安装
         */
        private fun installationAPK(context: Context, updateFile: File) {
            // 下载完成，点击安装
            Log.e("tag", "installationAPK")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
                val apkUri = FileProvider.getUriForFile(
                    context,
                    "com.jsf.piccompresstest",
                    updateFile
                )//在AndroidManifest中的android:authorities值
                val install = Intent(Intent.ACTION_VIEW)
                install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive")
                context.startActivity(install)
            } else {
                val uri = Uri.fromFile(updateFile)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                context.startActivity(intent)
            }
        }

    }

}