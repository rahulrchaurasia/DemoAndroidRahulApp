package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.NavGraphDirections
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.FragmentChooseReceiverBinding
import com.policyboss.demoandroidapp.databinding.FragmentHomeDashBoardBinding
import com.policyboss.demoandroidapp.databinding.FragmentViewTransactionBinding

/*
     ********* Snding Bundle Data *****************
     *Using Bundle we have to use normal navigation using xml id not an action
     * eg findNavController().navigate(R.id.sendCashFragment, args = bundle,navOptions = null)
     *
 */
class ChooseReceiverFragment : Fragment() {

    private var _binding : FragmentChooseReceiverBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Constant.TAG,"ChooseReceiverFragment : onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChooseReceiverBinding.inflate(inflater,container,false)

        Log.d(Constant.TAG,"ChooseReceiverFragment : onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(Constant.TAG,"ChooseReceiverFragment : onViewCreated")
        setupMenu()

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_home_24) // Optional: set custom navigation icon
        }

        binding.btnNext.setOnClickListener{

            var etReceivername = binding.etReceiverName.text.toString()

            //************* Use Normal Bundle Without Nav Argument ******************************
//            val bundle = Bundle()
//
//            bundle.putString(Constant.bundl_name_key,etReceivername)
//
//            findNavController().navigate(R.id.sendCashFragment, args = bundle,navOptions = getNavOption())

            //************* //************* send Data using Navargs ************* //************* //************* //*************

            val action =  ChooseReceiverFragmentDirections
                       .actionChooseReceiverFragmentToSendCashFragment(etReceivername)
            findNavController().navigate(action)
        }

        binding.btnCancel.setOnClickListener{

            // remove the currebt destination from Stack
            findNavController().popBackStack()


//            val action =  ChooseReceiverFragmentDirections
//                .actionChooseReceiverFragmentToSendCashFragment("demo")
//            findNavController().navigate(action)
        }
    }


    // Add Menu in fragment
    fun setupMenu(){

        // For Creating Menu
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.demo_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){

                    R.id.action_camera ->{

                        requireActivity().showToast("action_camera")
                    }
                    R.id.action_second ->{

                        val action = NavGraphDirections.actionGlobalAboutAppFragment()
                        findNavController().navigate(action)
                        requireActivity().showToast("action_second")
                    }
                }
                return false
            }



        },viewLifecycleOwner,Lifecycle.State.RESUMED)
    }
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.demo_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item!!,
//            requireView().findNavController())
//                || super.onOptionsItemSelected(item)
//    }
   fun getNavOption() : NavOptions {

     return  NavOptions.Builder()
           .setEnterAnim(R.anim.slide_in_right)
           .setExitAnim(R.anim.slide_out_left)
           .setPopEnterAnim(R.anim.slide_in_left)
           .setPopExitAnim(R.anim.slide_out_right)
           .build()
   }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = ChooseReceiverFragment()
    }
    override fun onResume() {
        super.onResume()

        Log.d(Constant.TAG,"ChooseReceiverFragment : onResume")


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(Constant.TAG,"ChooseReceiverFragment : onDestroyView")

    }


}