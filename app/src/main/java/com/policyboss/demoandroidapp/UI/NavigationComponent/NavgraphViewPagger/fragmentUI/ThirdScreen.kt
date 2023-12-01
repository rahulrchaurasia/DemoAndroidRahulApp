package com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger.fragmentUI

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentSecondScreenBinding
import com.policyboss.demoandroidapp.databinding.FragmentThirdScreenBinding


class ThirdScreen : Fragment() {

    private   var _binding : FragmentThirdScreenBinding? = null

    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentThirdScreenBinding.inflate(inflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finish.setOnClickListener{

            findNavController().navigate(R.id.action_viewPagerFragment_to_homeViewpagerFragment)
           // onBoardingFinished()
        // Note : temp05 commnted to see viewpager flow but actually it req
        }
    }


    private fun onBoardingFinished(){
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}