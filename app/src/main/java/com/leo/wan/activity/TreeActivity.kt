package com.leo.wan.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.leo.wan.R
import com.leo.wan.fragment.TreeListFragment
import com.leo.wan.model.TreeBean
import kotlinx.android.synthetic.main.activity_tree.*
import kotlinx.android.synthetic.main.fragment_project.tabLayout
import kotlinx.android.synthetic.main.fragment_project.viewPager

class TreeActivity : BaseActivity() {

    private lateinit var treeBean: TreeBean
    var nameList = ArrayList<String?>()
    private var fragmentList = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree)
        treeBean = intent.getSerializableExtra("data") as TreeBean
        initView()
    }

    private fun initView() {
        toolbar.title = treeBean.name
        treeBean.children?.forEach {
            nameList.add(it.name)
            fragmentList.add(TreeListFragment.newInstance(it.id))
        }
        viewPager.run {
            adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment = fragmentList[position]

                override fun getCount(): Int = nameList.size

                override fun getPageTitle(position: Int): CharSequence? = nameList[position]
            }
            tabLayout.setupWithViewPager(this)
        }
    }
}
