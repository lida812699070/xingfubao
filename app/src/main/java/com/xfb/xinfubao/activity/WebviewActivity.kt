package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.fragment.WebviewFragment
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : DefaultActivity() {

    private var url: String? = null
    private var html: String? = null
    private var title: String? = null
    private var subTitle: String? = null
    private var rightImage = 0
    private var fragment: WebviewFragment? = null
    private var isNeedPadding: Boolean = false
    private var isPublish: Boolean = false
    private var newsId: Int? = 0

    companion object {
        fun newInstanceUrl(
            context: Context,
            url: String,
            title: String = "新闻详情",
            subTitle: String = "",
            rightImage: Int = 0,
            isNeedPadding: Boolean = false,
            isPublish: Boolean = false,
            newsId: Int? = 0
        ) {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            intent.putExtra("subTitle", subTitle)
            intent.putExtra("rightImage", rightImage)
            intent.putExtra("isNeedPadding", isNeedPadding)
            intent.putExtra("isPublish", isPublish)
            intent.putExtra("newsId", newsId)
            context.startActivity(intent)
        }

        fun newInstanceHtml(
            context: Context,
            html: String,
            title: String = "新闻详情",
            subTitle: String = "",
            rightImage: Int = 0,
            isNeedPadding: Boolean = false,
            isPublish: Boolean = false
        ) {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("html", html)
            intent.putExtra("title", title)
            intent.putExtra("subTitle", subTitle)
            intent.putExtra("rightImage", rightImage)
            intent.putExtra("isNeedPadding", isNeedPadding)
            intent.putExtra("isPublish", isPublish)
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
        isNeedPadding = intent.getBooleanExtra("isNeedPadding", false)
        isPublish = intent.getBooleanExtra("isPublish", false)
        rightImage = intent.getIntExtra("rightImage", 0)
        newsId = intent.getIntExtra("newsId", 0)
        if (!TextUtils.isEmpty(html)) {
            fragment = WebviewFragment.newInstanceHtml(html!!, isNeedPadding, isPublish)
        } else if (!TextUtils.isEmpty(url)) {
            fragment = WebviewFragment.newInstanceUrl(url!!, isNeedPadding, isPublish)
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
            //TODO 请求分享信息  分享成功后请求
            myToolbar.setRightClickRes(rightImage) {
                if (R.mipmap.fenxiang_icon == rightImage) {
                    DialogUtils.showShareDialog(this)
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
