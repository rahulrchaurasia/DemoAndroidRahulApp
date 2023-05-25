package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentViewTransactionBinding


class ViewTransactionFragment : Fragment() {


    private var _binding : FragmentViewTransactionBinding ? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_view_transaction, container, false)

        _binding = FragmentViewTransactionBinding.inflate(inflater,container,false)

        return binding.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ViewTransactionFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}