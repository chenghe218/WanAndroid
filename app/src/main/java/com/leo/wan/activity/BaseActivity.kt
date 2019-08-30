package com.leo.wan.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.leo.wan.util.LanguageUtil

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        val context = languageWork(newBase)
        super.attachBaseContext(context)

    }

    private fun languageWork(context: Context): Context {
        // 8.0及以上使用createConfigurationContext设置configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            updateResources(context)
        } else {
            context
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun updateResources(context: Context): Context {
        val resources = context.resources
        val locale = LanguageUtil.getLocale(context) ?: return context
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return context.createConfigurationContext(configuration)
    }


}
