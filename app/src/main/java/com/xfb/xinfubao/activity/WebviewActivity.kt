package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.fragment.WebviewFragment
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : DefaultActivity() {

    private var url: String? = null
    private var html: String? = null
    private var title: String? = null
    private var subTitle: String? = null
    private var rightImage = 0
    private var fragment: WebviewFragment? = null

    companion object {
        fun newInstanceUrl(
            context: Context,
            url: String,
            title: String = "新闻详情",
            subTitle: String = "",
            rightImage: Int = 0
        ) {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            intent.putExtra("subTitle", subTitle)
            intent.putExtra("rightImage", rightImage)
            context.startActivity(intent)
        }

        fun newInstanceHtml(
            context: Context,
            html: String,
            title: String = "新闻详情",
            subTitle: String = "",
            rightImage: Int = 0
        ) {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("html", html)
            intent.putExtra("title", title)
            intent.putExtra("subTitle", subTitle)
            intent.putExtra("rightImage", rightImage)
            context.startActivity(intent)
        }

    }


    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun initView(savedInstanceState: Bundle?) {
        html = intent.getStringExtra("html")
        url = intent.getStringExtra("url")
        title = intent.getStringExtra("title")
        subTitle = intent.getStringExtra("subTitle")
        rightImage = intent.getIntExtra("rightImage", 0)
        if (!TextUtils.isEmpty(html)) {
            fragment = WebviewFragment.newInstanceHtml(html!!)
        } else if (!TextUtils.isEmpty(url)) {
            fragment = WebviewFragment.newInstanceUrl(url!!)
        } else {
            showMessage("没有数据")
            finish()
        }
        fragment?.let {
            val beginTransaction = supportFragmentManager.beginTransaction()
            beginTransaction.add(R.id.flContent, fragment!!, "fragment")
            beginTransaction.show(fragment!!)
            beginTransaction.commitAllowingStateLoss()
        }

        myToolbar.setTitle(title)
        myToolbar.setClick { finish() }
        if (!TextUtils.isEmpty(subTitle)) {
            myToolbar.setRightClickStr(subTitle) {
                if ("反馈" == subTitle) {
                    WebviewActivity.newInstanceUrl(this, Constant.FEEDBACK, "意见反馈")
                }
            }
        }

        if (rightImage != 0) {
            myToolbar.setRightClickRes(rightImage) {
                if (R.mipmap.fenxiang_icon == rightImage) {
                    //TODO 分享
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (true == fragment?.webViewCanGoBack()) {
            return true
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }
}
