package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

//import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.dataModel.SampleData
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.FragmentSendCashBinding


/*
Back navigation in Fragment :---
https://medium.com/@azureli/back-navigation-in-fragment-134c0fc5ba9e

The popUpToInclusive attribute determines whether the destination specified in popUpTo should also be removed from the back stack.
By default,
popUpToInclusive is set to false, which means the destination specified in popUpTo is 'not removed' from the back stack.
popUpToInclusive to true, it indicates that the destination specified in popUpTo should 'also be removed' along with the destinations above it.
 */

class sendCashFragment : Fragment()  , OnClickListener{

    private var _binding : FragmentSendCashBinding? = null
    private val binding get() = _binding!!

    private val args: sendCashFragmentArgs by navArgs()     // Declare Nav argument


    private val callback = object : OnBackPressedCallback(true){

        override fun handleOnBackPressed() {

            findNavController().popBackStack(R.id.homeDashBoardFragment,false)
        }


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentSendCashBinding.inflate(inflater,container,false)


        setOnClickListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val toolbar = requireActivity().findViewById<Toolbar>(com.policyboss.demoandroidapp.R.id.toolbar)

//        toolbar.setNavigationOnClickListener {
//
//
//            binding.root.findNavController().popBackStack(R.id.homeDashBoardFragment,false)
//
//        }

        // *************** Receiver Using Bundle  ***************

//        if(arguments?.getString(Constant.bundl_name_key) != null){
//
//            binding.txtReceiver.setText("Send cash to ${arguments?.getString(Constant.bundl_name_key) ?: ""}")
//        }

        // *************** Receiver Using nav argument  ***************

       //  binding.etAmount.setText(SampleData.defaultAmount.value.toString())


        // region BackPressHandling Use this or below both are same
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // Your custom back navigation logic here
//                // For example:
//                Toast.makeText(requireContext(), "Custom back press in fragment", Toast.LENGTH_SHORT).show()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        //endregion
        SampleData.defaultAmount.observe(viewLifecycleOwner){

            binding.etAmount.setText(it.toString())
        }
        binding.etAmount.requestFocus()
        val receiverName = args.receiverName


        binding.txtReceiver.text = "Send cash to "+ receiverName

         //********************* For Handling Back Button action programatically..*********************
        //********************* ********************* ********************* *********************

         requireActivity()
             .onBackPressedDispatcher
            // .addCallback(viewLifecycleOwner,callback)
             .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
                 override fun handleOnBackPressed() {
                     binding.root.findNavController().popBackStack(R.id.homeDashBoardFragment,false)

                 }

             })

    }


    fun setOnClickListener(){

        binding.btnSend.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback.remove()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onClick(view: View?) {

        when(view?.id){

            binding.btnSend.id ->{

                val action = sendCashFragmentDirections.actionSendCashFragmentToConfirmDialogFragment( args.receiverName,binding.etAmount.text.toString().toLong())

                findNavController().navigate(action )

            }

            binding.btnCancel.id ->{

            /*
          //Note :
        // popUpToInclusive to true :-- Remove the in between the destination along with  destination specified
                             ie Home  should also be removed along with the destinations above it
                             Here homeDashBoardFragment Fragment is also remove

         popUpToInclusive is set to false :-- which means the destination specified in popUpTo is not removed from the back stack.
                         Here homeDashBoardFragment Fragment is not also remove
                */

             //  findNavController().popBackStack(R.id.homeDashBoardFragment,true) // Include destination to remove

                /**************** PopBackStack *********************************/

                findNavController().popBackStack(R.id.homeDashBoardFragment,false)  //Include destination not to  remove

            }

            binding.btnDone.id ->{

                val action = sendCashFragmentDirections.actionSendCashFragmentToHomeDashBoardFragment()

                findNavController().navigate(action)

            }
        }
    }


}