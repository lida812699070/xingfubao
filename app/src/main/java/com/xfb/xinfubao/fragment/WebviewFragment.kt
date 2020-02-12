package com.xfb.xinfubao.fragment

import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.fragment_webview.*


class WebviewFragment : BaseFragment() {

    private var url: String? = null
    private var html: String? = null
    private var isNeedPadding: Boolean? = false
    private var isPublish: Boolean? = false

    companion object {
        fun newInstanceUrl(
            url: String?,
            isNeedPadding: Boolean = false,
            isPublish: Boolean = false
        ): WebviewFragment {
            val webviewFragment = WebviewFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("isNeedPadding", isNeedPadding)
            bundle.putBoolean("isPublish", isPublish)
            webviewFragment.arguments = bundle
            return webviewFragment
        }

        fun newInstanceHtml(
            html: String?,
            isNeedPadding: Boolean = false,
            isPublish: Boolean = false
        ): WebviewFragment {
            val webviewFragment = WebviewFragment()
            val bundle = Bundle()
            bundle.putString("html", html)
            webviewFragment.arguments = bundle
            bundle.putBoolean("isNeedPadding", isNeedPadding)
            bundle.putBoolean("isPublish", isPublish)
            return webviewFragment
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_webview
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        url = arguments?.getString("url")
        html = arguments?.getString("html")
        isNeedPadding = arguments?.getBoolean("isNeedPadding")
        isPublish = arguments?.getBoolean("isPublish")
        val webSettings = webView.settings
        webSettings.setGeolocationEnabled(true)
        //支持媒体资源自动播放
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT // 根据cache-cont
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        // int fontSize = 10;
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(false)
        webSettings.builtInZoomControls = true // 设置显示缩放按钮
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        val mWebView = webView
        //支持javascript
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        webSettings.loadWithOverviewMode = true
        if (true == isPublish) {
            webSettings.textZoom = 300
        } else {
            if (Build.VERSION.SDK_INT >= 19)
                webSettings.layoutAlgorithm =
                    WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING;
            else {
                webSettings.layoutAlgorithm =
                    WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
            }
        }
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (true == isNeedPadding) {
            layoutParams.setMargins(
                resources.getDimension(R.dimen.dp_12).toInt(),
                0,
                resources.getDimension(R.dimen.dp_12).toInt(),
                0
            )
            webView.layoutParams = layoutParams
        }
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
            val imgStyle = "<html><body><style> img{ width:100%; height:auto;}</style>";
            html = imgStyle + html + "</body></html>";
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.visibility = if (newProgress < 100) View.VISIBLE else View.GONE
                progressBar?.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }
        }
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url)
                return false // then it is not handled by default action
            }
        })
    }

    /**
     * 用于返回是否需要实现监听
     */
    fun webViewCanGoBack(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        } else {
            return false
        }
    }
}