package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChooseReceiverBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}