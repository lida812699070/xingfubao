package com.xfb.xinfubao.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.utils.CommentUtils
import com.careagle.sdk.utils.MyBitmapUtils
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant.SERVER_API
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.ZXingUtils
import kotlinx.android.synthetic.main.activity_invite_friend.*


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
        ivQrCodeP.setImageBitmap(qrImage)
        //下载保存
        tvDownLoadSave.setOnClickListener {
            RxPermissionsUtil.requestPermission(
                this@InviteFriendActivity,
                object : PermissionCallBack() {
                    override fun success() {
                        viewShot(clPic)
                        showMessage("图片保存成功")
                    }
                },
                Permission.WRITE_EXTERNAL_STORAGE
            )
        }
        //复制链接
        tvCopyUrl.setOnClickListener {
            CommentUtils.copy(downloadUrl)
            showMessage("已复制")
        }
        tvInviteCode.text = ConfigUtils.mUserInfo.inviteCode
        tvInviteCodeP.text = ConfigUtils.mUserInfo.inviteCode
    }

    /**
     * view截图
     * @return
     */
    fun viewShot(v: View) {
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
    }

}
