package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo.FirstFragment
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment.FragmentA
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment.FragmentB

class CustomTabPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {

        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if(position == 0){
            FragmentA()
        }else{
            FragmentB()
        }
    }


}