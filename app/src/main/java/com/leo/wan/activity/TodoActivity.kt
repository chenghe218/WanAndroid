package com.leo.wan.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.leo.wan.R
import com.leo.wan.adapter.TodoAdapter
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.dialog.TodoTipDialog
import com.leo.wan.model.TodoBean
import com.leo.wan.toastError
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : BaseActivity() {

    lateinit var todoAdapter: TodoAdapter
    var page: Int = 1
    var todoList = ArrayList<TodoBean.DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        initView()
    }

    override fun onResume() {
        page = 1
        getTodoList()
        super.onResume()
    }

    private fun initView() {
        initRefreshLayout()
        toolbar.run {
            title = "TODO"
            setNavigationOnClickListener {
                finish()
            }
        }
        button.setOnClickListener {
            startActivity(Intent(this, AddTodoActivity::class.java))
        }
        todoAdapter = TodoAdapter(this)
        rvData.adapter = todoAdapter
        todoAdapter.setOnItemClickListener { _, _, position ->
            Intent(this, AddTodoActivity::class.java).run {
                putExtra("data", todoAdapter.datas[position])
                startActivity(this)
            }
        }
        todoAdapter.setOnItemLongClickListener { _, _, position ->
            val text = if (todoAdapter.datas[position].status == 0) getString(R.string.mark_as_completed)
            else getString(R.string.mark_as_incomplete)
            TodoTipDialog().newInstance(text).apply {
                listener = {
                    modifyTodoStatus(todoAdapter.datas[position].id,
                            if (todoAdapter.datas[position].status == 0) 1
                            else 0, position)
                }
                deleteListener = {
                    deleteTodo(todoAdapter.datas[position].id, position)
                }
                show(supportFragmentManager, "")
            }
            true
        }
    }

    private fun initRefreshLayout() {
        refreshLayout?.run {
            setOnLoadMoreListener {
                page += 1
                getTodoList()
            }
            setOnRefreshListener {
                page = 1
                getTodoList()
            }
        }
    }

    /**
     * 获取TODO列表
     */
    private fun getTodoList() {
        NetWorkManager.getNetApi().getTodoList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<TodoBean>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<TodoBean>) {
                        refreshLayout.finishLoadMore()
                        refreshLayout.finishRefresh()
                        if (page == 1) {
                            todoList.clear()
                        }
                        baseBean.data.datas?.let { todoList.addAll(it) }
                        todoAdapter.datas = todoList
                        if (page < baseBean.data.pageCount)
                            refreshLayout.setEnableLoadMore(true) else refreshLayout.setEnableLoadMore(false)

                    }

                    override fun onError(e: Throwable) {
                        toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    /**
     * 删除一个TODO
     */
    private fun deleteTodo(id: Int, position: Int) {
        NetWorkManager.getNetApi().deleteTodo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<Any>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<Any>) {
                        todoAdapter.datas.remove(todoAdapter.datas[position])
                        todoAdapter.notifyDataSetChanged()
                        Snackbar.make(constraintLayout, getString(R.string.delete_success), Snackbar.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }

    /**
     * 更新TODO完成状态
     */
    private fun modifyTodoStatus(id: Int, status: Int, position: Int) {
        NetWorkManager.getNetApi().modifyTodoStatus(id, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseBean<Any>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(baseBean: BaseBean<Any>) {
                        todoAdapter.datas[position].status = if (todoAdapter.datas[position].status == 0) 1
                        else 0
                        todoAdapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {
                        toastError(e)
                    }

                    override fun onComplete() {

                    }
                })
    }
}
