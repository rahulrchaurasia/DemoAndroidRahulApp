package com.policyboss.demoandroidapp.ShareViewModel.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ShareViewModel.ViewModelDemo2
import com.policyboss.demoandroidapp.databinding.FragmentTest1Binding
import com.policyboss.demoandroidapp.databinding.FragmentTest2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TestFragment1 : Fragment() {

    private var _binding : FragmentTest2Binding? = null
    private val viewModelDemo2 : ViewModelDemo2 by activityViewModels()  // we have to use activityViewModels bec viewModel share by activity and it give activity instance
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTest2Binding.inflate(inflater,container,false)

        viewModelDemo2.data.observe(viewLifecycleOwner, {

            binding.txtfrag2.setText(it)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}