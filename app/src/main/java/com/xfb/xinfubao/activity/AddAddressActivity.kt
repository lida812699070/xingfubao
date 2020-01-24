package com.xfb.xinfubao.activity

import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.CommentUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ReceiveVo
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_add_address.*

/** 新增收货地址 */
class AddAddressActivity : DefaultActivity() {

    //保存时没有  新增有
    var receiveVo: ReceiveVo? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_add_address
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }

        receiveVo = intent.getSerializableExtra("data") as ReceiveVo?
        receiveVo?.let {
            etInputAddress.setTextValue(it.address)
            etDoorNo.setTextValue(it.doorplate)
            etConnectPeople.setTextValue(it.consignee)
            etMobile.setTextValue(it.phone)
            ivDefaultSwitch.isSelected = it.isDefault
        }
        tvSaveAddress.setOnClickListener {
            saveAddress()
        }

        ivDefaultSwitch.setOnClickListener {
            ivDefaultSwitch.isSelected = !ivDefaultSwitch.isSelected
        }
    }

    /** 保存收货地址 */
    private fun saveAddress() {
        val strAddress = etInputAddress.testValue()
        if (TextUtils.isEmpty(strAddress)) {
            showMessage("请输入收货地址")
            return
        }
        val strDoorNo = etDoorNo.testValue()
        if (TextUtils.isEmpty(strDoorNo)) {
            showMessage("请输入门牌号")
            return
        }
        val strUserName = etConnectPeople.testValue()
        if (TextUtils.isEmpty(strUserName)) {
            showMessage("请输入收货人姓名")
            return
        }
        val strMobile = etMobile.testValue()
        if (TextUtils.isEmpty(strMobile)) {
            showMessage("请输入收货人手机号")
            return
        }
        if (!CommentUtils.isMobile(strMobile)) {
            showMessage("请输入正确的手机号")
            return
        }

        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        receiveVo?.let {
            map["receiveId"] = "${it.receiveId}"
        }
        map["consignee"] = strUserName
        map["phone"] = strMobile
        map["address"] = strAddress
        map["doorplate"] = strDoorNo
        map["isDefault"] = "${ivDefaultSwitch.isSelected}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).addReceive(map)) {
            showMessage("地址保存成功")
            finish()
        }
    }
}
