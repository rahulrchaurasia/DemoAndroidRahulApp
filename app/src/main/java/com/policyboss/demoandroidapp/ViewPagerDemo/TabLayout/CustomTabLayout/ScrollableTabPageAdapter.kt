package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo.FirstFragment
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment.FragmentA
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment.FragmentB

class ScrollableTabPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {

        return 6
    }

    override fun createFragment(position: Int): Fragment {



        return  when(position){

            0 ->
                FragmentA()

            1 ->
                FragmentB()
            2 ->
                FragmentA()
            3 ->
                FragmentB()
            4 ->
                FragmentA()

            5 ->
                FragmentB()

            else ->{
                FragmentA()
            }


        }
    }


}