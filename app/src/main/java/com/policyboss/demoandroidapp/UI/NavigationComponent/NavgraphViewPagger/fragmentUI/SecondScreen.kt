package com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentSecondScreenBinding


class SecondScreen : Fragment() {


    private   var _binding : FragmentSecondScreenBinding? = null

   private val binding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_second_screen, container, false)

        _binding = FragmentSecondScreenBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager  = activity?.findViewById<ViewPager2>(R.id.viewPagerDemo)
        binding.next2.setOnClickListener{

            viewPager?.currentItem = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}