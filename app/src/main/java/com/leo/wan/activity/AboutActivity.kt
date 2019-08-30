package com.leo.wan.activity

import android.content.Context
import android.os.Bundle
import android.text.Html
import com.leo.wan.R
import kotlinx.android.synthetic.main.activity_about2.*

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about2)
        init()
    }

    private fun init() {
        toolbar.setNavigationOnClickListener { finish() }
        tv_content.text = Html.fromHtml(getString(R.string.about_content))
        tv_logo.text = "V ${getVersionName(this)}"
    }

    private fun getVersionName(context: Context): String {
        try {
            val packageManager = context.packageManager
            return packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}
