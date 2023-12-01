package com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.viewPagger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI.FirstScreen
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI.SecondScreen
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI.ThirdScreen


class ViewPagerFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPagerDemo)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter
        return view
    }

}