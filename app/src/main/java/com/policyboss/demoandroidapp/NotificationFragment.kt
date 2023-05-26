package com.policyboss.demoandroidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policyboss.demoandroidapp.databinding.FragmentNotificationBinding



class NotificationFragment : Fragment() {

     var _binding : FragmentNotificationBinding ? = null

     val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)

        _binding = FragmentNotificationBinding.inflate(inflater,container,false)

       return binding.root
    }




}