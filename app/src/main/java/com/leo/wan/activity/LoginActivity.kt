package com.leo.wan.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.leo.wan.*
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.UserBean
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus


class LoginActivity : BaseActivity(), View.OnClickListener {

    val dialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }
    private var remName = SPManager.getString(this, SPContent.SP_REMNAME, "")
    private var remPw = SPManager.getString(this, SPContent.SP_REMPW, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (remName.isNotEmpty()) {
            tv_name.setText(remName)
            tv_pw.setText(remPw)
            checkbox.isChecked = true
        }
        init()
    }

    private fun init() {
        bt_login_registered.setOnClickListener(this)
        bt_login.setOnClickListener(this)
        tv_tip.setOnClickListener(this)
        tv_tip_registered.setOnClickListener(this)
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (tv_name.text.toString().isEmpty() || tv_pw.text.toString().isEmpty()) {
                    toast(getString(R.string.tip_info))
                    checkbox.isChecked = false
                } else {
                    SPManager.saveString(this, SPContent.SP_REMNAME, tv_name.text.toString())
                    SPManager.saveString(this, SPContent.SP_REMPW, tv_pw.text.toString())
                }
            } else {
                SPManager.saveString(this, SPContent.SP_REMNAME, "")
                SPManager.saveString(this, SPContent.SP_REMPW, "")
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.bt_login -> {
                if (tv_name.text.toString().isEmpty() || tv_pw.text.toString().isEmpty()) {
                    toast(getString(R.string.tip_info))
                    return
                }
                if (tv_name.text.toString().length < 6) {
                    toast(getString(R.string.tip_num_6))
                    return
                }
                if (tv_pw.text.toString().length < 6) {
                    toast(getString(R.string.tip_pw_6))
                    return
                }
                dialog.showDialog(this)
                NetWorkManager.getNetApi().login(
                    tv_name.text.toString(),
                    tv_pw.text.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseBean<UserBean>> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(userBean: BaseBean<UserBean>) {
                            dialog.dismissDialog()
                            toast(getString(R.string.login_success))
                            EventBus.getDefault().post(LoginEvent(true))
                            SPManager.saveString(this@LoginActivity, SPContent.SP_NAME, userBean.data.username)
                            SPManager.saveInt(this@LoginActivity, SPContent.SP_ID, userBean.data.id)
                            setResult(Activity.RESULT_OK, Intent())
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            dialog.dismissDialog()
                            toastError(e)
                        }

                        override fun onComplete() {

                        }
                    })

            }
            R.id.bt_login_registered -> {
                if (tv_name_registered.text.toString().isEmpty() || tv_pw_registered.text.toString().isEmpty()
                    || tv_pw_registered1.text.toString().isEmpty()
                ) {
                    toast(getString(R.string.tip_info))
                    return
                }
                if (tv_name_registered.text.toString().length < 6) {
                    toast(getString(R.string.tip_num_6))
                    return
                }
                if (tv_pw_registered.text.toString().length < 6 || tv_pw_registered1.text.toString().length < 6) {
                    toast(getString(R.string.tip_pw_6))
                    return
                }
                if (tv_pw_registered.text.toString() != tv_pw_registered1.text.toString()) {
                    toast(getString(R.string.tip_pw_error))
                    return
                }
                dialog.showDialog(this)
                NetWorkManager.getNetApi().register(
                    tv_name_registered.text.toString(),
                    tv_pw_registered.text.toString(), tv_pw_registered1.text.toString()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseBean<Any>> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(model: BaseBean<Any>) {
                            dialog.dismissDialog()
                            toast(getString(R.string.registered_success))
                            group1.visibility = View.GONE
                            group.visibility = View.VISIBLE
                        }

                        override fun onError(e: Throwable) {
                            dialog.dismissDialog()
                            toastError(e)
                        }

                        override fun onComplete() {

                        }
                    })
            }
            R.id.tv_tip -> {
                group.visibility = View.GONE
                group1.visibility = View.VISIBLE
                tv_name.text = Editable.Factory.getInstance().newEditable("")
                tv_pw.text = Editable.Factory.getInstance().newEditable("")
            }
            R.id.tv_tip_registered -> {
                group1.visibility = View.GONE
                group.visibility = View.VISIBLE
            }
        }

    }


}
