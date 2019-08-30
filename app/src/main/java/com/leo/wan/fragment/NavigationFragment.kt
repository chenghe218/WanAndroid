package com.leo.wan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.leo.wan.R

/**
 * @Description:
 * @Author:         ch
 * @CreateDate:     2019/8/12 14:53
 */
class NavigationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_navigation, null)
        return view
    }
}