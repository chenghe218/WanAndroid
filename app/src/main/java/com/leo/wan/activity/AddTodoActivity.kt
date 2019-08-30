package com.leo.wan.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import com.leo.wan.R
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.model.AddTodoBean
import com.leo.wan.model.TodoBean
import com.leo.wan.toast
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.text.SimpleDateFormat
import java.util.*


class AddTodoActivity : BaseActivity() {

    var priorityType: Int = 0
    private var todoBean: TodoBean.DataBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        todoBean = intent.getSerializableExtra("data") as? TodoBean.DataBean
        initView()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initView() {
        tvDate.text = SimpleDateFormat("yyyy-MM-dd").format(Date())
        toolbar.run {
            title = getString(R.string.todo_add)
            setNavigationOnClickListener {
                finish()
            }
        }
        todoBean?.let {
            toolbar.title = getString(R.string.todo_modify)
            tvTitle.setText(it.title)
            tvContent.setText(it.content)
            if (it.priority == 0) tvGeneral.isChecked = true else tvImportant.isChecked = true
            tvDate.text = it.dateStr
        }
        button.setOnClickListener {
            if (toolbar.title == getString(R.string.todo_add)) saveTodo() else modifyTodo()
        }
        tvDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        tvDate.text = "$year-${month + 1}-$dayOfMonth"
                    }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
    }

    /**
     * 新增一个TODO
     */
    private fun saveTodo() {
        val title = tvTitle.text.toString()
        val content = tvContent.text.toString()
        priorityType = if (tvGeneral.isChecked) 0 else 1
        val date = tvDate.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            toast(getString(R.string.tip_info))
            return
        } else {
            NetWorkManager.getNetApi().addTodo(title, content, date, 0, priorityType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseBean<AddTodoBean>> {
                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(baseBean: BaseBean<AddTodoBean>) {
                            toast(getString(R.string.todo_save_success))
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            toastError(e)
                        }

                        override fun onComplete() {

                        }
                    })
        }
    }

    /**
     * 更新一个TODO
     */
    private fun modifyTodo() {
        val title = tvTitle.text.toString()
        val content = tvContent.text.toString()
        priorityType = if (tvGeneral.isChecked) 0 else 1
        val date = tvDate.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            toast(getString(R.string.tip_info))
            return
        } else {
            todoBean?.let {
                NetWorkManager.getNetApi().modifyTodo(it.id, title, content, date, it.status, 0, priorityType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<BaseBean<Any>> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(baseBean: BaseBean<Any>) {
                                toast(getString(R.string.todo_save_success))
                                finish()
                            }

                            override fun onError(e: Throwable) {
                                toastError(e)
                            }

                            override fun onComplete() {

                            }
                        })
            }

        }
    }
}
