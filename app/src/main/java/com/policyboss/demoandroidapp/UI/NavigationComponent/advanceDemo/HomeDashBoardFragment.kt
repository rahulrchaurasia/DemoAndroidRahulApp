package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo.FirstFragment
import com.policyboss.demoandroidapp.Utility.hideKeyboard
import com.policyboss.demoandroidapp.databinding.FragmentHomeDashBoardBinding


class HomeDashBoardFragment : Fragment() , OnClickListener {


    private var _binding : FragmentHomeDashBoardBinding? = null
    private lateinit var bottomNavigationView : BottomNavigationView

    lateinit var navController : NavController
    lateinit var layout : View

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Log.d(Constant.TAG,"Home Fragment onCreateView triggered..")
        _binding = FragmentHomeDashBoardBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout =  binding.root
        navController =  findNavController()

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.VISIBLE

        requireContext().hideKeyboard(layout)

        setOnClickListener()

    }

    override fun onResume() {
        super.onResume()
        Log.d(Constant.TAG,"Home Fragment :---- onResume triggered..")
    }
    fun setOnClickListener(){

        binding.btnSendMoney.setOnClickListener(this)
        binding.btnViewBalance.setOnClickListener(this)
        binding.btnTransaction.setOnClickListener(this)
    }




    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = HomeDashBoardFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {

        when(view?.id){

            binding.btnViewBalance.id ->{

                val action = HomeDashBoardFragmentDirections.actionHomeDashBoardFragmentToViewBalanceFragment()
                navController.navigate(action)

            }

            binding.btnTransaction.id ->{

                //Note : we can use action via xml
                val action = HomeDashBoardFragmentDirections.actionHomeDashBoardFragmentToViewTransactionFragment()
                //navController.navigate(R.id.action_homeDashBoardFragment_to_viewTransactionFragment)  //via xml
                //navController.navigate(R.id.viewTransactionFragment)  // via xml

                navController.navigate(action)

            }

            binding.btnSendMoney.id ->{

                val action = HomeDashBoardFragmentDirections.actionHomeDashBoardFragmentToChooseReceiverFragment()
                navController.navigate(action)
            }


        }
    }
}