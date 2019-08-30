package com.leo.wan.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.leo.wan.R
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        toolbar.setNavigationOnClickListener { finish() }
        toolbar.inflateMenu(R.menu.more_item)
        toolbar.setOnMenuItemClickListener {
            share()
            true
        }
        webView.settings.apply {
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = false
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                toolbar.title = intent.getStringExtra("title")
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                toolbar.title = "加载失败"
                handler?.proceed()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                toolbar.title = "加载中..."
                super.onPageStarted(view, url, favicon)
            }
        }
        webView.loadUrl(intent.getStringExtra("url"))
    }

    /**
     * 分享
     */
    private fun share() {
        Intent().run {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(
                    R.string.share_article_url,
                    getString(R.string.app_name),
                    intent.getStringExtra("title"),
                    intent.getStringExtra("url")
            ))
            startActivity(Intent.createChooser(this, ""))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (webView != null) {
            val wv = webView.parent as ViewGroup
            wv.removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }

}
