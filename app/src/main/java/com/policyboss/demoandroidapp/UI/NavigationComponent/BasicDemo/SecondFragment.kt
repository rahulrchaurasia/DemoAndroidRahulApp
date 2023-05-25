package com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentFirstBinding
import com.policyboss.demoandroidapp.databinding.FragmentSecondBinding


class SecondFragment : Fragment() {

    private var _binding : FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = SecondFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}