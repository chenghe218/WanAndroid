package com.leo.wan.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.leo.wan.R
import com.leo.wan.fragment.ArticleCollectionFragment
import com.leo.wan.fragment.WebCollectionFragment
import kotlinx.android.synthetic.main.activity_all_collection.*
import kotlinx.android.synthetic.main.activity_all_collection.viewPager

class AllCollectionActivity : BaseActivity() {

    private var fragmentList = mutableListOf<Fragment>()
    private var nameList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_collection)
        initView()
    }

    private fun initView() {
        fragmentList.add(ArticleCollectionFragment.getInstance())
        fragmentList.add(WebCollectionFragment.getInstance())
        nameList.add(getString(R.string.collection_article))
        nameList.add(getString(R.string.collection_website))
        toolbar.setNavigationOnClickListener {
            finish()
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
