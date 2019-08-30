package com.leo.wan

import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
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