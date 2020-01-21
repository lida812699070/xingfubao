package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.careagle.sdk.callback.PermissionCallBack
import com.careagle.sdk.utils.Permission
import com.careagle.sdk.utils.RxPermissionsUtil
import com.xfb.xinfubao.R
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.utils.loadLocalUriCircle
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_user_info.*


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
            DialogUtils.showTakePicDialog(this@UserInfoActivity) {
                mUri = it
            }
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
