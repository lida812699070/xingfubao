package com.xfb.xinfubao.fragment

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.fragment_webview.*

class WebviewFragment : BaseFragment() {

    private var url: String? = null
    private var html: String? = null

    companion object {
        fun newInstanceUrl(url: String?): WebviewFragment {
            val webviewFragment = WebviewFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            webviewFragment.arguments = bundle
            return webviewFragment
        }

        fun newInstanceHtml(html: String?): WebviewFragment {
            val webviewFragment = WebviewFragment()
            val bundle = Bundle()
            bundle.putString("html", html)
            webviewFragment.arguments = bundle
            return webviewFragment
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_webview
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        url = arguments?.getString("url")
        html = arguments?.getString("html")
        val webSettings = webView.settings
        webSettings.setGeolocationEnabled(true)
        //支持媒体资源自动播放
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // 根据cache-cont
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        // int fontSize = 10;
        // settings.setTextSize(TextSize.NORMAL);
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        object : Any() {
            fun setLoadWithOverviewMode(overview: Boolean) {
                webSettings.setLoadWithOverviewMode(overview)
            }
        }.setLoadWithOverviewMode(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            }
        }
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url)
        } else if (!TextUtils.isEmpty(html)) {
            webView.loadData(html, "text/html", "UTF-8")
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
                progressBar?.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }
        }
    }

}