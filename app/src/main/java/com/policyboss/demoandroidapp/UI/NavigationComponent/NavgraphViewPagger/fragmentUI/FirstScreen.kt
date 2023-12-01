package com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentFirstScreenBinding


class FirstScreen : Fragment() {
    // TODO: Rename and change types of parameters


    private var _binding : FragmentFirstScreenBinding? = null

    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // val view =  inflater.inflate(R.layout.fragment_first_screen, container, false)

        _binding = FragmentFirstScreenBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPagerDemo)
        val btnNext = view?.findViewById<TextView>(R.id.next)



        binding.next.setOnClickListener{

            viewPager?.currentItem = 1
        }
    }


}