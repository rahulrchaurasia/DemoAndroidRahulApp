package com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.FragmentFirstBinding


class FirstFragment : Fragment() , OnClickListener{
    // TODO: Rename and change types of parameters

    private var _binding: FragmentFirstBinding? = null
  lateinit var navController : NavController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

       // navController= findNavController()

        binding.btnAction.setOnClickListener(this)
    }

    companion object {

        @JvmStatic
        fun newInstance() = FirstFragment()
    }



    override fun onClick(view: View?) {

        when(view?.id)  {

            binding.btnAction.id ->{

              findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}