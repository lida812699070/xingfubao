package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.utils.loadLocalUriCircle
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.File


/** 我的资料 */
class UserInfoActivity : DefaultActivity() {
    companion object {
        const val REQ_CHOICE_FROM_ALBUM = 2
        const val REQUEST_TAKE_PHOTO_CODE = 3
    }

    var mUri: Uri? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_user_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        tvHeader.setOnClickListener {
            RxPermissionsUtil.requestPermission(this, object : PermissionCallBack() {
                override fun success() {
                    DialogUtils.showTakePicDialog(this@UserInfoActivity, {
                        Matisse.from(this@UserInfoActivity)
                            .choose(MimeType.ofImage())
                            .countable(true)
                            .imageEngine(GlideEngine())
                            .thumbnailScale(0.85f)
                            .forResult(REQ_CHOICE_FROM_ALBUM)
                    }) {
                        // 步骤一：创建存储照片的文件
                        val path =
                            "$filesDir${File.separator}images${File.separator}${System.currentTimeMillis()}"
                        val file = File(path, "test.jpg")
                        if (!file.getParentFile().exists())
                            file.getParentFile().mkdirs()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //步骤二：Android 7.0及以上获取文件 Uri
                            mUri = FileProvider.getUriForFile(
                                this@UserInfoActivity,
                                "com.jsf.piccompresstest",
                                file
                            )
                        } else {
                            //步骤三：获取文件Uri
                            mUri = Uri.fromFile(file)
                        }
                        //步骤四：调取系统拍照
                        val intent = Intent("android.media.action.IMAGE_CAPTURE")
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE)
                    }
                }
            }, Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CHOICE_FROM_ALBUM -> {
                    val result = Matisse.obtainResult(data)
                    if (!result.isEmpty()) {
                        ivHeader.loadLocalUriCircle(result[0])
                    }
                }
                REQUEST_TAKE_PHOTO_CODE -> {
                    mUri?.let {
                        ivHeader.loadLocalUriCircle(it)
                    }
                }
            }
        }
    }

}
