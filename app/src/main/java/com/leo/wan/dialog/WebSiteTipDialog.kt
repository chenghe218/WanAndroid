package com.leo.wan.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.leo.wan.R

/**
 * @Author Leo
 * @DATE 2019/5/13 16:23
 * @description
 */
class WebSiteTipDialog : DialogFragment() {

    lateinit var openListener: () -> Unit
    lateinit var deleteListener: () -> Unit
    lateinit var copyListener: () -> Unit
    lateinit var modifyListener: () -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contentView = LayoutInflater.from(activity).inflate(
            R.layout.dialog_website_tip,
            GridLayout(activity), false
        )
        val mDialog = Dialog(activity!!, R.style.ActionSheet)
        val tvOpen = contentView.findViewById<TextView>(R.id.tvOpen)
        val tvDelete = contentView.findViewById<TextView>(R.id.tvDelete)
        val tvCopy = contentView.findViewById<TextView>(R.id.tvCopy)
        val tvModify = contentView.findViewById<TextView>(R.id.tvModify)

        tvCopy.setOnClickListener {
            copyListener()
            dismiss()
        }

        tvModify.setOnClickListener {
            modifyListener()
            dismiss()
        }

        tvOpen.setOnClickListener {
            openListener()
            dismiss()
        }

        tvDelete.setOnClickListener {
            deleteListener()
            dismiss()
        }

        val window = mDialog.window
        mDialog.setContentView(contentView)
        window!!.setGravity(Gravity.CENTER)
        return mDialog
    }

}
