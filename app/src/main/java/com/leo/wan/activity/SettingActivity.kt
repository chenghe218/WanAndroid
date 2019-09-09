package com.leo.wan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.leo.wan.MainActivity
import com.leo.wan.R
import com.leo.wan.util.LanguageUtil
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        toolbar.setNavigationOnClickListener { finish() }
        init()
    }

    private fun init() {
        language.text = if (SPManager.getInt(this@SettingActivity, SPContent.SP_NO, 0) == 0)
            getString(R.string.simplified_chinese) else getString(R.string.english)
        checkbox.isChecked = SPManager.getBoolean(this, SPContent.SP_WIFI, false)
        tv_no_pic.setOnClickListener(this)
        tv_no_pic_dis.setOnClickListener(this)
        choose_language.setOnClickListener(this)
        language.setOnClickListener(this)
        checkbox.setOnCheckedChangeListener { _, b ->
            if (b) {
                SPManager.saveBoolean(this, SPContent.SP_WIFI, true)
            } else {
                SPManager.saveBoolean(this, SPContent.SP_WIFI, false)
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.tv_no_pic, R.id.tv_no_pic_dis -> {
                if (checkbox.isChecked) {
                    checkbox.isChecked = false
                    SPManager.saveBoolean(this, SPContent.SP_WIFI, false)
                } else {
                    checkbox.isChecked = true
                    SPManager.saveBoolean(this, SPContent.SP_WIFI, true)
                }
            }
            R.id.choose_language, R.id.language -> {
                val items = arrayOf(getString(R.string.simplified_chinese), getString(R.string.english))
                var nex = 0
                val tipDialog = AlertDialog.Builder(this).apply {
                    this.setTitle(getString(R.string.choose_language))
                    this.setSingleChoiceItems(items, SPManager.getInt(this@SettingActivity, SPContent.SP_NO, 0)) { _, p1 ->
                        nex = p1
                    }
                    this.setPositiveButton(getString(R.string.sure)) { _, _ ->
                        when (nex) {
                            0 -> {
                                if (LanguageUtil.updateLocale(this@SettingActivity, LanguageUtil.LOCALE_CHINESE)) {
                                    val intent = Intent(this@SettingActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    language.text = items[0]
                                }
                            }
                            1 -> {
                                if (LanguageUtil.updateLocale(this@SettingActivity, LanguageUtil.LOCALE_ENGLISH)) {
                                    val intent = Intent(this@SettingActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    language.text = items[1]
                                }
                            }
                        }
                        SPManager.saveInt(this@SettingActivity, SPContent.SP_NO, nex)
                    }
                }
                tipDialog.show()
            }
        }
    }
}
