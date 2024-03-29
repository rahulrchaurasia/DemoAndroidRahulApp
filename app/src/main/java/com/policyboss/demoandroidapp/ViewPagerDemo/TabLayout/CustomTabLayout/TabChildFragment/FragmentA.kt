package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.TabChildFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentABinding
import com.policyboss.demoandroidapp.databinding.FragmentChooseReceiverBinding


class FragmentA : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding : FragmentABinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentABinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_a, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentA()

    }
}