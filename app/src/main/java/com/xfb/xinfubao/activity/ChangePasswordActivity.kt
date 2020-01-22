package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_change_password.*

/** 修改密码 */
class ChangePasswordActivity : DefaultActivity() {
    private var changePasswordEnum = ChangePasswordEnum.CHANGE_LOGIN_PASSWORD

    companion object {
        fun toActivity(enum: ChangePasswordEnum?, context: Context) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            intent.putExtra("enum", enum)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        changePasswordEnum = intent.getSerializableExtra("enum") as ChangePasswordEnum
        when (changePasswordEnum) {
            ChangePasswordEnum.CHANGE_LOGIN_PASSWORD -> {

            }
            ChangePasswordEnum.SET_PAY_PASSWORD -> {
                etPasswordTop.hint = "请输入支付密码"
                etPasswordBottom.hint = "请再次输入密码"
                myToolbar.setTitle("设置支付密码")
            }
            ChangePasswordEnum.CHANGE_PAY_PASSWORD -> {
                etPasswordTop.hint = "请输入新的支付密码"
                etPasswordBottom.hint = "请再次输入密码"
                myToolbar.setTitle("修改支付密码")
            }
            ChangePasswordEnum.CHANGE_MOBILE -> {
                etMobile.hint = "请输入新的手机号码"
                etCheckCode.hint = "请输入验证码"
                etPasswordTop.hint = "请输入支付密码"
                etPasswordBottom.setVisible(false)
                myToolbar.setTitle("修改绑定手机")
            }
        }
    }

}
