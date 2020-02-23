package com.xfb.xinfubao.activity

import android.os.Bundle
import com.careagle.sdk.Config
import com.careagle.sdk.utils.CommentUtils
import com.careagle.sdk.utils.MyBitmapUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant.SERVER_API
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.ZXingUtils
import kotlinx.android.synthetic.main.activity_invite_friend.*
import java.io.File

//https://fenxianghome.oss-cn-hangzhou.aliyuncs.com/apk/testapk/app-fengxiang-debug.apk
/** 邀请好友 */
class InviteFriendActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_invite_friend
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        val downloadUrl = SERVER_API + "/help/register/${ConfigUtils.mUserInfo.inviteCode}/1"
        val qrImage = ZXingUtils.createQRImage(downloadUrl, 500, 500)
        ivQrCode.setImageBitmap(qrImage)
        //下载保存
        tvDownLoadSave.setOnClickListener {
            val path =
                "${Config.getFileCacheDirPath()}${File.separator}images"
            val file = File(path, "${System.currentTimeMillis()}.jpg")
            val qrImagePath = MyBitmapUtils.saveBitmap2file(qrImage, file.absolutePath)
            CommentUtils.copyFile(
                qrImagePath,
                CommentUtils.getCameraPath() + System.nanoTime() + ".jpg",
                true
            )
            showMessage("图片保存成功")
        }
        //复制链接
        tvCopyUrl.setOnClickListener {
            CommentUtils.copy(downloadUrl)
        }
        tvInviteCode.text = ConfigUtils.mUserInfo.inviteCode
        //TODO 邀请好友接口
    }
}
