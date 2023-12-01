package com.policyboss.demoandroidapp.ActivityLifecycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.policyboss.demoandroidapp.AlertDialog.customActivityAsDialog
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.Utility.hideKeyboard
import com.policyboss.demoandroidapp.databinding.FragmentLifeCycleDdemoBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLifeCycleDdemo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLifeCycleDdemo : Fragment() {


    private var _binding : FragmentLifeCycleDdemoBinding? = null
    lateinit var layout : View

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MyFragment: onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.d(TAG, "MyFragment: onCreateView")
        // Inflate the layout for this fragment
        _binding = FragmentLifeCycleDdemoBinding.inflate(inflater,container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "MyFragment: onViewCreated")
        layout =  binding.root

        requireContext().hideKeyboard(layout)

        binding.imgClose.setOnClickListener{

            startActivity(Intent( this.context, customActivityAsDialog::class.java))

        }

    }

    //region Fragment Lifecycle

    override fun onAttach(context: Context) {

        super.onAttach(context)
        Log.d(TAG, "MyFragment: onAttach")
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "MyFragment: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MyFragment: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MyFragment: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "MyFragment: onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "MyFragment: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MyFragment: onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "MyFragment: onDetach")
    }

    //endregion

    companion object {
                @JvmStatic fun newInstance() =
                FragmentLifeCycleDdemo()
    }
}