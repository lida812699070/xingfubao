package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.fragment.WebviewFragment
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : DefaultActivity() {

    private var url: String? = null
    private var html: String? = null
    private var title: String? = null
    private var fragment: WebviewFragment? = null

    companion object {
        fun newInstanceUrl(context: Context, url: String, title: String = "新闻详情") {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }

        fun newInstanceHtml(context: Context, html: String, title: String = "新闻详情") {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra("html", html)
            intent.putExtra("title", title)
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

        ivFinish.setOnClickListener {
            finish()
        }
        tvTitle.text = title!!

    }

}
