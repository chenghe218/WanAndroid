package com.leo.wan

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.leo.wan.base.NetWorkManager
import com.leo.wan.util.LanguageUtil
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager


/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/8/14 14:15
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NetWorkManager.getInstance().init(applicationContext)
        if (SPManager.getBoolean(applicationContext, SPContent.SP_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        languageWork()
    }

    private fun languageWork() {
        val locale = LanguageUtil.getLocale(this)
        LanguageUtil.updateLocale(this, locale)
    }
}
