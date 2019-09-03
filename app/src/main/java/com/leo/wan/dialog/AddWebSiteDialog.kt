package com.leo.wan.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.leo.wan.R
import com.leo.wan.toast

/**
 * @Author Leo
 * @DATE 2019/5/13 16:23
 * @description
 */
class AddWebSiteDialog : DialogFragment() {

    lateinit var saveListener: (String, String) -> Unit

    fun newInstance(title: String, name: String?, link: String?): AddWebSiteDialog {
        val args = Bundle()
        args.putString("title", title)
        args.putString("name", name)
        args.putString("link", link)
        val dialog = AddWebSiteDialog()
        dialog.arguments = args
        return dialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val contentView = LayoutInflater.from(activity).inflate(
            R.layout.dialog_add_website,
            GridLayout(activity), false
        )
        val mDialog = Dialog(activity!!, R.style.ActionSheet)
        val tvTitle = contentView.findViewById<TextView>(R.id.tvTitle)
        val save = contentView.findViewById<TextView>(R.id.save)
        val etLink = contentView.findViewById<EditText>(R.id.etLink)
        val etName = contentView.findViewById<EditText>(R.id.etName)
        arguments?.let {
            tvTitle.text = it.getString("title")
            if (!it.getString("title").isNullOrEmpty())
                etName.setText(it.getString("name"))
            if (!it.getString("link").isNullOrEmpty())
                etLink.setText(it.getString("link"))
        }

        save.setOnClickListener {
            if (etName.text.toString().isEmpty() || etLink.text.toString().isEmpty()) {
                context?.toast(getString(R.string.tip_info))
                return@setOnClickListener
            }
            saveListener(etName.text.toString(), etLink.text.toString())
            dismiss()
        }

        val window = mDialog.window
        mDialog.setContentView(contentView)
        window!!.setGravity(Gravity.CENTER)
        return mDialog
    }

}
