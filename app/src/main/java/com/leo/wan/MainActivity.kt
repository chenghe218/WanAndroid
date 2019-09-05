package com.leo.wan

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.billy.android.swipe.SmartSwipeBack
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.leo.wan.activity.*
import com.leo.wan.base.BaseBean
import com.leo.wan.base.NetWorkManager
import com.leo.wan.base.SaveCookiesInterceptor
import com.leo.wan.fragment.*
import com.leo.wan.model.CoinBean
import com.leo.wan.util.SPContent
import com.leo.wan.util.SPManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.head_drawer_layout.*
import kotlinx.android.synthetic.main.head_drawer_layout.view.*
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val fragmentList = mutableListOf<Fragment>()
    private var currentFragment: Fragment? = null
    private var mineFragment: MainFragment? = null
    private var systemFragment: SystemFragment? = null
    private var projectFragment: ProjectFragment? = null
    private var weChatFragment: WeChatFragment? = null
    private var navigationFragment: NavigationFragment? = null

    lateinit var tvCoin: TextView
    lateinit var tvRanking: TextView
    private var mIndex = 0
    private var mode: Boolean = false
    private var firstTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
        //判断是否为切换白夜模式
        if (savedInstanceState != null) {
            switchFragment(mineFragment!!)
            toolbar.title = getString(R.string.menu_main)
        } else {
            currentFragment = mineFragment
            currentFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.frameLayout, it, mineFragment?.javaClass?.name)
                    .commit()
            }
            fragmentList.add(mineFragment!!)
        }

        //主Activity不需要侧滑返回功能，其它Activity都采用仿小米侧滑返回效果
        SmartSwipeBack.activityBezierBack(application, { activity ->
            //根据传入的activity，返回true代表需要侧滑返回；false表示不需要侧滑返回
            activity !is MainActivity
        }, 250)

        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        toolbar.inflateMenu(R.menu.serach_item)
        toolbar.setOnMenuItemClickListener {
            Intent(this, SearchActivity::class.java).run {
                putExtra("search", true)
                startActivity(this)
            }
            true
        }

        val name = SPManager.getString(this, SPContent.SP_NAME, "")
        tvCoin = navigationView.getHeaderView(0).tv_coin
        tvRanking = navigationView.getHeaderView(0).tv_Rank
        if (name.isNotEmpty()) {
            navigationView.getHeaderView(0).tv_name.text = name
            navigationView.menu.findItem(R.id.drawer_login_out).isVisible = true
            getCoin()
        } else {
            tvCoin.text = getString(R.string.coin_null)
            tvRanking.text = getString(R.string.rank_null)
            navigationView.menu.findItem(R.id.drawer_login_out).isVisible = false
        }
        mode = SPManager.getBoolean(applicationContext, SPContent.SP_MODE, false)

        navigationView.menu.findItem(R.id.drawer_mode).title =
            if (mode) getString(R.string.drawer_mode_day) else getString(R.string.drawer_mode_night)

        navigationViewItemSelect()
    }

    /**
     * 左侧菜单点击事件
     */
    private fun navigationViewItemSelect() {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_collection -> {
                    if (tv_name.text == getString(R.string.login)) {
                        toast(getString(R.string.login_un))
                        gotoLogin()
                    } else startActivity(Intent(this, AllCollectionActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.drawer_todo -> {
                    if (tv_name.text == getString(R.string.login)) {
                        toast(getString(R.string.login_un))
                        gotoLogin()
                    } else startActivity(Intent(this, TodoActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.drawer_setting -> {
                    startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.drawer_mode -> {
                    if (mode) {
                        SPManager.saveBoolean2(applicationContext, SPContent.SP_MODE, false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        navigationView.menu.findItem(R.id.drawer_mode).title = getString(R.string.drawer_mode_night)
                    } else {
                        SPManager.saveBoolean2(applicationContext, SPContent.SP_MODE, true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        navigationView.menu.findItem(R.id.drawer_mode).title = getString(R.string.drawer_mode_day)
                    }
                    bottomNavigationView.selectedItemId = R.id.menu_main
                    fragmentList.clear()
                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                    recreate()
                }
                R.id.drawer_aboutUs -> {
                    startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                    drawerLayout.closeDrawers()
                }
                R.id.drawer_login_out -> {
                    if (tv_name.text != getString(R.string.login)) {
                        val tipDialog = AlertDialog.Builder(this).apply {
                            this.setTitle(getString(R.string.tip))
                            this.setMessage(getString(R.string.sign_out_sure))
                            this.setPositiveButton(getString(R.string.sure)) { _, _ -> loginOut() }
                            this.setNegativeButton(getString(R.string.cancel)) { p0, _ -> p0.dismiss() }
                        }
                        tipDialog.show()
                    } else {
                        toast(getString(R.string.login_un))
                    }
                    drawerLayout.closeDrawers()
                }
            }
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", mIndex)
    }

    private fun initFragment() {
        mineFragment ?: MainFragment().let {
            mineFragment = it
        }
        projectFragment ?: ProjectFragment().let {
            projectFragment = it
        }
        systemFragment ?: SystemFragment().let {
            systemFragment = it
        }
        weChatFragment ?: WeChatFragment().let {
            weChatFragment = it
        }
        navigationFragment ?: NavigationFragment().let {
            navigationFragment = it
        }
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.menu_main -> {
                if (!p0.isChecked) {
                    switchFragment(mineFragment!!)
                    toolbar.title = getString(R.string.menu_main)
                    return true
                }
            }
            R.id.menu_project -> {
                if (!p0.isChecked) {
                    switchFragment(projectFragment!!)
                    toolbar.title = getString(R.string.menu_project)
                    return true
                }
            }
            R.id.menu_system -> {
                if (!p0.isChecked) {
                    switchFragment(systemFragment!!)
                    toolbar.title = getString(R.string.menu_system)
                    return true
                }
            }
            R.id.menu_navigation -> {
                if (!p0.isChecked) {
                    switchFragment(navigationFragment!!)
                    toolbar.title = getString(R.string.menu_navigation)
                    return true
                }
            }
            R.id.menu_wechat -> {
                if (!p0.isChecked) {
                    switchFragment(weChatFragment!!)
                    toolbar.title = getString(R.string.menu_wechat)
                    return true
                }
            }

        }
        return true
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager
            .beginTransaction()
        if (!fragmentList.contains(fragment)) {
            currentFragment?.let {
                transaction
                    .hide(it)
                    .add(R.id.frameLayout, fragment)
                    .commit()
            }
            fragmentList.add(fragment)
        } else {
            currentFragment?.let {
                transaction
                    .hide(it)
                    .show(fragment)
                    .commit()
            }
        }
        currentFragment = fragment
    }

    /**
     * 登录
     */
    fun toLogin(view: View) {
        if (tv_name.text == getString(R.string.login)) {
            gotoLogin()
            drawerLayout.closeDrawers()
        }
    }

    /**
     * 个人积分榜
     */
    fun toRank(view: View) {
        if (tv_name.text != getString(R.string.login)) {
            Intent(this, MyCoinRecordActivity::class.java).run {
                putExtra("coin", tvCoin.text)
                startActivity(this)
            }
            drawerLayout.closeDrawers()
        } else {
            toast(getString(R.string.login_un))
            gotoLogin()
        }
    }

    /**
     * 总积分榜
     */
    fun toAllRank(view: View) {
        Intent(this, RankingActivity::class.java).run {
            startActivity(this)
        }
        drawerLayout.closeDrawers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0x123 -> {
                    val name = SPManager.getString(this, SPContent.SP_NAME, "")
                    if (name.isNotEmpty())
                        tv_name.text = name
                    getCoin()
                    navigationView.menu.findItem(R.id.drawer_login_out).isVisible = true
                }
            }
        }
    }

    /**
     * 获取个人积分
     */
    private fun getCoin() {
        NetWorkManager.getNetApi().getCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<CoinBean>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(baseBean: BaseBean<CoinBean>) {
                    tvCoin.text = getString(R.string.coin, baseBean.data.coinCount)
                    tvRanking.text = getString(R.string.rank, baseBean.data.rank)
                }

                override fun onError(e: Throwable) {
                    toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    /**
     * 退出登录
     */
    private fun loginOut() {
        NetWorkManager.getNetApi().loginOut()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseBean<Any>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(userBean: BaseBean<Any>) {
                    toast(getString(R.string.sign_out_success))
                    SPManager.saveString(this@MainActivity, SPContent.SP_NAME, "")
                    SPManager.saveInt(this@MainActivity, SPContent.SP_ID, -1)
                    SPManager.saveBoolean(this@MainActivity, SPContent.SP_WIFI, false)
                    SaveCookiesInterceptor.clearCookie(this@MainActivity)
                    tvCoin.text = getString(R.string.coin_null)
                    tvRanking.text = getString(R.string.rank_null)
                    tv_name.text = getString(R.string.login)
                    EventBus.getDefault().post(LoginEvent(false))
                    navigationView.menu.findItem(R.id.drawer_login_out).isVisible = false
                    drawerLayout.closeDrawers()
                }

                override fun onError(e: Throwable) {
                    toastError(e)
                }

                override fun onComplete() {

                }
            })
    }

    private fun gotoLogin() {
        startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java), 0x123)
    }

    /**
     * 双击退出程序
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val secondTime = System.currentTimeMillis()
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime <= 2000) {
                finish()
            } else {
                toast(getString(R.string.exit))
                firstTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

