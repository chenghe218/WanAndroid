package com.leo.wan

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.widget.Toast
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/14 15:26
 */

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toastError(e: Throwable) {
    e.message?.let {
        toast(it)
    }
}

fun Long.toStringDate(pattern: String): String {
    if (this < 0 || TextUtils.isEmpty(pattern)) {
        return ""
    }
    val sdf = SimpleDateFormat(pattern, Locale.CHINA)
    return sdf.format(Date(this))
}

fun String.toEncryptionString(): String {
    if (this.isEmpty() || this.length < 3) {
        return ""
    }
    return this.replaceRange(1, 3, "**")
}

fun ProgressDialog.showDialog(context: Context) {
    this.apply {
        setMessage(context.getString(R.string.loading))
        this.show()
    }
}

fun ProgressDialog.dismissDialog() {
    this.dismiss()
}

fun Context.randomColor(): Int {
    val random = Random()
    //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
    var red = random.nextInt(190)
    var green = random.nextInt(190)
    var blue = random.nextInt(190)
    if (SPManager.getBoolean(this,SPContent.SP_MODE,false)) {
        //150-255
        red = random.nextInt(105) + 150
        green = random.nextInt(105) + 150
        blue = random.nextInt(105) + 150
    }
    //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
    return Color.rgb(red, green, blue)
}