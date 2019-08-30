package com.leo.wan.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.leo.wan.R
import kotlinx.android.synthetic.main.dialog_todo_tip.*

/**
 * @Author Leo
 * @DATE 2019/5/13 16:23
 * @description
 */
class TodoTipDialog : DialogFragment() {

    lateinit var listener: () -> Unit
    lateinit var deleteListener: () -> Unit

    fun newInstance(text: String): TodoTipDialog {
        val args = Bundle()
        args.putString("text", text)
        val dialog = TodoTipDialog()
        dialog.arguments = args
        return dialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contentView = LayoutInflater.from(activity).inflate(R.layout.dialog_todo_tip,
                GridLayout(activity), false)
        val mDialog = Dialog(activity!!, R.style.ActionSheet)
        val tvMark=contentView.findViewById<TextView>(R.id.tvMark)
        val tvDelete=contentView.findViewById<TextView>(R.id.tvDelete)
        tvMark.text = arguments?.getString("text")

        tvMark.setOnClickListener {
            listener()
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
