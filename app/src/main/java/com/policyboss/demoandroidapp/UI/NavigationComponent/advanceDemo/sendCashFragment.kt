package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

//import android.R
import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.UI.NavigationComponent.PushNotificationEntity
import com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.dataModel.SampleData
import com.policyboss.demoandroidapp.Utility.NotificationHelper
import com.policyboss.demoandroidapp.Utility.NotificationHelperNavGraph
import com.policyboss.demoandroidapp.Utility.showSnackbar
import com.policyboss.demoandroidapp.databinding.FragmentSendCashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/*
Back navigation in Fragment :---
https://medium.com/@azureli/back-navigation-in-fragment-134c0fc5ba9e

The popUpToInclusive attribute determines whether the destination specified in popUpTo should also be removed from the back stack.
By default,
popUpToInclusive is set to false, which means the destination specified in popUpTo is 'not removed' from the back stack.
popUpToInclusive to true, it indicates that the destination specified in popUpTo should 'also be removed' along with the destinations above it.
 */

@AndroidEntryPoint
class sendCashFragment : Fragment()  , OnClickListener{

    private var _binding : FragmentSendCashBinding? = null
    private val binding get() = _binding!!

    private val args: sendCashFragmentArgs by navArgs()     // Declare Nav argument NotificationHelperNavGraph

    @Inject  lateinit var notificationHelper: NotificationHelper

    @Inject  lateinit var notificationHelperNavGraph: NotificationHelperNavGraph

    //regioncomment no need here
//    private val callback = object : OnBackPressedCallback(true){
//
//        override fun handleOnBackPressed() {
//
//            findNavController().popBackStack(R.id.homeDashBoardFragment,false)
//        }
//
//
//    }

    //endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(Constant.TAG,"SendFragment : onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(Constant.TAG,"SendFragment : onCreateView")
        _binding = FragmentSendCashBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reqPermission()
        setupMenu()
        setOnClickListener()

       // notificationHelper = NotificationHelper(requireContext())
      //  notificationHelper.init()
        // val toolbar = requireActivity().findViewById<Toolbar>(com.policyboss.demoandroidapp.R.id.toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {

            title = "SendCashFragment :"

        }
        //region commented
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

        //endregion

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
                     binding.root.findNavController().popBackStack(com.policyboss.demoandroidapp.R.id.homeDashBoardFragment,true)

                 }

             })

    }


    fun setOnClickListener(){

        binding.btnSend.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)
        binding.btnCustomToolBar.setOnClickListener(this)
        binding.btnGlobalAction.setOnClickListener(this)
        binding.btnNotification.setOnClickListener(this)
        binding.btnNotificationGraph.setOnClickListener(this)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(Constant.TAG,"SendFragment : onDestroyView")
       // callback.remove()
    }

    override fun onResume() {
        super.onResume()
        Log.d(Constant.TAG,"SendFragment : onResume")

    }

    fun reqPermission(){

        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            Constant.REQUEST_ID_POST_NOTIFICATION
        )
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            @SuppressLint("UnsafeOptInUsageError")
            override fun onPrepareMenu(menu: Menu) {

                val menuItem = menu.findItem(com.policyboss.demoandroidapp.R.id.menuRechargeRefresh)
                val actionView = menuItem.actionView
                actionView?.setOnClickListener {

                    requireContext().showSnackbar(binding.root, "Refresh 22")
                }
//                val view = menu.getItem(0).actionView
//                view?.findViewById<ImageView>(R.id.imgMenuWallet)?.let {
//                    it.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            requireContext(),
//                            R.drawable.ic_account_balance_wallet
//                        )
//                    )
//                }
//
//                view?.findViewById<TextView>(R.id.txtMenuWalletBalance)?.let {
//                    it.text =
//                        "${resources.getString(R.string.rupee_symbol)}${300}"
//                }
            }



            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(com.policyboss.demoandroidapp.R.menu.transaction_menu, menu)


            }



            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item


                return when (menuItem.itemId) {
                    com.policyboss.demoandroidapp.R.id.menuRechargeWalletBalance -> {
                        requireContext().showSnackbar(binding.root, "Wallet clicked")
                        return true
                    }
                    com.policyboss.demoandroidapp.R.id.menuRechargeRefresh -> {
                        requireContext().showSnackbar(binding.root, "Refresh")
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

                findNavController().popBackStack(com.policyboss.demoandroidapp.R.id.homeDashBoardFragment,false)  //Include destination not to  remove

               // findNavController().popBackStack()



            }

            binding.btnDone.id ->{

                val action = sendCashFragmentDirections.actionSendCashFragmentToHomeDashBoardFragment()

                findNavController().navigate(action)



            }

            binding.btnGlobalAction.id->{

                val action = sendCashFragmentDirections.actionGlobalAboutAppFragment()
               findNavController().navigate(action)
            }
            binding.btnCustomToolBar.id -> {

                val action = sendCashFragmentDirections.actionSendCashFragmentToCustomToolbarFragment()

                findNavController().navigate(action)

            }

            binding.btnNotification.id->{

                val pushNotification = PushNotificationEntity("Sender is SendCash FragmentMessage", "Data From Notification", 100.0, "https://example.com/logo.png")

                notificationHelper.sendNotification("NavGraph Data", "Notification come from Normal way in Main Activity", pushNotification)

            }

            binding.btnNotificationGraph.id->{

                val pushNotification = PushNotificationEntity("Sender is SendCash FragmentMessage", "Data From Notification", 100.0, "https://example.com/logo.png")

                notificationHelperNavGraph.sendNotification("NavGraph Data", "Notification come from NavGraph tarhet diectly to destnation fragment", pushNotification)

            }
        }
    }


}