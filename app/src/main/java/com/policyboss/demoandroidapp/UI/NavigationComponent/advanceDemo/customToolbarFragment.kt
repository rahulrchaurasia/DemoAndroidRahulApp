package com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.NavGraphDirections
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.activity.NavigationAdvanceCallback
import com.policyboss.demoandroidapp.databinding.FragmentCustomToolbarBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//Note : here we hide and show Appbar using Interface callback to NavigationAdvanceActivity

/*
activity as? NavigationAdvanceCallback:
activity: This refers to the current activity associated with the fragment.
as?: This is a safe cast operator in Kotlin. It attempts to cast the activity to the NavigationAdvanceCallback interface type, but if the cast fails (i.e., the activity doesn't implement the interface), it returns null.
NavigationAdvanceCallback: This is the name of the interface you defined.

 */
class customToolbarFragment : Fragment(),View.OnClickListener {


        var _binding : FragmentCustomToolbarBinding? = null

        private lateinit var activity: AppCompatActivity
        private val binding get() =   _binding!!

       // private lateinit var offsetChangedListenerProperty: AppBarLayout.OnOffsetChangedListener
        var  thresholdOffset = 0

    private val offsetChangedListenerProperty: AppBarLayout.OnOffsetChangedListener by lazy {
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            binding.offset.text =
                " thresholdOffset ${thresholdOffset} verticalOffset  ${verticalOffset} "

            lifecycleScope.launch(Dispatchers.Main) {
                if (verticalOffset <=  thresholdOffset) {
                    // Vertical offset is zero, which means content is at the top

                    //delay(1000) // Introduce a 1-second delay

                    binding.pinnedLayut.elevation = 40f // Set the elevation to 40dp
                    binding.pinnedLayut.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.off_white
                        )
                    ) // Change the background color

                    binding.imgBack.visibility = View.VISIBLE


                } else  {

                    //delay(1000)
                    binding.pinnedLayut.elevation = 0f // Set the elevation to 0dp
                    binding.pinnedLayut.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.lightGrey
                        )
                    ) // Restore the original background color
                    binding.imgBack.visibility = View.GONE


                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as  AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_custom_toolbar, container, false)

        _binding = FragmentCustomToolbarBinding.inflate(inflater,container,false)

       return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hide onlt toolbar not required actualy...
        //activity.supportActionBar?.hide()

        // Access the interface and hide the AppBarLayout : we are gone the Visibilty of Actity's appBar
        (activity as? NavigationAdvanceCallback)?.hideAppBar()

        //Handle custom Toolbat/ appBar
        binding.imgBack.visibility = View.GONE

        getCollapseToolbarHeight()

        binding.imgBack.setOnClickListener(this)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {

                    findNavController().popBackStack(R.id.chooseReceiverFragment,false)


                   // val action = NavGraphDirections.actionGlobalAboutAppFragment()
                    //findNavController().navigate(action)


                }

            })

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // Hide the common toolbar when this fragment is visible
        if (!hidden) {

           // activity.supportActionBar?.hide()
        }
    }


    private fun getCollapseToolbarHeight(){

        // Obtain the thresholdOffset after the layout is calculated

        val viewTreeObserver = binding.appBar.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // Check if the view is still alive and in a valid state
                if (binding.appBar != null && binding.appBar.isAttachedToWindow) {

                    if (binding.appBar.isLaidOut && binding.collapsingToolbar.isLaidOut) {
                        // Check if the viewTreeObserver is attached to the appBar view

                        // Calculate the threshold value for verticalOffset based on view heights
                        thresholdOffset = -binding.collapsingToolbar.height

                        // Remove the listener to avoid multiple calls
                        binding.appBar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }


                }

            }
        })

    }


    override fun onClick(view: View?) {

        when(view?.id){

            binding.imgBack.id ->{

                // findNavController().popBackStack(R.id.homeDashBoardFragment,false)
                // reach till chooseReceiverFragment : inclusive false mean not remove it
                findNavController().popBackStack(R.id.chooseReceiverFragment,false)
            }
        }
    }
    override fun onStart() {
        super.onStart()

        Log.d(Constant.TAG,"onStart: Added Callback")
        binding.appBar.addOnOffsetChangedListener(offsetChangedListenerProperty)
    }

    override fun onStop() {
        super.onStop()
        offsetChangedListenerProperty?.let {
            binding.appBar.removeOnOffsetChangedListener(offsetChangedListenerProperty)

            Log.d(Constant.TAG,"OnStop: Remove Callback")
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(Constant.TAG,"onDestroy() Called" )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        Log.d(Constant.TAG,"onDestroyView() Called" )
        // Show the common toolbar when this fragment is destroyed
        // Access the interface and show the AppBarLayout
        (activity as? NavigationAdvanceCallback)?.showAppBar()

    }



}