package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentABinding
import com.policyboss.demoandroidapp.databinding.FragmentBBinding


class FragmentB : Fragment() {

    private var _binding : FragmentBBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentB()

    }
}