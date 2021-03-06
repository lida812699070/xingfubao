package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.Config
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.FileUtils
import com.careagle.sdk.utils.MyBitmapUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.UserInfoActivity.Companion.REQUEST_TAKE_PHOTO_CODE
import com.xfb.xinfubao.activity.UserInfoActivity.Companion.REQ_CHOICE_FROM_ALBUM
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.api.UpLoadPicApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.event.EventAuth
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.getFile
import com.xfb.xinfubao.utils.loadUri
import com.xfb.xinfubao.utils.setVisible
import com.zhihu.matisse.Matisse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth_by_id_card.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File


/** 身份证认证 */
class AuthByIdCardActivity : DefaultActivity() {
    var mUri: Uri? = null
    private var isFront = true
    private var frontUrlStr = ""
    private var backUrlStr = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_auth_by_id_card
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        tvOK.setOnClickListener {
            auth()
        }

        ivFacePic.setOnClickListener {
            isFront = true
            DialogUtils.showTakePicDialog(this) {
                mUri = it
            }
        }
        ivBackPic.setOnClickListener {
            isFront = false
            DialogUtils.showTakePicDialog(this) {
                mUri = it
            }
        }
    }

    private fun auth() {
        val map = hashMapOf<String, String>()
        val rname = etName.text.toString()
        val rid = etIDCard.text.toString()
        if (TextUtils.isEmpty(rname)) {
            showMessage("请输入姓名")
            return
        }
        if (TextUtils.isEmpty(rid)) {
            showMessage("身份证号")
            return
        }
        if (TextUtils.isEmpty(frontUrlStr)) {
            showMessage("请选择身份证正面照")
            return
        }
        if (TextUtils.isEmpty(backUrlStr)) {
            showMessage("请选择身份证反面照")
            return
        }
        map["oppositeUrl"] = backUrlStr
        map["positiveUrl"] = frontUrlStr
        map["rid"] = rid
        map["rname"] = rname
        map["userid"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).checkidentitycard(map)) {
            AuthResultActivity.toActivity(this,1)
            EventBus.getDefault().post(EventAuth())
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CHOICE_FROM_ALBUM -> {
                    val result = Matisse.obtainResult(data)
                    if (!result.isEmpty()) {
                        loadUrl(result[0])
                    }
                }
                REQUEST_TAKE_PHOTO_CODE -> {
                    mUri?.let {
                        loadUrl(it)
                    }
                }
            }
        }
    }

    private fun loadUrl(uri: Uri?) {
        var file = File(FileUtils.getRealFilePath(this, uri))
        val tagFile = getFile()
        file = MyBitmapUtils().bitmapCompress(file, tagFile, 500, 500)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        showProgress("请稍候")
        RetrofitCreateHelper.createApi(UpLoadPicApi::class.java).uploadImages(body)
            .doFinally { hideProgress() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.code == 0) {
                    uploadPicSuccess(it.data)
                } else {
                    showMessage(it.msg)
                }
            }, {
                showMessage("上传图片失败")
            }, {})
    }

    private fun uploadPicSuccess(data: String?) {
        if (isFront) {
            ivFacePic.loadUri("$data")
            frontUrlStr = data!!
            ivReTakePicFace.setVisible(true)
            tvFacePic.setVisible(false)
        } else {
            ivBackPic.loadUri("$data")
            backUrlStr = data!!
            tvBackPic.setVisible(false)
            ivReTakePicBack.setVisible(true)
        }
    }
}
