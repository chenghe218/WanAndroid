package com.leo.wan.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @Description:
 * @Author: ch
 * @CreateDate: 2019/7/30 20:37
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected V mBinding;
    protected VM mViewModel;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewDataBinding();
        mContext = this.getApplicationContext();
        initData();
        initViewObservable();
    }


    /**
     * 注入绑定DataBinding
     */
    private void initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, initContentView());
        mBinding.setVariable(initVariableId(), mViewModel = initViewModel());
        mBinding.executePendingBindings();
        mBinding.setLifecycleOwner(this);
    }

    /**
     * 初始化布局文件
     */
    public abstract int initContentView();

    /**
     * 初始化ViewModel
     */
    public abstract VM initViewModel();

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id(根据对应xml文件 < data > < variable > name < / variable > < / data > 自动生成)
     */
    public abstract int initVariableId();

    /**
     * 处理LiveData的订阅方法
     */
    public abstract void initViewObservable();

    /**
     刷新布局
     */
    public void refreshLayout() {
        if (mViewModel != null) {
            mBinding.setVariable(initVariableId(), mViewModel);
            mBinding.executePendingBindings();
        }
    }

    /**
     * 初始化方法
     */
    public abstract void initData();

}
