package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.FileUtils
import com.careagle.sdk.utils.MyBitmapUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.api.UpLoadPicApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.EventUserInfo
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.getFile
import com.xfb.xinfubao.utils.loadUriCircle
import com.xfb.xinfubao.utils.loadUriNoError
import com.zhihu.matisse.Matisse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import java.io.File


/** 我的资料1 */
class UserInfoActivity : DefaultActivity() {
    companion object {
        const val REQ_CHOICE_FROM_ALBUM = 2
        const val REQUEST_TAKE_PHOTO_CODE = 3
        const val REQUEST_NAME = 4
        const val REQUEST_CORP_CODE = 5

    }

    var mUri: Uri? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_user_info
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        //头像
        tvHeader.setOnClickListener {
            DialogUtils.showTakePicDialog(this@UserInfoActivity) {
                mUri = it
            }
        }
        //昵称
        tvNikeNameText.setOnClickListener {
            startActivityForResult(Intent(this, InputNikeActivity::class.java), REQUEST_NAME)
        }
        //收货地址
        tvAddressText.setOnClickListener {
            startActivity(Intent(this, AddressManagerActivity::class.java))
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        getUserInfo(map)
    }

    private fun getUserInfo(map: HashMap<String, String>) {
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            bindData(it.data)
        }
    }

    private fun bindData(data: UserInfo) {
        ivHeader.loadUriCircle(Constant.PIC_URL + data.headIcon)
        if (TextUtils.isEmpty(data.nickName)) {
            tvNikeName.text = "未设置"
        } else {
            tvNikeName.text = data.nickName
        }
        tvUserId.text = "${data.userId}"
        ivVIPLevel.loadUriNoError(data.gradeIcon)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CHOICE_FROM_ALBUM -> {
                    val result = Matisse.obtainResult(data)
                    if (!result.isEmpty()) {
                        mUri = result[0]
                        corp(result[0])

                    }
                }
                REQUEST_TAKE_PHOTO_CODE -> {
                    mUri?.let {
                        corp(it)
                    }
                }
                REQUEST_NAME -> {
                    val name = data?.getStringExtra("name")
                    editUserInfo(nickName = name!!)
                }
                REQUEST_CORP_CODE -> {
                    data?.let {
                        val bundle = it.extras
                        val parcelable = bundle.getParcelable<Bitmap>("data")
                        val file = getFile()
                        MyBitmapUtils.saveBitmap2file(parcelable, file.absolutePath)
                        loadFile(file)
                    }
                }
            }
        }
    }

    private fun corp(uri: Uri) {
        // 调用系统中自带的图片剪裁
        val intent = Intent("com.android.camera.action.CROP")
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION.or(Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CORP_CODE);
    }

    fun editUserInfo(headShotUrl: String = "", nickName: String = "") {
        val map = hashMapOf<String, String>()
        map["headShotUrl"] = headShotUrl
        map["nickName"] = nickName
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).updateUser(map)) {
            if (!TextUtils.isEmpty(nickName)) {
                tvNikeName.text = nickName
            }
            if (!TextUtils.isEmpty(headShotUrl)) {
                ivHeader.loadUriCircle("${Constant.PIC_URL}$headShotUrl")
            }
            EventBus.getDefault().post(EventUserInfo())
        }
    }

    private fun loadUrl(uri: Uri?) {
        var file = File(FileUtils.getRealFilePath(this, uri))
        val tagFile = getFile()
        file = MyBitmapUtils().bitmapCompress(file, tagFile, 300, 300)
        loadFile(file)
    }

    private fun loadFile(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        showProgress("请稍候")
        RetrofitCreateHelper.createApi(UpLoadPicApi::class.java).uploadImages(body)
            .doFinally { hideProgress() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.code == 0) {
                    editUserInfo(headShotUrl = it.data)
                } else {
                    showMessage(it.msg)
                }
            }, {
                showMessage("上传图片失败")
            }, {})
    }
}
