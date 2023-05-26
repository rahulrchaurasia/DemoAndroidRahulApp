package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentStartBinding


class StartFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding : FragmentStartBinding? = null

    private lateinit var bottomNavigationView : BottomNavigationView

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentStartBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.GONE
        binding.btnStart.setOnClickListener {

            val action = StartFragmentDirections.actionStartFragmentToHomeDashBoardFragment()

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.d(Constant.TAG,"Start Fragment :---- onResume triggered..")
    }


}